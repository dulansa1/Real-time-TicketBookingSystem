package com.backend.TicketingSystem.Service;

import com.backend.TicketingSystem.Model.Customer;
import com.backend.TicketingSystem.Model.TicketPool;
import com.backend.TicketingSystem.Model.Vendor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketSimulationService {
    private boolean running = false;
    List<Thread> vendorThreads = new ArrayList<>();
    List<Thread> customerThreads = new ArrayList<>();
    private TicketPool ticketPool;


    public synchronized void startSimulation(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxCapacity) {
        if (running) {
            stopSimulation();//stop any simulation before starting a new one
        }

        this.ticketPool = new TicketPool(maxCapacity, totalTickets);
        this.ticketPool.clearLogs();//Clear logs before starting a new simulation

        // Create and start vendor threads
        int numVendors = 3;

        for (int i = 0; i < numVendors; i++) {
            Thread vendorThread = new Thread(new Vendor(ticketPool, ticketReleaseRate), "Vendor-" + (i + 1));
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }

        // Create and start customer threads
        int numCustomers = 4;

        for (int i = 0; i < numCustomers; i++) {
            Thread customerThread = new Thread(new Customer(ticketPool, customerRetrievalRate), "Customer-" + (i + 1));
            customerThreads.add(customerThread);
            customerThread.start();
        }
        running = true;
    }

    public synchronized void stopSimulation() {
        if (!running) return;{
            // If the simulation is not running, do nothing

            for (Thread thread : vendorThreads) {
                thread.interrupt();
            }
            for (Thread thread : customerThreads) {
                thread.interrupt();
            }

            vendorThreads.clear();
            customerThreads.clear();
            running = false;
        }
    }

    public synchronized int getTotalTickets() {
        return ticketPool != null? ticketPool.getTotalTickets() : 0;
    }

    public synchronized int getMaxTicketCapacity() {
        return ticketPool != null ? ticketPool.getMaxTicketCapacity() : 0;
    }

    public synchronized List<String> getLogs() {
        if (ticketPool != null) {
            return ticketPool.getLogs(); // Retrieve logs from the ticket pool
        }
        return List.of("No simulation running.");
    }
}

