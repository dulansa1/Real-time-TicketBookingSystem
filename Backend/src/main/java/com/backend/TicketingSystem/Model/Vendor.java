package com.backend.TicketingSystem.Model;

public class Vendor implements Runnable {
    private final TicketPool pool;
    private final int ticketReleaseRate;

    public Vendor(TicketPool pool, int ticketReleaseRate) {
        this.pool = pool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    // Add tickets and retrieve IDs
                    var addedTicketIds = pool.addTickets(ticketReleaseRate);
                    Thread.sleep(1000); // Simulate time delay for adding tickets
                    pool.addTickets(ticketReleaseRate);
                }
            } catch (InterruptedException e) {
                System.out.println("Vendor interrupted: " + Thread.currentThread().getName());
                break;
            }
        }
    }
}
