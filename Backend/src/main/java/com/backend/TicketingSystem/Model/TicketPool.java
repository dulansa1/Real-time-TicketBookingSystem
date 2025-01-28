package com.backend.TicketingSystem.Model;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<Integer> tickets;
    private final int maxTicketCapacity;
    private final int totalTickets;
    private final FileWriter logWriter;
    private int nextTicketId = 1; // Start ticket IDs from 1
    private final List<String> logs;

    public TicketPool(int maxTicketCapacity, int totalTickets) {
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTickets = totalTickets;
        this.logs = new ArrayList<>();


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
                addedTickets.setLength(addedTickets.length() - 2);
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
            while (tickets.isEmpty() && nextTicketId <= totalTickets) {
                logAndPrint(Thread.currentThread().getName() + " is waiting to purchase tickets.");
                wait(); // Wait until tickets are available
            }

            if (tickets.isEmpty() && nextTicketId > totalTickets) {
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
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logMessage = timestamp + " - " + message;

        System.out.println(logMessage); // Log to console
        logs.add(logMessage); // Store the log message in the list for real-time access

        if (logWriter != null) {
            try {
                logWriter.write(logMessage + "\n");
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

    public synchronized List<String> getLogs() {
        return new ArrayList<>(logs); // Return a copy of the logs to avoid concurrency issues
    }

    public synchronized void clearLogs() {
        logs.clear(); // Clear logs when a new simulation starts or when needed
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

}