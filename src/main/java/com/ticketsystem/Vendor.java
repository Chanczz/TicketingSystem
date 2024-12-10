package com.ticketsystem;

import java.util.Scanner;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final String vendorName;
    private final String vendorEmail;
    private final String vendorPassword;
    private final int vendorPhoneNumber;
    private final int ticketDelay;
    private final String eventTitle;
    private final int ticketCost;
    private int numberOfTickets;

    public Vendor(TicketPool ticketPool, String vendorName, String vendorEmail, String vendorPassword, int vendorPhoneNumber, int ticketDelay, String eventTitle, int ticketCost) {
        this.ticketPool = ticketPool;
        this.vendorName = vendorName;
        this.vendorEmail = vendorEmail;
        this.vendorPassword = vendorPassword;
        this.vendorPhoneNumber = vendorPhoneNumber;
        this.ticketDelay = ticketDelay;
        this.eventTitle = eventTitle;
        this.ticketCost = ticketCost;
    }

    @Override
    public void run() {
        for (int i = 0; i < numberOfTickets; i++) {
            try {
                Ticket ticket = new Ticket(vendorPhoneNumber * 100 + i, eventTitle, ticketCost);
                ticketPool.addTicket(ticket, vendorName);
                Thread.sleep(ticketDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void login() {
        Scanner scanner = new Scanner(System.in);
        boolean isValidInput = false;

        while (!isValidInput) {
            int availableTicketsSpace = ticketPool.getTotalCount() - ticketPool.getCurrentCount();
            System.out.println("Tickets available for addition: " + availableTicketsSpace);

            try {
                System.out.print("Enter the number of tickets you'd like to add: ");
                numberOfTickets = scanner.nextInt();

                if (numberOfTickets <= 0) {
                    System.out.println("Please enter a positive number! Ticket count must be greater than 0.");
                } else if (numberOfTickets > availableTicketsSpace) {
                    System.out.println("Error: You cannot add more tickets than the available space. Please try again.");
                } else {
                    System.out.println("You have selected to add " + numberOfTickets + " tickets.");
                    isValidInput = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid input! Please enter a valid number for ticket count.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    public void logout() {
        ticketPool.setShowMessages(false);
    }

    public synchronized String getVendorName() {
        return vendorName;
    }

    public synchronized String getVendorEmail() {
        return vendorEmail;
    }

    public synchronized String getVendorPassword() {
        return vendorPassword;
    }

    public synchronized int getVendorPhoneNumber() {
        return vendorPhoneNumber;
    }
} 