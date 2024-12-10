package com.ticketsystem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

public class TicketPool {
    private final int totalCount;
    private int currentCount;
    private final Queue<Ticket> tickets = new LinkedList<>();
    private int currentId = 1;
    private volatile boolean showMessages = true;
    private final List<String> activityLogs = new ArrayList<>();
    private int ticketsSold;

    public TicketPool(int totalCount) {
        this.totalCount = totalCount;
        this.currentCount = 0;
        this.ticketsSold = 0;
    }

    public void setShowMessages(boolean showMessages) {
        this.showMessages = showMessages;
    }

    public List<String> getActivityLogs() {
        return new ArrayList<>(activityLogs);
    }

    public synchronized void addTicket(Ticket ticket, String vendorName) {
        try {
            while (currentCount >= totalCount) {
                wait();
            }
            tickets.offer(ticket);
            currentCount++;

            if (showMessages) {
                System.out.println("Ticket added: " + ticket.getEventName() + " | Current Count: " + currentCount);
                System.out.println("Vendor: " + vendorName + " added ticket ID: " + ticket.getId());
            }

            activityLogs.add("Ticket added: " + ticket.getEventName() + " | Current Count: " + currentCount);
            activityLogs.add("Vendor: " + vendorName + " added ticket ID: " + ticket.getId());

        } catch (InterruptedException e) {
            System.out.println("Error: The ticket addition process was interrupted.");
        }
    }

    public synchronized Ticket purchaseTicket(String customerName) {
        Ticket ticket = null;
        try {
            while (currentCount == 0) {
                wait();
            }
            ticket = tickets.poll();
            ticketsSold++;

            if (showMessages && ticket != null) {
                System.out.println("Ticket purchased: " + ticket.getEventName() + " | Current Count: " + currentCount);
                System.out.println("Customer: " + customerName + " purchased ticket ID: " + ticket.getId());
            }

            if (ticket != null) {
                activityLogs.add("Ticket purchased: " + ticket.getEventName() + " | Current Count: " + currentCount);
                activityLogs.add("Customer: " + customerName + " purchased ticket ID: " + ticket.getId());
            }
        } catch (InterruptedException e) {
            System.out.println("Error: The ticket purchasing process was interrupted.");
        }
        return ticket;
    }

    public synchronized int getCurrentCount() {
        return currentCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }

    public synchronized Ticket getFirstTicket() {
        return tickets.peek();
    }

    public void createTicket(int count, String eventName, int price) {
        try {
            for (int i = 0; i < count; i++) {
                Ticket ticket = new Ticket(currentId++, eventName, price);
                currentCount++;
                tickets.add(ticket);
            }
        } catch (Exception e) {
            System.out.println("Error: Failed to create tickets due to an unknown error.");
        }
    }

    public void saveTickets() {
        String fileName = "tickets.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Ticket ticket : tickets) {
                String ticketData = ticket.getId() + "," + ticket.getEventName() + "," + ticket.getPrice();
                writer.write(ticketData);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error: An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
} 