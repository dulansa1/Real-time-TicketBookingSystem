package com.backend.TicketingSystem.CLI;
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

        String choice = scanner.nextLine();
        if (choice.equals("1")) {
            // Load configuration or create default configuration if not found
            config = SystemConfig.loadFromJson(configFileName);
            if (config == null) {
                // Create a new configuration with default values
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
        int initialTotalTickets = config.getTotalTickets();
        int maxCapacity = config.getMaxTicketCapacity();
        int ticketReleaseRate = config.getTicketReleaseRate();
        int customerRetrievalRate = config.getCustomerRetrievalRate();

        TicketPool pool = new TicketPool(maxCapacity, initialTotalTickets);

        // Create and start vendor threads
        Thread vendor1 = new Thread(new Vendor(pool, ticketReleaseRate), "Vendor-1");
        Thread vendor2 = new Thread(new Vendor(pool, ticketReleaseRate), "Vendor-2");
        Thread vendor3 = new Thread(new Vendor(pool, ticketReleaseRate), "Vendor-3");

        // Create and start customer threads
        Thread customer1 = new Thread(new Customer(pool, customerRetrievalRate), "Customer-1");
        Thread customer2 = new Thread(new Customer(pool, customerRetrievalRate), "Customer-2");
        Thread customer3 = new Thread(new Customer(pool, customerRetrievalRate), "Customer-3");

        // Start threads
        vendor1.start();
        vendor2.start();
        vendor3.start();
        customer1.start();
        customer2.start();
        customer3.start();
    }
}
