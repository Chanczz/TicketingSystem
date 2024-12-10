# Ticket Management System with JavaFX

## Project Overview

This is a ticket management system that allows vendors to create and add tickets while customers can purchase them. The system features a graphical user interface built with JavaFX, replacing the original console-based interface with a modern, user-friendly window application.

## Understanding JavaFX Implementation

### What is JavaFX?

Think of JavaFX as a way to build a house. In our `TicketSystemApp` class (which extends `Application`), we're creating the blueprint for our application's visual structure. The `Stage` is like the plot of land - it's the main window where everything lives. Inside that, we put a `Scene`, which is like the foundation of our house.

Now, for organizing everything inside, we use layouts. Our main layout is `BorderPane` - imagine it like dividing a room into sections (top, bottom, left, right, and center). Inside this, we put a `TabPane`, which is like having different rooms you can switch between using tabs at the top. Each tab (`Tab`) is a separate room for different functions: configuration, login, registration, and system stats.

Here's how we create this structure:

```java
public void start(Stage primaryStage) {
    // Create the main window
    primaryStage.setTitle("Ticket System");

    // Create the main room layout
    BorderPane mainLayout = new BorderPane();

    // Create the tab system
    mainTabPane = new TabPane();

    // Create each "room" (tab)
    Tab configTab = new Tab("Configuration", createConfigurationPanel());
    Tab loginTab = new Tab("Login", createLoginPanel());
    // ... more tabs ...

    // Put all tabs in the tab pane
    mainTabPane.getTabs().addAll(configTab, loginTab, ...);

    // Put everything together
    mainLayout.setCenter(mainTabPane);
    Scene scene = new Scene(mainLayout, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
}
```

Inside each tab, we use `VBox` (vertical box) to stack elements from top to bottom. Think of it like stacking boxes in a closet. Each element is a JavaFX control:

```java
private VBox createLoginPanel() {
    // Create a vertical stack with 10 pixels spacing
    VBox loginBox = new VBox(10);
    loginBox.setPadding(new Insets(10));  // Add padding around edges

    // Create interactive elements
    Label titleLabel = new Label("Login");  // Just text
    TextField emailField = new TextField();  // Where users type
    emailField.setPromptText("Enter email here");  // Helper text

    Button loginButton = new Button("Login");  // Clickable button

    // When button is clicked, run our login code
    loginButton.setOnAction(event -> {
        // This is like a trigger - when clicked, do something
        handleLogin(emailField.getText());
    });

    // Stack everything in order
    loginBox.getChildren().addAll(titleLabel, emailField, loginButton);
    return loginBox;
}
```

Think of `Platform.runLater()` like a safe way to change the house's decorations while people are still using the house. It makes sure we don't interrupt anyone while updating what they see.

All these visual elements connect to our original ticket system through event handlers (like the button click example above). When someone interacts with a control, it triggers our backend code that handles the actual ticket logic.

This is how we created a user-friendly interface for our ticket system - we built a structured layout, filled it with interactive elements, and connected those elements to our existing ticket management code. The JavaFX framework handles all the complex parts of showing windows and processing clicks, letting us focus on making our ticket system work properly.

### Key JavaFX Components Used

1. **Layouts**

   - `BorderPane`: Main layout that divides space into top, bottom, left, right, center
   - `VBox`: Stacks elements vertically
   - `HBox`: Arranges elements horizontally
   - `TabPane`: Creates tabbed interface

2. **Controls**

   - `TextField`: For text input
   - `PasswordField`: For secure password input
   - `Button`: For clickable actions
   - `Label`: For displaying text
   - `TextArea`: For multi-line text display
   - `RadioButton`: For option selection
   - `Tab`: For creating tabbed sections

3. **Dialogs**
   - `Alert`: For showing messages and errors
   - Custom dialogs for specific interactions

### Threading in JavaFX

1. **JavaFX Application Thread**

   - All UI updates must happen on this thread
   - Use `Platform.runLater()` for safe UI updates from other threads

2. **Background Operations**
   - Ticket operations run on separate threads
   - UI stays responsive during long operations
   - Thread synchronization ensures data consistency

## System Features

- Real-time ticket management
- Multi-user support (Vendors and Customers)
- Thread-safe operations
- Activity logging
- Persistent storage (saves ticket data to file)

## Building and Running

### Prerequisites

- Java 17 or higher
- Maven
- JavaFX SDK

### Build Instructions

1. Clone the repository
2. Navigate to project directory
3. Run Maven commands:

```bash
mvn clean install
mvn javafx:run
```

## Implementation Details

### Threading Model

The system uses Java's threading capabilities to:

- Handle concurrent ticket operations
- Update UI without blocking
- Maintain data consistency

### Data Persistence

- Ticket data is saved to a file
- Activity logs are maintained in memory
- User data is managed during runtime

## User Interface Flow

1. **System Configuration**

   - Set maximum ticket capacity
   - Configure initial ticket count
   - Set event details and pricing

2. **User Registration/Login**

   - Choose user type (Vendor/Customer)
   - Enter credentials
   - Access respective functionalities

3. **Operations**
   - Vendors: Add tickets with delays
   - Customers: Purchase tickets with delays
   - Real-time status updates

## Technical Notes

- Uses JavaFX for GUI
- Maven for project management
- Modular design for easy maintenance
- Thread-safe operations
- Real-time update capabilities
