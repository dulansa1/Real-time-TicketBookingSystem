package com.backend.TicketingSystem.CLI;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<Integer> tickets;
    private final int maxTicketCapacity;
    private final int totalTickets;
    private final FileWriter logWriter;
    private int nextTicketId = 1; // Start ticket IDs from 1

    public TicketPool(int maxTicketCapacity, int totalTickets) {
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;

        // Initialize logWriter
        FileWriter writer = null;
        try {
            writer = new FileWriter("system_log.txt", true); // Open in append mode
        } catch (IOException e) {
            System.out.println("Error initializing log writer: " + e.getMessage());
        }
        this.logWriter = writer;
    }

    // Vendor adds tickets
// Vendor adds tickets
    public synchronized void addTickets(int count) {
        try {
            while (tickets.size() >= maxTicketCapacity) {
                logAndPrint(Thread.currentThread().getName() + " is waiting to add tickets.");
                wait(); // Wait until there is space in the pool
            }
            if (nextTicketId > totalTickets) {
                logAndPrint(Thread.currentThread().getName() + " tried to add tickets, but total tickets limit reached.");
                return;
            }

            int ticketsToAdd = Math.min(count, maxTicketCapacity - tickets.size());
            StringBuilder addedTickets = new StringBuilder();
            for (int i = 0; i < ticketsToAdd; i++) {
                if (nextTicketId <= totalTickets) {
                    int ticketId = nextTicketId++;
                    tickets.add(ticketId);
                    addedTickets.append("Ticket id=").append(ticketId).append(", ");
                }
            }

            if (addedTickets.length() > 0) {
                addedTickets.setLength(addedTickets.length() - 2); // Remove trailing comma and space
            }

            logAndPrint(Thread.currentThread().getName() + " added " + ticketsToAdd + " tickets. (" + addedTickets + ") Total: " + tickets.size());
            notifyAll(); // Notify customers that tickets are available
        } catch (Exception e) {
            handleError("Error adding tickets: ", e);
        }
    }

    // Customer removes (purchases) tickets
    public synchronized void removeTicket(int count) {
        try {
            while (tickets.isEmpty()) {
                logAndPrint(Thread.currentThread().getName() + " is waiting to purchase tickets.");
                wait(); // Wait until tickets are available
            }

            if (nextTicketId > totalTickets) {
                logAndPrint(Thread.currentThread().getName() + " trying to buy tickets, but tickets are sold out.");
                return;
            }

            int ticketsToRemove = Math.min(count, tickets.size());
            StringBuilder retrievedTickets = new StringBuilder();
            for (int i = 0; i < ticketsToRemove; i++) {
                if (!tickets.isEmpty()) {
                    int ticketId = tickets.remove(0);
                    retrievedTickets.append("Ticket id=").append(ticketId).append(", ");
                }
            }

            if (retrievedTickets.length() > 0) {
                retrievedTickets.setLength(retrievedTickets.length() - 2); // Remove trailing comma and space
            }

            logAndPrint(Thread.currentThread().getName() + " purchased " + ticketsToRemove + " tickets. (" + retrievedTickets + ") Remaining: " + tickets.size());
            notifyAll(); // Notify vendors that space is available
        } catch (Exception e) {
            handleError("Error removing tickets: ", e);
        }
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
