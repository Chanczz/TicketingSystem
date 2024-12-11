package main.java.com.ticketing;

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

        // Start monitoring thread for CLI
        if (!isGUI()) {
            new Thread(this::monitorSystem).start();
        }
    }

    public void stop() {
        if (!isRunning) {
            System.out.println("System is not running!");
            return;
        }

        isRunning = false;
        System.out.println("\nStopping the Ticketing System...");

        // Stop all vendors and customers
        for (Vendor vendor : vendors) {
            vendor.stop();
        }
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
            System.out.println("Shutdown interrupted");
            Thread.currentThread().interrupt();
        }

        // Print final statistics
        printStatistics();
    }

    private void monitorSystem() {
        while (isRunning) {
            try {
                Thread.sleep(5000); // Update every 5 seconds
                printStatistics();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void printStatistics() {
        System.out.println("\n=== System Statistics ===");
        System.out.println("Available Tickets: " + ticketPool.getAvailableTickets());
        System.out.println("Maximum Capacity: " + ticketPool.getMaxTicketCapacity());
        
        System.out.println("\nCustomer Statistics:");
        for (Customer customer : customers) {
            System.out.println(customer.getCustomerId() + " purchased: " + customer.getTicketsPurchased() + " tickets");
        }
        System.out.println("=====================\n");
    }

    public int getAvailableTickets() {
        return ticketPool.getAvailableTickets();
    }

    public int getMaxCapacity() {
        return ticketPool.getMaxTicketCapacity();
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    private boolean isGUI() {
        return Thread.currentThread().getStackTrace()[3].getClassName().contains("GUI");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter maximum ticket capacity: ");
        int maxCapacity = scanner.nextInt();
        
        TicketingSystem system = new TicketingSystem(maxCapacity);
        system.initialize();
        
        boolean running = true;

        while (running) {
            System.out.println("\nAvailable commands:");
            System.out.println("1. Start system");
            System.out.println("2. Stop system");
            System.out.println("3. Exit");
            System.out.print("Enter command (1-3): ");

            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    system.start();
                    break;
                case 2:
                    system.stop();
                    break;
                case 3:
                    system.stop();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command!");
            }
        }

        System.out.println("Thank you for using the Real-Time Event Ticketing System!");
        scanner.close();
    }
} 