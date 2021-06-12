package com.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.model.AdjustmentMessage;
import com.model.Message;
import com.model.Product;
import com.processor.Register;

public class SalesEngine {
	
    private static SalesEngine salesEngine = new SalesEngine();
    private Register register;

    private static final long MESSAGE_CAPACITY = 50;

    private SalesEngine() {
        this.register = new Register();
    }

    public static SalesEngine getSalesEngine() {
        return salesEngine;
    }

    public boolean initialize(String stockFile) {
        BufferedReader stockBuffer = null;

        try {
            String stockEntry;
            stockBuffer = new BufferedReader(new FileReader(stockFile));

            while((stockEntry = stockBuffer.readLine()) != null) {
                boolean productAdded = register.addProduct(parseStockEntry(stockEntry));

                if(!productAdded) {
                    System.out.println("Stock updation has failed.");
                }
            }
        } catch(IOException exception) {
            exception.printStackTrace();
        } finally {
            if(stockBuffer != null) {
                try {
                    stockBuffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    private Product parseStockEntry(String stockEntry) {
        if(stockEntry == null) {
            return null;
        }

        String[] productData = stockEntry.split("\\s*,\\s*");

        if(productData.length != 4) {
            System.out.println("Too much or too less product data. Please correct the data.");
            return null;
        }

        Product product = null;

        try {
            product = new Product(productData[0], 
                    Long.valueOf(productData[1]),
                    Long.valueOf(productData[2]),
                    Double.valueOf(productData[3]));
        } catch (NumberFormatException exception) {
            System.out.println("Product count and/or pricing is incorrect. Please correct the data.");
        }

        return product;
    }

    public List<Message> parse(String notificationsFile) {
        List<Message> messages = null;
        ObjectMapper mapper = new ObjectMapper();

        try {
            messages = mapper.readValue(new File(notificationsFile), new TypeReference<List<Message>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public boolean process(List<Message> messages) {
        int processedMessages = 0;
        StringBuilder adjustmentsLog = new StringBuilder();

        for(Message message : messages) {
            boolean recordsUpdated = register.updateRecords(message);
            if(!recordsUpdated) {
                return false;
            }

            processedMessages++;

            if(message instanceof AdjustmentMessage) {
                adjustmentsLog.append("Product (");
                adjustmentsLog.append(message.getType());
                adjustmentsLog.append(") was adjusted (operation: ");
                adjustmentsLog.append(((AdjustmentMessage) message).getOperationType());
                adjustmentsLog.append(") by a value of ");
                adjustmentsLog.append(message.getSellingPrice());
                adjustmentsLog.append(" at approximately ");
                adjustmentsLog.append(new Date());
                adjustmentsLog.append(".\n");
            }

            if(processedMessages % 10 == 0) {
                System.out.println("Intermediate Processed Sales Record");
                register.printSalesReport();
            }

            if(processedMessages == MESSAGE_CAPACITY) {
                System.out.println("Maximum message processing limit has reached, hence stopping the processing.");
                break;
            }
        }

        System.out.println("Final Sales Report");
        register.printSalesReport();

        if(adjustmentsLog.length() != 0) {
            System.out.println("Adjustment Log");
            System.out.println(adjustmentsLog.toString());
        }

        return true;
    }
}