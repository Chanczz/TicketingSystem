package com.ticketsystem;

import java.util.Scanner;

public class TicketSystemCLI {
    private final TicketPool ticketPool;
    private final Scanner scanner;
    private Vendor currentVendor;
    private Customer currentCustomer;

    public TicketSystemCLI() {
        this.ticketPool = new TicketPool();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Ticket System!");
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getChoice();

            switch (choice) {
                case 1 -> addVendor();
                case 2 -> addCustomer();
                case 3 -> showSystemStatus();
                case 4 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        cleanup();
    }

    private void displayMenu() {
        System.out.println("\n=== Ticket System Menu ===");
        System.out.println("1. Add Vendor");
        System.out.println("2. Add Customer");
        System.out.println("3. Show System Status");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private int getChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void addVendor() {
        System.out.print("Enter vendor name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter release rate (ms): ");
        int releaseRate;
        try {
            releaseRate = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid release rate. Using default of 1000ms.");
            releaseRate = 1000;
        }

        Vendor vendor = new Vendor(ticketPool, releaseRate);
        Thread vendorThread = new Thread(vendor, name);
        vendorThread.start();
        
        System.out.println("Vendor " + name + " added successfully!");
    }

    private void addCustomer() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        
        System.out.print("Is this a VIP customer? (y/n): ");
        boolean isVip = scanner.nextLine().toLowerCase().startsWith("y");
        
        System.out.print("Enter retrieval rate (ms): ");
        int retrievalRate;
        try {
            retrievalRate = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid retrieval rate. Using default of 1500ms.");
            retrievalRate = 1500;
        }

        Customer customer;
        if (isVip) {
            System.out.print("Enter VIP level (1-3): ");
            int vipLevel;
            try {
                vipLevel = Integer.parseInt(scanner.nextLine());
                vipLevel = Math.min(Math.max(vipLevel, 1), 3);
            } catch (NumberFormatException e) {
                System.out.println("Invalid VIP level. Using level 1.");
                vipLevel = 1;
            }
            customer = new VIPCustomer(ticketPool, retrievalRate, vipLevel);
        } else {
            customer = new Customer(ticketPool, retrievalRate);
        }

        Thread customerThread = new Thread(customer, name);
        customerThread.start();
        
        System.out.println("Customer " + name + (isVip ? " (VIP)" : "") + " added successfully!");
    }

    private void showSystemStatus() {
        System.out.println("\n=== System Status ===");
        System.out.println("Available Tickets: " + ticketPool.getTotalCount());
        System.out.println("Tickets Sold: " + ticketPool.getTicketsSold());
    }

    private void cleanup() {
        System.out.println("\nShutting down the system...");
        ticketPool.shutdown();
        scanner.close();
        System.out.println("Goodbye!");
    }

    public static void main(String[] args) {
        new TicketSystemCLI().start();
    }
} 