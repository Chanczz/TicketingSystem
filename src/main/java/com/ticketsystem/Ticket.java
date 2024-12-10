package com.ticketsystem;

public class Ticket {
    private int id;
    private String eventName;
    private int price;

    public Ticket(int id, String eventName, int price) {
        this.id = id;
        this.eventName = eventName;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
} 