# Ticket Management System

A robust, multi-threaded ticket management system built with Java, featuring both CLI and GUI interfaces, VIP customer support, analytics, and persistent storage. This project demonstrates advanced implementation of the Producer-Consumer pattern in a real-world application.

## Table of Contents

1. [System Overview](#system-overview)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Components](#components)
5. [Installation](#installation)
6. [Usage](#usage)
7. [Technical Details](#technical-details)
8. [JavaFX Implementation](#javafx-implementation)
9. [Multi-threading Details](#multi-threading-details)
10. [Testing](#testing)
11. [Learning Outcomes](#learning-outcomes)

## System Overview

The Ticket Management System is a comprehensive solution for managing ticket sales between vendors and customers. It supports concurrent operations, different user types, and provides both command-line and graphical user interfaces.

## Features

### Core Features

- Multi-threaded ticket processing
- Concurrent vendor and customer operations
- Real-time ticket availability tracking
- Thread-safe ticket pool management
- Persistent storage using SQLite
- Comprehensive transaction logging
- Real-time analytics

### User Types

- **Vendors**
  - Can add tickets to the pool
  - Configurable ticket release rates
  - Unique identification with email and phone
- **Regular Customers**
  - Can purchase tickets from the pool
  - Configurable ticket retrieval rates
  - Basic queue priority
- **VIP Customers**
  - Priority access to tickets
  - Three VIP levels (1-3)
  - Higher probability of successful purchases
  - Special identification in logs

### Interfaces

- Command Line Interface (CLI)
- Graphical User Interface (GUI) with JavaFX
- Both interfaces can run simultaneously

## Architecture

### Core Components

1. **Ticket Pool (`TicketPool.java`)**

   - Central ticket management
   - Thread-safe operations
   - Transaction logging
   - Analytics integration

2. **Users**

   - `Vendor.java`: Ticket suppliers
   - `Customer.java`: Basic ticket purchasers
   - `VIPCustomer.java`: Priority ticket purchasers

3. **Persistence (`TicketPersistence.java`)**

   - SQLite database integration
   - Transaction history
   - State persistence

4. **Analytics (`TicketAnalytics.java`)**
   - Real-time statistics
   - User activity tracking
   - Peak usage monitoring

### User Interfaces

1. **CLI (`TicketSystemCLI.java`)**

   - Text-based interface
   - Interactive menu system
   - Status monitoring

2. **GUI (`TicketSystemApp.java`)**
   - JavaFX-based interface
   - Real-time updates
   - Visual status display

## Components

### Ticket Pool

The `TicketPool` class is the core component managing ticket operations:

```java
public class TicketPool {
    private final ConcurrentLinkedQueue<Ticket> tickets;
    private final TicketAnalytics analytics;
    private final TicketPersistence persistence;
    // ...
}
```

- Uses `ConcurrentLinkedQueue` for thread-safe operations
- Integrates with analytics and persistence
- Manages ticket capacity and sales tracking

### Vendor

Vendors are ticket suppliers:

```java
public class Vendor implements Runnable {
    protected final TicketPool ticketPool;
    protected final int releaseRate;
    // ...
}
```

- Runs in separate thread
- Configurable ticket release rate
- Automatic ticket generation

### Customer

Customers purchase tickets:

```java
public class Customer implements Runnable {
    protected final TicketPool ticketPool;
    protected final int retrievalRate;
    // ...
}
```

- Runs in separate thread
- Configurable ticket retrieval rate
- Basic purchase priority

### VIP Customer

Extended customer with priority access:

```java
public class VIPCustomer extends Customer {
    private static final double VIP_PRIORITY_CHANCE = 0.8;
    private final int vipLevel; // 1-3
    // ...
}
```

- Three priority levels
- Higher purchase success rate
- Special identification in system

## Installation

1. **Prerequisites**

   - Java 17 or higher
   - Maven 3.6 or higher
   - SQLite (included via Maven)

2. **Build**

   ```bash
   mvn clean install
   ```

3. **Run CLI**

   ```bash
   mvn exec:java -Dexec.mainClass="com.ticketsystem.TicketSystemCLI"
   ```

4. **Run GUI**
   ```bash
   mvn exec:java -Dexec.mainClass="com.ticketsystem.TicketSystemApp"
   ```

## Usage

### CLI Interface

1. Start the system
2. Choose operation:
   - Add Vendor
   - Add Customer
   - Show System Status
   - Exit

### GUI Interface

1. Launch the application
2. Use the interface to:
   - Add/manage vendors
   - Add/manage customers
   - Monitor transactions
   - View analytics

### Testing

Run the test scenario:

```bash
mvn exec:java -Dexec.mainClass="com.ticketsystem.TestSystem"
```

## Technical Details

### Thread Safety

- Synchronized methods for critical sections
- Concurrent collections for shared resources
- Atomic operations for counters
- Volatile flags for thread control

### Database Schema

```sql
CREATE TABLE tickets (
    id INTEGER PRIMARY KEY,
    event_name TEXT NOT NULL,
    price REAL NOT NULL,
    priority INTEGER DEFAULT 0
)
```

### Analytics Tracking

- Vendor activity monitoring
- Customer purchase tracking
- Peak usage statistics
- Transaction history

## JavaFX Implementation

The GUI is implemented using JavaFX, following best practices for modern desktop applications:

### Scene Graph Structure

```java
public class TicketSystemApp extends Application {
    private VBox root;              // Root container
    private TabPane mainTabPane;    // Main content area
    private StatusBar statusBar;    // System status display
    // ...
}
```

### Key Components

1. **Main Window**

   - TabPane for multiple views
   - Status bar for system information
   - Real-time updates via Platform.runLater()

2. **Vendor Management Tab**

   - Add/remove vendor controls
   - Vendor status display
   - Ticket release rate configuration

3. **Customer Management Tab**

   - Customer registration form
   - VIP level selection
   - Purchase history view

4. **Analytics Dashboard**
   - Real-time charts and graphs
   - Transaction statistics
   - System performance metrics

### Threading Model

- UI updates handled on JavaFX Application Thread
- Background operations on separate threads
- Platform.runLater() for thread-safe UI updates
- Task and Service classes for long-running operations

## Multi-threading Details

### Producer-Consumer Implementation

1. **Producer (Vendor) Threading**

   ```java
   public class Vendor implements Runnable {
       @Override
       public void run() {
           while (!Thread.interrupted()) {
               // Generate and add tickets
               Thread.sleep(releaseRate);
           }
       }
   }
   ```

2. **Consumer (Customer) Threading**
   ```java
   public class Customer implements Runnable {
       @Override
       public void run() {
           while (!Thread.interrupted()) {
               // Attempt to purchase tickets
               Thread.sleep(retrievalRate);
           }
       }
   }
   ```

### Synchronization Mechanisms

1. **Thread-Safe Collections**

   - ConcurrentLinkedQueue for ticket pool
   - AtomicInteger for counters
   - Volatile flags for thread control

2. **Critical Section Protection**

   ```java
   public synchronized boolean addTickets(List<Ticket> newTickets) {
       // Thread-safe ticket addition logic
   }
   ```

3. **Deadlock Prevention**
   - Resource ordering
   - Timeout mechanisms
   - Lock hierarchy

## Learning Outcomes

This project demonstrates proficiency in:

1. **Advanced Programming Concepts**

   - Object-Oriented Programming principles
   - Design patterns (Producer-Consumer, Observer)
   - Multi-threading and concurrency

2. **Modern Technologies**

   - JavaFX for GUI development
   - SQLite for persistence
   - Maven for project management

3. **Professional Skills**

   - System architecture design
   - Code documentation
   - Testing and debugging

4. **Industry Relevance**
   - Real-world application simulation
   - Scalable system design
   - Performance optimization

These skills are highly valued in:

- Software Development roles
- System Architecture positions
- Technical Lead positions
- Quality Assurance roles

## Testing

The system includes a comprehensive test suite (`TestSystem.java`) that:

1. Creates multiple vendors and customers
2. Simulates concurrent operations
3. Verifies system stability
4. Validates analytics
5. Tests persistence

### Sample Test Scenario

```java
// Create vendors
Vendor fastVendor = new Vendor(ticketPool, 1000); // 1 second
Vendor slowVendor = new Vendor(ticketPool, 2000); // 2 seconds

// Create customers
Customer regularCustomer = new Customer(ticketPool, 1500);
VIPCustomer vipCustomer = new VIPCustomer(ticketPool, 1000, 3);

// Run concurrent operations
// Monitor results
// Verify system state
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to the branch
5. Create a Pull Request
