package com.ticketsystem;

public class Vendor implements Runnable {
    protected final TicketPool ticketPool;
    protected final int releaseRate;
    protected volatile boolean running;
    private String name;
    private String email;
    private String phoneNumber;
    private String password;

    public Vendor(TicketPool ticketPool, int releaseRate) {
        this(ticketPool, null, null, null, releaseRate, 0, null, 0);
    }

    public Vendor(TicketPool ticketPool, String name, String email, String password, 
                 int releaseRate, int maxTickets, String phoneNumber, int priority) {
        this.ticketPool = ticketPool;
        this.name = name;
        this.email = email;
        this.password = password;
        this.releaseRate = releaseRate;
        this.phoneNumber = phoneNumber;
        this.running = true;
    }

    public String getVendorName() {
        return name;
    }

    public String getVendorEmail() {
        return email;
    }

    public String getVendorPassword() {
        return password;
    }

    public String getVendorPhoneNumber() {
        return phoneNumber;
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
                Ticket ticket = new Ticket();
                ticketPool.addTicket(ticket);
                Thread.sleep(releaseRate);
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