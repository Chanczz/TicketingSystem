package com.ticketsystem;

public class Ticket {
    private static int nextId = 1;
    private final int id;
    private final String eventName;
    private final double price;
    private int priority;  // 0 for regular tickets, 1-3 for VIP levels

    public Ticket() {
        this(nextId++, "General Event", 100.0, 0);
    }

    public Ticket(int id, String eventName, double price) {
        this(id, eventName, price, 0);
    }

    public Ticket(int id, String eventName, double price, int priority) {
        this.id = id;
        this.eventName = eventName;
        this.price = price;
        this.priority = Math.min(Math.max(priority, 0), 3); // Ensure priority is between 0-3
    }

    public int getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public double getPrice() {
        return price;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = Math.min(Math.max(priority, 0), 3);
    }

    @Override
    public String toString() {
        return String.format("Ticket{id=%d, event='%s', price=%.2f, priority=%d}", 
            id, eventName, price, priority);
    }
} 