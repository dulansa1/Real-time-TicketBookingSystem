package com.backend.TicketingSystem.Model;

public class Customer implements Runnable {
    private final TicketPool pool;
    private final int ticketDemand;

    public Customer(TicketPool pool, int ticketDemand) {
        this.pool = pool;
        this.ticketDemand = ticketDemand;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Customers try to buy tickets at intervals
                Thread.sleep(2000); // Simulate delay between ticket purchases
                pool.removeTicket(ticketDemand);
            } catch (InterruptedException e) {
                System.out.println("Customer interrupted: " + Thread.currentThread().getName());
                break;
            }
        }
    }
}

