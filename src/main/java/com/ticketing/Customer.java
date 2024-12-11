package main.java.com.ticketing;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerId;
    private final long retrievalInterval;
    private volatile boolean running;
    private int ticketsPurchased;

    public Customer(String customerId, TicketPool ticketPool, long retrievalInterval) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.running = true;
        this.ticketsPurchased = 0;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (ticketPool.removeTicket()) {
                    ticketsPurchased++;
                    System.out.println("Customer " + customerId + " purchased a ticket. Total purchased: " + ticketsPurchased);
                } else {
                    System.out.println("Customer " + customerId + " couldn't purchase a ticket - no tickets available");
                }
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                stop();
            }
        }
    }

    public void stop() {
        running = false;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getTicketsPurchased() {
        return ticketsPurchased;
    }
} 