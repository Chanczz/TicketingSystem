package com.ticketing;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicketingSystem {
    private final TicketPool ticketPool;
    private final List<Thread> vendorThreads;
    private final List<Thread> customerThreads;
    private final List<Vendor> vendors;
    private final List<Customer> customers;
    private boolean isRunning;

    public TicketingSystem(int maxCapacity) {
        this.ticketPool = new TicketPool(maxCapacity);
        this.vendorThreads = new ArrayList<>();
        this.customerThreads = new ArrayList<>();
        this.vendors = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.isRunning = false;
    }

    public void initialize(int numVendors, int numCustomers, long releaseInterval, 
                         long retrievalInterval, int ticketsPerRelease) {
        // Create vendor threads
        for (int i = 0; i < numVendors; i++) {
            Vendor vendor = new Vendor("V" + (i + 1), ticketPool, ticketsPerRelease, releaseInterval);
            vendors.add(vendor);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
        }

        // Create customer threads
        for (int i = 0; i < numCustomers; i++) {
            Customer customer = new Customer("C" + (i + 1), ticketPool, retrievalInterval);
            customers.add(customer);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
        }
    }

    public void initialize() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to the Real-Time Event Ticketing System!");
        System.out.println("Please configure the system parameters:");
        
        System.out.print("Enter number of vendors: ");
        int numVendors = scanner.nextInt();
        
        System.out.print("Enter number of customers: ");
        int numCustomers = scanner.nextInt();
        
        System.out.print("Enter ticket release interval (in milliseconds): ");
        long releaseInterval = scanner.nextLong();
        
        System.out.print("Enter ticket retrieval interval (in milliseconds): ");
        long retrievalInterval = scanner.nextLong();
        
        System.out.print("Enter tickets per release for vendors: ");
        int ticketsPerRelease = scanner.nextInt();

        initialize(numVendors, numCustomers, releaseInterval, retrievalInterval, ticketsPerRelease);
    }

    public void start() {
        if (isRunning) {
            System.out.println("System is already running!");
            return;
        }

        isRunning = true;
        System.out.println("\nStarting the Ticketing System...");

        // Start all vendor threads
        for (Thread vendorThread : vendorThreads) {
            vendorThread.start();
        }

        // Start all customer threads
        for (Thread customerThread : customerThreads) {
            customerThread.start();
        }
    }

    public void stop() {
        if (!isRunning) {
            System.out.println("System is not running!");
            return;
        }

        isRunning = false;
        System.out.println("\nStopping the Ticketing System...");

        // Stop all vendors
        for (Vendor vendor : vendors) {
            vendor.stop();
        }

        // Stop all customers
        for (Customer customer : customers) {
            customer.stop();
        }

        // Wait for all threads to complete
        try {
            for (Thread thread : vendorThreads) {
                thread.join();
            }
            for (Thread thread : customerThreads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted while stopping threads");
        }

        System.out.println("System stopped successfully!");
    }

    public TicketPool getTicketPool() {
        return ticketPool;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public static void main(String[] args) {
        TicketingSystem system = new TicketingSystem(100);
        system.initialize();
        system.start();

        // Run for a while
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        system.stop();
    }
} 