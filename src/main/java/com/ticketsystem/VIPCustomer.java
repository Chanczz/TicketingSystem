package com.ticketsystem;

public class VIPCustomer extends Customer {
    private static final double VIP_PRIORITY_CHANCE = 0.8; // 80% chance to get priority
    private final int vipLevel; // 1-3, higher means more priority

    public VIPCustomer(TicketPool ticketPool, int retrievalRate, int vipLevel) {
        super(ticketPool, retrievalRate);
        this.vipLevel = Math.min(Math.max(vipLevel, 1), 3); // Ensure VIP level is between 1-3
    }

    public VIPCustomer(String name, String email, String password, TicketPool ticketPool, int retrievalRate, int vipLevel) {
        super(name, email, password, ticketPool, retrievalRate);
        this.vipLevel = Math.min(Math.max(vipLevel, 1), 3);
    }

    @Override
    public void run() {
        while (running) {
            try {
                String customerName = getCustomerName() != null ? getCustomerName() : "Anonymous";
                if (Math.random() < (VIP_PRIORITY_CHANCE * (vipLevel / 3.0))) {
                    customerName = "VIP-" + customerName;
                }
                
                Ticket ticket = ticketPool.removeTicket(customerName);
                if (ticket != null) {
                    System.out.println(customerName + " got ticket: " + ticket);
                }
                Thread.sleep(retrievalRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public int getVipLevel() {
        return vipLevel;
    }
} 