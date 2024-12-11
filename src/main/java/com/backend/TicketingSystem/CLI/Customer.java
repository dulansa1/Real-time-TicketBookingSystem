package com.backend.TicketingSystem.CLI;
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
                Thread.sleep(2000); // Delay for ticket purchase
                pool.removeTicket(ticketDemand);
            } catch (InterruptedException e) {
                System.out.println("Customer interrupted: " + Thread.currentThread().getName());
                break;
            }
        }
    }
}
