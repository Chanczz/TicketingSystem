package com.ticketsystem;

public interface Menu {
    void registerVendor();
    void registerCustomer();
    void loginVendor();
    void loginCustomer();
    void systemStats();
    void activityLog();
    boolean menu();
} 