# Real-Time Event Ticketing System

A concurrent ticketing system that demonstrates the Producer-Consumer pattern using Java threading and JavaFX GUI.

## Features

1. Core Features:

   - Multi-threaded ticket management
   - Producer-Consumer pattern implementation
   - Thread-safe operations
   - Real-time monitoring
   - Both CLI and GUI interfaces

2. GUI Features:
   - Configuration panel for system parameters
   - Real-time monitoring of ticket availability
   - Customer purchase statistics
   - System control panel
   - Visual feedback and alerts

## Prerequisites

- Java Development Kit (JDK) 17 or later
- Apache Maven 3.6 or later
- JavaFX SDK 17 or later

## Setup Instructions

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd real-time-ticketing-system
   ```

2. Build the project:

   ```bash
   mvn clean package
   ```

3. Run the application:
   - For GUI version:
     ```bash
     mvn javafx:run
     ```
   - For CLI version:
     ```bash
     java -cp target/real-time-ticketing-system-1.0-SNAPSHOT.jar com.ticketing.TicketingSystem
     ```

## Usage Guide

### GUI Version

1. Configuration:

   - Enter the maximum ticket capacity
   - Specify the number of vendors and customers
   - Set ticket release and retrieval intervals
   - Click "Configure System" to initialize

2. Operation:
   - Use "Start System" to begin ticket operations
   - Monitor ticket availability and customer statistics in real-time
   - Use "Stop System" to halt operations
   - Close the window to exit

### CLI Version

1. Configuration:

   - Follow the prompts to enter system parameters
   - Enter maximum capacity
   - Specify vendor and customer counts
   - Set intervals for ticket operations

2. Commands:
   - Enter 1 to start the system
   - Enter 2 to stop the system
   - Enter 3 to exit

## System Architecture

1. Core Components:

   - `TicketPool`: Thread-safe ticket management
   - `Vendor`: Producer implementation
   - `Customer`: Consumer implementation
   - `TicketingSystem`: Core system logic
   - `TicketingSystemGUI`: JavaFX GUI implementation

2. Threading Model:
   - Uses Java's built-in threading
   - Synchronized operations
   - Atomic variables for thread safety
   - ReentrantLock for complex operations

## Development

### Project Structure

```
src/main/java/com/ticketing/
├── TicketPool.java
├── Vendor.java
├── Customer.java
├── TicketingSystem.java
└��─ TicketingSystemGUI.java
```

### Building from Source

1. Clean and compile:

   ```bash
   mvn clean compile
   ```

2. Run tests:

   ```bash
   mvn test
   ```

3. Create executable JAR:
   ```bash
   mvn package
   ```

## Troubleshooting

1. JavaFX Issues:

   - Ensure JavaFX SDK is properly installed
   - Verify module-info.java configuration
   - Check JAVA_HOME environment variable

2. Common Problems:
   - If GUI doesn't start, verify JavaFX dependencies
   - For thread issues, check thread dump using jstack
   - Memory issues: adjust JVM heap size with -Xmx flag

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
