package com.ticketsystem;

import java.util.Scanner;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerName;
    private final String customerEmail;
    private final String customerPassword;
    private int ticketsToPurchase;
    private final int purchaseDelay;

    public Customer(String customerName, String customerEmail, String customerPassword, TicketPool ticketPool, int purchaseDelay) {
        this.ticketPool = ticketPool;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPassword = customerPassword;
        this.purchaseDelay = purchaseDelay;
    }

    @Override
    public void run() {
        for (int i = 0; i < ticketsToPurchase; i++) {
            try {
                Ticket ticket = ticketPool.purchaseTicket(customerName);
                Thread.sleep(purchaseDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;

        // Show the first ticket details if available
        Ticket firstTicket = ticketPool.getFirstTicket();
        if (firstTicket != null) {
            System.out.println("\n===========================");
            System.out.println("First available ticket: ");
            System.out.println("Ticket ID: " + firstTicket.getId());
            System.out.println("Event: " + firstTicket.getEventName());
            System.out.println("Price: LKR " + firstTicket.getPrice());
            System.out.println("===========================\n");
        } else {
            System.out.println("No tickets available for purchase.");
        }

        while (!isValidInput) {
            int availableTickets = ticketPool.getCurrentCount() - ticketPool.getTicketsSold();
            System.out.println("Tickets remaining for purchase: " + availableTickets);

            try {
                System.out.print("Enter the number of tickets you want to buy: ");
                ticketsToPurchase = scanner.nextInt();

                if (ticketsToPurchase <= 0) {
                    System.out.println("Invalid input! Please enter a positive number for ticket count.");
                } else if (ticketsToPurchase > availableTickets) {
                    System.out.println("Error: Not enough tickets available. Please enter a valid ticket count.");
                } else {
                    System.out.println("You selected to purchase " + ticketsToPurchase + " tickets.");
                    isValidInput = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    public void logout() {
        ticketPool.setShowMessages(false);
    }

    public synchronized String getCustomerName() {
        return customerName;
    }

    public synchronized String getCustomerEmail() {
        return customerEmail;
    }

    public synchronized String getCustomerPassword() {
        return customerPassword;
    }
} 