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
    public synchronized List<Integer> addTickets(int count) {
        List<Integer> addedTickets = new ArrayList<>();
        try {
            while (tickets.size() >= maxTicketCapacity && nextTicketId <= totalTickets) {
                logAndPrint(Thread.currentThread().getName() + " is waiting to add tickets.");
                wait(); // Wait until there is space in the pool
            }

            if (nextTicketId > totalTickets) {
                logAndPrint(Thread.currentThread().getName() + " tried to add tickets," +
                        " but total tickets limit reached.");
                notifyAll(); // Wake up any waiting customers
                return addedTickets;
            }

            int ticketsToAdd = Math.min(count, maxTicketCapacity - tickets.size());
            for (int i = 0; i < ticketsToAdd; i++) {
                if (nextTicketId <= totalTickets) {
                    tickets.add(nextTicketId);
                    addedTickets.add(nextTicketId++);
                }
            }

            logAndPrint(Thread.currentThread().getName() + " added " + ticketsToAdd +
                    " tickets. (Ticket IDs: " + addedTickets + ") Total: " + tickets.size());
            notifyAll(); // Notify waiting customers
        } catch (Exception e) {
            handleError("Error adding tickets: ", e);
        }
        return addedTickets;
    }

    public synchronized List<Integer> removeTicket(int count) {
        List<Integer> removedTickets = new ArrayList<>();
        try {
            while (tickets.isEmpty() && nextTicketId <= totalTickets) {
                logAndPrint(Thread.currentThread().getName() + " is waiting to purchase tickets.");
                wait(); // Wait until tickets are available
            }

            if (tickets.isEmpty() && nextTicketId > totalTickets) {
                logAndPrint(Thread.currentThread().getName() + " tried to buy tickets," +
                        " but tickets are sold out.");
                notifyAll(); // Wake up any waiting vendors
                return removedTickets;
            }

            int ticketsToRemove = Math.min(count, tickets.size());
            for (int i = 0; i < ticketsToRemove; i++) {
                removedTickets.add(tickets.remove(0));
            }

            logAndPrint(Thread.currentThread().getName() + " purchased " + ticketsToRemove +
                    " tickets. (Ticket IDs: " + removedTickets + ") Remaining: " + tickets.size());
            notifyAll(); // Notify waiting vendors
        } catch (Exception e) {
            handleError("Error removing tickets: ", e);
        }
        return removedTickets;
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

    public synchronized List<String> getLogs() {
        return new ArrayList<>(logs); // Return a copy of the logs to avoid concurrency issues
    }

    public synchronized void clearLogs() {
        logs.clear(); // Clear logs when a new simulation starts or when needed
    }


    private void handleError(String context, Exception e) {
        String errorMessage = context + e.getMessage();
        logAndPrint(errorMessage);
    }
    public int getTotalTickets() {
        return totalTickets;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

}