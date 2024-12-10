package com.ticketsystem;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MainMenu {
    private final TicketPool ticketPool;
    private final int vendorDelay;
    private final int customerDelay;
    private Vendor currentVendor;
    private Customer currentCustomer;

    public MainMenu(TicketPool ticketPool, int vendorDelay, int customerDelay) {
        this.ticketPool = ticketPool;
        this.vendorDelay = vendorDelay;
        this.customerDelay = customerDelay;
    }

    public void loginVendor(String email, String phone, String password) {
        // Find vendor by email and phone
        if (currentVendor != null) {
            showError("A vendor is already logged in");
            return;
        }

        // Create a new vendor for testing
        currentVendor = new Vendor(ticketPool, vendorDelay);
        currentVendor.login();
        showInfo("Vendor logged in successfully");
    }

    public void loginCustomer(String email, String password) {
        // Find customer by email
        if (currentCustomer != null) {
            showError("A customer is already logged in");
            return;
        }

        // Create a new customer for testing
        currentCustomer = new Customer(ticketPool, customerDelay);
        currentCustomer.login();
        showInfo("Customer logged in successfully");
    }

    public void logoutVendor() {
        if (currentVendor != null) {
            currentVendor.logout();
            currentVendor = null;
            showInfo("Vendor logged out successfully");
        }
    }

    public void logoutCustomer() {
        if (currentCustomer != null) {
            currentCustomer.logout();
            currentCustomer = null;
            showInfo("Customer logged out successfully");
        }
    }

    public String getSystemStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== System Status ===\n");
        status.append("Available Tickets: ").append(ticketPool.getTotalCount()).append("\n");
        status.append("Tickets Sold: ").append(ticketPool.getTicketsSold()).append("\n");
        return status.toString();
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 