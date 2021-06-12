package com.main;

import com.processor.SalesEngine;
import com.model.Message;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
    	
        if(args.length != 2) {
            System.out.println("Please provide two arguments for processing the data");
            System.exit(1);
        }

        if(isInvalidFilePath(args[0])) {
            System.out.println("File path in argumnent 1 is invalid. Please provide correct path for processing the data.");
            System.exit(1);
        }
        
        if(isInvalidFilePath(args[1])) {
            System.out.println("File path in argument 2 is invalid. Please provide correct path for processing the data.");
            System.exit(1);
        }
        
        

        String stockFile = args[0];
        String notificationFile = args[1];

        SalesEngine salesEngine = SalesEngine.getSalesEngine();

        boolean initialized = salesEngine.initialize(stockFile);
        
        if(!initialized) {
            System.out.println("Unable to initialize stock engine. Further information about the error is logged in console.");
            System.exit(1);
        }

        List<Message> messages = salesEngine.parse(notificationFile);
        
        if(messages == null) {
            System.out.println("Notifications data parsing has failed.  Further information about the error is logged in console.");
            System.exit(1);
        }

        boolean processed = salesEngine.process(messages);
        
        if(!processed) {
            System.out.println("Sales processing has failed.  Further information about the error is logged in console.");
            System.exit(1);
        }
    }

    private static boolean isInvalidFilePath(String filePath) {
        try {
        	
            Path path = Paths.get(filePath);

            if(Files.notExists(path)) {
            	System.out.println("File path does not exist.");
                return true;
            }

            if(!Files.isRegularFile(path)) {
            	System.out.println("File path is not a regular file.");
                return true;
            }


            if(!Files.isReadable(path)) {
            	System.out.println("File path is not readable.");
                return true;
            }
            
        } catch (InvalidPathException | NullPointerException exception) {
            return true;
        }

        return false;
    }
}
