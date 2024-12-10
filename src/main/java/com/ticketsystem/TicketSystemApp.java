package com.ticketsystem;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;

public class TicketSystemApp extends Application {
    private TicketPool ticketPool;
    private MainMenu mainMenu;
    
    // UI Components
    private TabPane mainTabPane;
    private TextField maxTicketCapacityField;
    private TextField currentTicketCountField;
    private TextField vendorDelayField;
    private TextField customerDelayField;
    private TextField eventNameField;
    private TextField ticketPriceField;
    private TextArea activityLogArea;
    private Label statusLabel;
    private VBox loginPane;
    private VBox registerPane;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ticket System");

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        
        // Initialize tabs
        mainTabPane = new TabPane();
        
        // Create tabs
        Tab configTab = new Tab("Configuration", createConfigurationPanel());
        Tab loginTab = new Tab("Login", createLoginPanel());
        Tab registerTab = new Tab("Register", createRegistrationPanel());
        Tab statsTab = new Tab("System Stats", createStatsPanel());
        
        mainTabPane.getTabs().addAll(configTab, loginTab, registerTab, statsTab);
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        mainLayout.setCenter(mainTabPane);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createConfigurationPanel() {
        VBox configPanel = new VBox(10);
        configPanel.setPadding(new Insets(10));
        configPanel.setStyle("-fx-background-color: #f0f0f0;");

        Label titleLabel = new Label("System Configuration");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        maxTicketCapacityField = new TextField();
        maxTicketCapacityField.setPromptText("Maximum Ticket Capacity");

        currentTicketCountField = new TextField();
        currentTicketCountField.setPromptText("Current Ticket Count");

        vendorDelayField = new TextField();
        vendorDelayField.setPromptText("Vendor Delay (ms)");

        customerDelayField = new TextField();
        customerDelayField.setPromptText("Customer Delay (ms)");

        eventNameField = new TextField();
        eventNameField.setPromptText("Event Name");

        ticketPriceField = new TextField();
        ticketPriceField.setPromptText("Ticket Price");

        Button applyButton = new Button("Apply Configuration");
        applyButton.setOnAction(e -> applyConfiguration());

        configPanel.getChildren().addAll(
            titleLabel,
            new Label("Maximum Ticket Capacity:"),
            maxTicketCapacityField,
            new Label("Current Ticket Count:"),
            currentTicketCountField,
            new Label("Vendor Delay (ms):"),
            vendorDelayField,
            new Label("Customer Delay (ms):"),
            customerDelayField,
            new Label("Event Name:"),
            eventNameField,
            new Label("Ticket Price:"),
            ticketPriceField,
            applyButton
        );

        return configPanel;
    }

    private VBox createLoginPanel() {
        loginPane = new VBox(10);
        loginPane.setPadding(new Insets(10));

        Label titleLabel = new Label("Login");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ToggleGroup userTypeGroup = new ToggleGroup();
        RadioButton vendorRadio = new RadioButton("Vendor");
        RadioButton customerRadio = new RadioButton("Customer");
        vendorRadio.setToggleGroup(userTypeGroup);
        customerRadio.setToggleGroup(userTypeGroup);
        vendorRadio.setSelected(true);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone (Vendors only)");
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin(
            vendorRadio.isSelected(),
            emailField.getText(),
            phoneField.getText(),
            passwordField.getText()
        ));

        loginPane.getChildren().addAll(
            titleLabel,
            new HBox(10, vendorRadio, customerRadio),
            emailField,
            phoneField,
            passwordField,
            loginButton
        );

        return loginPane;
    }

    private VBox createRegistrationPanel() {
        registerPane = new VBox(10);
        registerPane.setPadding(new Insets(10));

        Label titleLabel = new Label("Register");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ToggleGroup userTypeGroup = new ToggleGroup();
        RadioButton vendorRadio = new RadioButton("Vendor");
        RadioButton customerRadio = new RadioButton("Customer");
        vendorRadio.setToggleGroup(userTypeGroup);
        customerRadio.setToggleGroup(userTypeGroup);
        vendorRadio.setSelected(true);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone (Vendors only)");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerButton = new Button("Register");
        registerButton.setOnAction(e -> handleRegistration(
            vendorRadio.isSelected(),
            nameField.getText(),
            emailField.getText(),
            phoneField.getText(),
            passwordField.getText()
        ));

        registerPane.getChildren().addAll(
            titleLabel,
            new HBox(10, vendorRadio, customerRadio),
            nameField,
            emailField,
            phoneField,
            passwordField,
            registerButton
        );

        return registerPane;
    }

    private VBox createStatsPanel() {
        VBox statsPanel = new VBox(10);
        statsPanel.setPadding(new Insets(10));

        Label titleLabel = new Label("System Statistics");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        statusLabel = new Label("System not configured");
        
        activityLogArea = new TextArea();
        activityLogArea.setEditable(false);
        activityLogArea.setPrefRowCount(10);
        activityLogArea.setWrapText(true);

        Button refreshButton = new Button("Refresh Stats");
        refreshButton.setOnAction(e -> updateStats());

        statsPanel.getChildren().addAll(
            titleLabel,
            statusLabel,
            new Label("Activity Log:"),
            activityLogArea,
            refreshButton
        );

        return statsPanel;
    }

    private void applyConfiguration() {
        try {
            int maxTickets = Integer.parseInt(maxTicketCapacityField.getText());
            int currentTickets = Integer.parseInt(currentTicketCountField.getText());
            int vendorDelay = Integer.parseInt(vendorDelayField.getText());
            int customerDelay = Integer.parseInt(customerDelayField.getText());
            String eventName = eventNameField.getText();
            int ticketPrice = Integer.parseInt(ticketPriceField.getText());

            if (maxTickets < currentTickets) {
                showError("Current ticket count cannot exceed maximum capacity");
                return;
            }

            ticketPool = new TicketPool(maxTickets);
            ticketPool.createTicket(currentTickets, eventName, ticketPrice);
            mainMenu = new MainMenu(ticketPool, vendorDelay, customerDelay, eventName, ticketPrice);
            
            updateStats();
            showInfo("System configured successfully");
            
            // Enable other tabs
            mainTabPane.getTabs().forEach(tab -> tab.setDisable(false));
            
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for numeric fields");
        } catch (Exception e) {
            showError("Configuration error: " + e.getMessage());
        }
    }

    private void handleLogin(boolean isVendor, String email, String phone, String password) {
        if (ticketPool == null) {
            showError("Please configure the system first");
            return;
        }

        if (isVendor) {
            mainMenu.loginVendor();
        } else {
            mainMenu.loginCustomer();
        }
    }

    private void handleRegistration(boolean isVendor, String name, String email, String phone, String password) {
        if (ticketPool == null) {
            showError("Please configure the system first");
            return;
        }

        if (isVendor) {
            mainMenu.registerVendor();
        } else {
            mainMenu.registerCustomer();
        }
    }

    private void updateStats() {
        if (ticketPool != null) {
            Platform.runLater(() -> {
                StringBuilder status = new StringBuilder();
                status.append("Total Capacity: ").append(ticketPool.getTotalCount()).append("\n");
                status.append("Available Tickets: ").append(ticketPool.getCurrentCount() - ticketPool.getTicketsSold()).append("\n");
                status.append("Remaining Capacity: ").append(ticketPool.getTotalCount() - ticketPool.getCurrentCount());
                statusLabel.setText(status.toString());
                
                activityLogArea.clear();
                for (String log : ticketPool.getActivityLogs()) {
                    activityLogArea.appendText(log + "\n");
                }
            });
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 