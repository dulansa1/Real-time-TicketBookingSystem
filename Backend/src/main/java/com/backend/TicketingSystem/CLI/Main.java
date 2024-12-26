package com.backend.TicketingSystem.CLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String configFileName = "config.json";
        SystemConfig config;

        // Ask the user for the preferred configuration method
        System.out.println("Welcome! Choose your configuration method:");
        System.out.println("1. JSON way (load from or create a config.json file)");
        System.out.println("2. Manual way (enter configuration manually)");
        System.out.print("Enter your choice (1 or 2): ");

        String choice = scanner.next();
        if (choice.equals("1")) {
            // Load configuration or create default configuration if not found
            config = SystemConfig.loadFromJson(configFileName);
            if (config == null) {
                // A new configuration with default values
                config = new SystemConfig();
                config.saveToJson(configFileName);
                System.out.println("Default configuration created and saved to " + configFileName);
            }
        } else if (choice.equals("2")) {
            // Manual configuration
            config = new SystemConfig();
            config.configureSystem();
            config.saveToJson(configFileName); // Optionally save the manual configuration
            System.out.println("Manual configuration saved to " + configFileName);
        } else {
            System.out.println("Invalid choice. Exiting program.");
            return; // Exit the program if the choice is invalid
        }

        // Use the configuration
        int totalTickets = config.getTotalTickets();
        int ticketReleaseRate = config.getTicketReleaseRate();
        int customerRetrievalRate = config.getCustomerRetrievalRate();
        int maxTicketCapacity = config.getMaxTicketCapacity();

        TicketPool pool = new TicketPool(maxTicketCapacity, totalTickets);

        // Create and start vendor threads
        int numVendors = 3; // Define the number of vendor threads
        List<Thread> vendorThreads = new ArrayList<>();

        for (int i = 0; i < numVendors; i++) {
            Thread vendorThread = new Thread(new Vendor(pool, ticketReleaseRate), "Vendor-" + (i + 1));
            vendorThreads.add(vendorThread);
            vendorThread.start(); // Start each vendor thread
        }

        // Create and start customer threads
        int numCustomers = 4; // Define the number of vendor threads
        List<Thread> customerThreads = new ArrayList<>();

        for (int i = 0; i < numCustomers; i++) {
            Thread customerThread = new Thread(new Customer(pool, customerRetrievalRate), "Customer-" + (i + 1));
            customerThreads.add(customerThread);
            customerThread.start(); // Start each vendor thread
        }
    }
}
