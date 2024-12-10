package com.ticketsystem;

import java.util.Map;

public class TestSystem {
    public static void main(String[] args) {
        // Initialize the ticket pool
        TicketPool ticketPool = new TicketPool();
        ticketPool.setMaxCapacity(50);

        // Create vendors with different release rates
        Vendor fastVendor = new Vendor(ticketPool, 1000); // 1 second
        Vendor slowVendor = new Vendor(ticketPool, 2000); // 2 seconds

        // Create regular customers
        Customer regularCustomer1 = new Customer(ticketPool, 1500); // 1.5 seconds
        Customer regularCustomer2 = new Customer(ticketPool, 2000); // 2 seconds

        // Create VIP customers with different levels
        VIPCustomer vipCustomer1 = new VIPCustomer(ticketPool, 1000, 1); // Level 1
        VIPCustomer vipCustomer2 = new VIPCustomer(ticketPool, 1000, 3); // Level 3 (highest)

        // Start vendor threads
        Thread vendor1Thread = new Thread(fastVendor, "FastVendor");
        Thread vendor2Thread = new Thread(slowVendor, "SlowVendor");

        // Start customer threads
        Thread regular1Thread = new Thread(regularCustomer1, "RegularCustomer1");
        Thread regular2Thread = new Thread(regularCustomer2, "RegularCustomer2");
        Thread vip1Thread = new Thread(vipCustomer1, "VIPCustomer1");
        Thread vip2Thread = new Thread(vipCustomer2, "VIPCustomer3");

        System.out.println("Starting test scenario...");
        
        // Start all threads
        vendor1Thread.start();
        vendor2Thread.start();
        regular1Thread.start();
        regular2Thread.start();
        vip1Thread.start();
        vip2Thread.start();

        // Let the system run for 30 seconds
        try {
            Thread.sleep(30000);

            // Print analytics
            System.out.println("\n=== System Analytics ===");
            TicketAnalytics analytics = ticketPool.getAnalytics();
            
            System.out.println("\nVendor Statistics:");
            analytics.getVendorStats().forEach((vendor, count) -> 
                System.out.println(vendor + ": " + count + " tickets"));

            System.out.println("\nCustomer Statistics:");
            analytics.getCustomerStats().forEach((customer, count) -> 
                System.out.println(customer + ": " + count + " tickets"));

            System.out.println("\nPeak Statistics:");
            Map<String, Object> peakStats = analytics.getPeakStats();
            System.out.println("Peak Count: " + peakStats.get("count"));
            System.out.println("Peak Time: " + peakStats.get("time"));

            // Print transaction history
            System.out.println("\n=== Transaction History ===");
            ticketPool.getTransactionHistory().forEach(System.out::println);

            // Stop all threads
            System.out.println("\nStopping all threads...");
            fastVendor.stop();
            slowVendor.stop();
            regularCustomer1.stop();
            regularCustomer2.stop();
            vipCustomer1.stop();
            vipCustomer2.stop();

            // Wait for threads to finish
            vendor1Thread.join();
            vendor2Thread.join();
            regular1Thread.join();
            regular2Thread.join();
            vip1Thread.join();
            vip2Thread.join();

            // Clean up
            ticketPool.shutdown();
            System.out.println("Test completed successfully!");

        } catch (InterruptedException e) {
            System.err.println("Test interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
} 