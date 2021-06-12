package com.processor;



import java.util.*;

import com.enums.OperationType;
import com.enums.TransactionStatus;
import com.model.AdjustmentMessage;
import com.model.DetailedMessage;
import com.model.Message;
import com.model.Product;
import com.model.Transaction;

public class Register {
    
	private Map<Product, List<Transaction>> records;

    public Register() {
        this.records = new HashMap<>();
    }

    public Register(Map<Product, List<Transaction>> records) {
        this.records = records;
    }

    public Map<Product, List<Transaction>> getRecords() {
        return records;
    }

    public boolean addProduct(Product product) {
        if(product == null || records.containsKey(product)) {
            return false;
        }

        records.put(product, new ArrayList<>());
        return true;
    }

    public boolean updateRecords(Message message) {
        if(message == null) {
            System.out.println("Invalid sales record.");
            return false;
        }

        Product product = findProduct(message.getType());
        if(product == null) {
            System.out.println("Invalid sales record. Product was not in stock.");
            return false;
        }

        List<Transaction> transactions = records.get(product);

        if(message instanceof AdjustmentMessage) {
            transactions = adjustTransactions(transactions, message);
        } else if(message instanceof DetailedMessage) {
            transactions = addNewTransactions(transactions, message);
        } else {
            examineNotification(message);
            transactions.add(new Transaction(message.getSellingPrice()));
        }

        if(transactions.size() == 0) {
            System.out.println("There are no product " + message.getType() +
                    " related sales to adjust. Doing nothing.");
            return false;
        }

        records.put(product, transactions);
        return true;
    }

    private Product findProduct(String productType) {
        Set<Product> products = records.keySet();

        for(Product product : products) {
            if(productType.equals(product.getType())) {
                return product;
            }
        }

        return null;
    }

    private List<Transaction> adjustTransactions(List<Transaction> transactions, Message message) {
        OperationType operationType = ((AdjustmentMessage) message).getOperationType();

        switch(operationType) {
            case ADD:
                for(Transaction transaction : transactions) {
                    transaction.setValue(transaction.getValue() + message.getSellingPrice());
                    transaction.setTransactionStatus(TransactionStatus.ADJUSTED);
                }
                break;
            case MULTIPLY:
                for(Transaction transaction : transactions) {
                    transaction.setValue(transaction.getValue() * message.getSellingPrice());
                    transaction.setTransactionStatus(TransactionStatus.ADJUSTED);
                }
                break;
            case SUBTRACT:
                for(Transaction transaction : transactions) {
                    if(transaction.getValue() < message.getSellingPrice()) {
                       System.out.println("Potential loss detected for [Product type: " +
                               message.getType() + ", Existing value: " + transaction.getValue() +
                               ", Selling price: " + message.getSellingPrice() +
                               ", during processing.");
                    }

                    transaction.setValue(transaction.getValue() - message.getSellingPrice());
                    transaction.setTransactionStatus(TransactionStatus.ADJUSTED);
                }
                break;
            default:
                System.out.println("Currently this operation is not supported by the system.");
                break;
        }

        return transactions;
    }

    private List<Transaction> addNewTransactions(List<Transaction> transactions, Message message) {
        double price = message.getSellingPrice();
        long transactionsCount = ((DetailedMessage) message).getInstanceCount();

        examineNotification(message);

        if(transactionsCount <= 0) {
            System.out.println("Invalid sales instances, it should be greater than 0 for processing.");
            return transactions;
        }

        for(long i=0; i<transactionsCount; i++) {
            transactions.add(new Transaction(price));
        }

        return transactions;
    }

    private void examineNotification(Message message) {
        if(message.getSellingPrice() <= 0) {
            System.out.println("Logging the free distribution of goods and money for Product " +
                    message.getType() + ", Selling price: " + message.getSellingPrice() +
                    "].");
        }
    }

    public void printSalesReport() {
        for(Map.Entry<Product, List<Transaction>> record : records.entrySet()) {
            System.out.println("Product type: " + record.getKey().getType() +
                    ", Total units sold: " + record.getValue().size() +
                    ", Revenue generated: " + getRevenueForProduct(record.getValue())
            );
        }
    }

    private double getRevenueForProduct(List<Transaction> transactions) {
        double revenueGenerated = 0;

        for(Transaction transaction : transactions) {
            revenueGenerated += transaction.getValue();
        }

        return revenueGenerated;
    }
}
