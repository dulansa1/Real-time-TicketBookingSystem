package com.backend.TicketingSystem.CLI;

import java.util.Scanner;
import java.io.*;
import com.google.gson.Gson;

public class SystemConfig {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    private transient Scanner scanner = new Scanner(System.in); // Transient to avoid serialization

    // Default constructor with default values
    public SystemConfig() {
        this.totalTickets =100;
        this.ticketReleaseRate = 10;
        this.customerRetrievalRate = 5;
        this.maxTicketCapacity = 500;
    }

    public void configureSystem() {
        totalTickets = getValidInput("Enter total number of tickets: ");
        ticketReleaseRate = getValidInput("Enter ticket release rate (per second): ");
        customerRetrievalRate = getValidInput("Enter customer retrieval rate (per second): ");
        maxTicketCapacity = getValidInput("Enter maximum ticket capacity: ");
    }

    private int getValidInput(String prompt) {
        int value = -1;
        while (value < 0) {
            System.out.println(prompt);
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value < 0) {
                    System.out.println("Please enter a non-negative value.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return value;
    }

    // Save configuration to a JSON file
    public void saveToJson(String filename) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(this, writer);
            System.out.println("Configuration saved to JSON file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving configuration to JSON: " + e.getMessage());
        }
    }

    // Load configuration from a JSON file
    public static SystemConfig loadFromJson(String filename) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, SystemConfig.class);
        } catch (IOException e) {
            System.out.println("Configuration file not found or invalid. Creating default configuration...");
            return null; // File not found or invalid
        }
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
}


