package com.ticketing;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final String vendorId;
    private final int ticketsPerRelease;
    private final long releaseInterval;
    private volatile boolean running;

    public Vendor(String vendorId, TicketPool ticketPool, int ticketsPerRelease, long releaseInterval) {
        this.vendorId = vendorId;
        this.ticketPool = ticketPool;
        this.ticketsPerRelease = ticketsPerRelease;
        this.releaseInterval = releaseInterval;
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (ticketPool.addTickets(ticketsPerRelease)) {
                    System.out.println("Vendor " + vendorId + " released " + ticketsPerRelease + " tickets");
                } else {
                    System.out.println("Vendor " + vendorId + " couldn't release tickets - pool is full");
                }
                Thread.sleep(releaseInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                stop();
            }
        }
    }

    public void stop() {
        running = false;
    }

    public String getVendorId() {
        return vendorId;
    }
} 