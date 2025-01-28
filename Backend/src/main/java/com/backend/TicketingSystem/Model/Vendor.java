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
                Thread.sleep(1000); // Delay for ticket release
                pool.addTickets(ticketReleaseRate);
            } catch (InterruptedException e) {
                System.out.println("Vendor interrupted: " + Thread.currentThread().getName());
                break;
            }
        }
    }
}
