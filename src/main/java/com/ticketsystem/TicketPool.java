package com.ticketsystem;

import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

public class TicketPool {
    private final ConcurrentLinkedQueue<Ticket> tickets;
    private final TicketAnalytics analytics;
    private final TicketPersistence persistence;
    private int maxCapacity;
    private boolean showMessages;
    private final List<String> transactionHistory;
    private int ticketsSold;

    public TicketPool() {
        this.tickets = new ConcurrentLinkedQueue<>();
        this.analytics = new TicketAnalytics(5); // Update every 5 seconds
        this.persistence = new TicketPersistence();
        this.maxCapacity = 100; // Default capacity
        this.showMessages = true;
        this.transactionHistory = new ArrayList<>();
        this.ticketsSold = 0;
    }

    public void setMaxCapacity(int capacity) {
        this.maxCapacity = capacity;
    }

    public void setShowMessages(boolean show) {
        this.showMessages = show;
    }

    public synchronized void addTicket(Ticket ticket) {
        if (tickets.size() < maxCapacity) {
            tickets.offer(ticket);
            analytics.recordVendorActivity("Vendor", 1);
            if (showMessages) {
                System.out.println("Ticket added: " + ticket);
            }
            transactionHistory.add("Added ticket: " + ticket);
        }
    }

    public synchronized Ticket removeTicket(String customerName) {
        Ticket ticket = tickets.poll();
        if (ticket != null) {
            ticketsSold++;
            analytics.recordCustomerPurchase(customerName);
            if (showMessages) {
                System.out.println("Ticket removed by " + customerName + ": " + ticket);
            }
            transactionHistory.add("Removed ticket by " + customerName + ": " + ticket);
        }
        return ticket;
    }

    public int getTotalCount() {
        return tickets.size();
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public List<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public void saveTickets() {
        persistence.saveTickets(new ArrayList<>(tickets));
    }

    public void loadTickets() {
        List<Ticket> loadedTickets = persistence.loadTickets();
        tickets.clear();
        tickets.addAll(loadedTickets);
    }

    public TicketAnalytics getAnalytics() {
        return analytics;
    }

    public void shutdown() {
        saveTickets();
    }
} 