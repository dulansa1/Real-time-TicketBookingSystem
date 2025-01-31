package com.backend.TicketingSystem.CLI;

import java.util.Scanner;
import java.io.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemConfig {
    private int totalTickets; // Total number of tickets vendors can add
    private int ticketReleaseRate; // Number of tickets a vendor can release per second
    private int customerRetrievalRate; // Number of tickets a customer can retrieve per second
    private int maxTicketCapacity; // The max tickets a customer can buy or a vendor can release at a time

    private transient Scanner scanner = new Scanner(System.in); // Transient to avoid serialization

    public void configureSystem() {
        totalTickets = getValidInput("Enter total number of tickets (Total tickets vendors can add): ");
        ticketReleaseRate = getValidInput("Enter ticket release rate (No. of tickets vendors can release per second): ");
        customerRetrievalRate = getValidInput("Enter customer retrieval rate (No. of tickets customers can retrieve per second): ");
        maxTicketCapacity = getValidInput("Enter maximum ticket capacity (Max no.of tickets that can be in the ticketpool): ");
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
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(filename), this);
            System.out.println("Configuration saved to JSON file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving configuration to JSON: " + e.getMessage());
        }
    }

    // Load configuration from a JSON file
    public static SystemConfig loadFromJson(String filename) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(new File(filename), SystemConfig.class);
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
