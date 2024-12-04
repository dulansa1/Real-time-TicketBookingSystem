package com.backend.TicketingSystem.CLI;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<Integer> tickets;
    private final int maxCapacity;
    private final FileWriter logWriter;
    private int nextTicketId = 1; // Unique ticket ID generator

    public TicketPool(int maxCapacity, int initialTotalTickets) {
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.maxCapacity = maxCapacity;

        // Initialize the ticket pool with the initial total tickets
        for (int i = 0; i < initialTotalTickets; i++) {
            tickets.add(nextTicketId++);
        }

        // Initialize logWriter
        FileWriter writer = null;
        try {
            writer = new FileWriter("system_log.txt", true); // Open in append mode
        } catch (IOException e) {
            System.out.println("Error initializing log writer: " + e.getMessage());
        }
        this.logWriter = writer;
    }

    public synchronized void addTickets(int count) {
        try {
            if (tickets.size() + count <= maxCapacity) {
                StringBuilder addedTickets = new StringBuilder();
                for (int i = 0; i < count; i++) {
                    int ticketId = nextTicketId++;
                    tickets.add(ticketId);
                    addedTickets.append("id=").append(ticketId).append(", ");
                }
                // Remove trailing comma and space
                if (addedTickets.length() > 0) {
                    addedTickets.setLength(addedTickets.length() - 2);
                }
                logAndPrint(Thread.currentThread().getName() + " added " + count + " tickets. (" + addedTickets + ")"+" Total: " + tickets.size() );
            } else {
                logAndPrint(Thread.currentThread().getName() + " tried to add tickets, but capacity is full.");
            }
        } catch (Exception e) {
            handleError("Error adding tickets: ", e);
        }
    }

    public synchronized void removeTicket(int count) {
        try {
            if (tickets.size() >= count) {
                StringBuilder retrievedTickets = new StringBuilder();
                for (int i = 0; i < count; i++) {
                    int ticketId = tickets.remove(0);
                    retrievedTickets.append("id=").append(ticketId).append(", ");
                }
                // Remove trailing comma and space
                if (retrievedTickets.length() > 0) {
                    retrievedTickets.setLength(retrievedTickets.length() - 2);
                }
                logAndPrint(Thread.currentThread().getName() + " purchased " + count + " tickets. (" + retrievedTickets + ")"+" Remaining: " + tickets.size() );

            } else {
                logAndPrint(Thread.currentThread().getName() + " is waiting to retrieve tickets.");
            }
        } catch (Exception e) {
            handleError("Error removing tickets: ", e);
        }
    }

    public int getAvailableTickets() {
        return tickets.size();
    }

    public int getMaxTicketCapacity() {
        return maxCapacity;
    }

    private void logAndPrint(String message) {
        System.out.println(message);
        if (logWriter != null) {
            try {
                logWriter.write(message + "\n");
                logWriter.flush();
            } catch (IOException e) {
                System.out.println("Error writing to log file: " + e.getMessage());
            }
        }
    }

    private void handleError(String context, Exception e) {
        String errorMessage = context + e.getMessage();
        logAndPrint(errorMessage);
    }
}
