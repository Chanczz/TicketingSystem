package com.ticketsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenu implements Menu {
    private List<Vendor> vendorList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();
    private TicketPool ticketPool;
    private boolean isVendorLoggedIn = false;
    private boolean isCustomerLoggedIn = false;
    private Vendor currentVendor = null;
    private Customer currentCustomer = null;
    private final int addTicketDelay;
    private final int purchaseTicketDelay;
    private final String event;
    private final int price;

    public MainMenu(TicketPool ticketPool, int addTicketDelay, int purchaseTicketDelay, String event, int price) {
        this.ticketPool = ticketPool;
        this.addTicketDelay = addTicketDelay;
        this.purchaseTicketDelay = purchaseTicketDelay;
        this.event = event;
        this.price = price;
    }

    @Override
    public void registerVendor() {
        Scanner scanner = new Scanner(System.in);
        String email, name, password;
        int phoneNumber;

        try {
            System.out.print("Enter your phone number: ");
            phoneNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline left by nextInt()
        } catch (Exception e) {
            System.out.println("Invalid phone number. Please enter a valid number.");
            return;
        }

        try {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();

            if (!email.contains("@") || !email.contains(".")) {
                System.out.println("Invalid email. Please provide a valid email address.");
                return;
            }

            for (Vendor vendor : vendorList) {
                if (vendor.getVendorEmail().equals(email)) {
                    System.out.println("This email is already taken. Try another one.");
                    return;
                }
                if (vendor.getVendorPhoneNumber() == phoneNumber) {
                    System.out.println("This phone number is already in use. Try a different one.");
                    return;
                }
            }

        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        try {
            System.out.print("Enter your name: ");
            name = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        Vendor vendor = new Vendor(ticketPool, name, email, password, phoneNumber, addTicketDelay, event, price);
        vendorList.add(vendor);
        System.out.println("Vendor successfully registered.");
    }

    @Override
    public void registerCustomer() {
        Scanner scanner = new Scanner(System.in);
        String email, name, password;

        try {
            System.out.print("Enter your email: ");
            email = scanner.nextLine();

            if (!email.contains("@") || !email.contains(".")) {
                System.out.println("Invalid email. Please provide a valid email address.");
                return;
            }

            for (Customer customer : customerList) {
                if (customer.getCustomerEmail().equals(email)) {
                    System.out.println("This email is already taken. Try another one.");
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        try {
            System.out.print("Enter your name: ");
            name = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        Customer customer = new Customer(name, email, password, ticketPool, purchaseTicketDelay);
        customerList.add(customer);
        System.out.println("Customer successfully registered.");
    }

    @Override
    public void loginVendor() {
        Scanner scanner = new Scanner(System.in);
        String input, password;

        try {
            System.out.print("Enter your phone number or email: ");
            input = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        boolean loggedIn = false;
        for (Vendor vendor : vendorList) {
            if ((String.valueOf(vendor.getVendorPhoneNumber()).equals(input) || vendor.getVendorEmail().equals(input)) &&
                    vendor.getVendorPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + vendor.getVendorName() + "!");
                currentVendor = vendor;
                ticketPool.setShowMessages(true);
                vendor.login();
                Thread vendorThread = new Thread(vendor);
                vendorThread.start();
                isVendorLoggedIn = true;
                loggedIn = true;
                break;
            }
        }

        if (!loggedIn) {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    @Override
    public void loginCustomer() {
        Scanner scanner = new Scanner(System.in);
        String input, password;

        try {
            System.out.print("Enter your email: ");
            input = scanner.nextLine();
            System.out.print("Enter your password: ");
            password = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again.");
            return;
        }

        boolean loggedIn = false;

        for (Customer customer : customerList) {
            if (customer.getCustomerEmail().equals(input) && customer.getCustomerPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + customer.getCustomerName() + "!");
                currentCustomer = customer;
                ticketPool.setShowMessages(true);
                customer.login();
                Thread customerThread = new Thread(customer);
                customerThread.start();
                isCustomerLoggedIn = true;
                loggedIn = true;
                break;
            }
        }

        if (!loggedIn) {
            System.out.println("Invalid credentials. Please try again.");
        }
    }

    @Override
    public void systemStats() {
        System.out.println("Total Ticket Capacity: " + ticketPool.getTotalCount());
        System.out.println("Tickets Available: " + (ticketPool.getCurrentCount() - ticketPool.getTicketsSold()));
        System.out.println("Remaining Capacity: " + (ticketPool.getTotalCount() - ticketPool.getCurrentCount()));
        System.out.println("Number of Vendors: " + vendorList.size());
        System.out.println("Number of Customers: " + customerList.size());
    }

    @Override
    public void activityLog() {
        for (String log : ticketPool.getActivityLogs()) {
            System.out.println(log);
        }
    }

    @Override
    public boolean menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (isVendorLoggedIn || isCustomerLoggedIn) {
                System.out.println("You are logged in. Press any key to log out.");
                scanner.nextLine();

                if (isVendorLoggedIn) {
                    currentVendor.logout();
                    ticketPool.setShowMessages(false);
                    isVendorLoggedIn = false;
                    currentVendor = null;
                }
                if (isCustomerLoggedIn) {
                    currentCustomer.logout();
                    ticketPool.setShowMessages(false);
                    isCustomerLoggedIn = false;
                    currentCustomer = null;
                }

                System.out.println("Logged out successfully.");
                continue;
            }

            System.out.println("============ Main Menu ==============");
            System.out.println("1. Register as a Vendor");
            System.out.println("2. Register as a Customer");
            System.out.println("3. Vendor Login");
            System.out.println("4. Customer Login");
            System.out.println("5. View System Information");
            System.out.println("6. View Activity Log");
            System.out.println("7. Exit");
            System.out.println("=====================================");
            int choice = -1;

            try {
                System.out.print("Please select an option: ");
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    registerVendor();
                    break;
                case 2:
                    registerCustomer();
                    break;
                case 3:
                    loginVendor();
                    break;
                case 4:
                    loginCustomer();
                    break;
                case 5:
                    systemStats();
                    break;
                case 6:
                    activityLog();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    ticketPool.saveTickets();
                    return false; // Exit the menu
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
} 