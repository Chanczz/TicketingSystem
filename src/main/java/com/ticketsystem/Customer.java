package com.ticketsystem;

public class Customer implements Runnable {
    protected final TicketPool ticketPool;
    protected final int retrievalRate;
    protected volatile boolean running;
    private String name;
    private String email;
    private String password;

    public Customer(TicketPool ticketPool, int retrievalRate) {
        this(null, null, null, ticketPool, retrievalRate);
    }

    public Customer(String name, String email, String password, TicketPool ticketPool, int retrievalRate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.running = true;
    }

    public String getCustomerName() {
        return name;
    }

    public String getCustomerEmail() {
        return email;
    }

    public String getCustomerPassword() {
        return password;
    }

    public void login() {
        running = true;
    }

    public void logout() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Ticket ticket = ticketPool.removeTicket(name != null ? name : "Anonymous");
                if (ticket != null) {
                    System.out.println(name + " got ticket: " + ticket);
                }
                Thread.sleep(retrievalRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }
} 