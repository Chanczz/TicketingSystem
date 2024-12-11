package com.ticketing;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class TicketingSystemGUI extends Application {
    private TicketingSystem ticketingSystem;
    private TextArea logArea;
    private Label availableTicketsLabel;
    private VBox customerStatsBox;
    private boolean isConfigured = false;

    // Configuration fields
    private TextField maxCapacityField;
    private TextField numVendorsField;
    private TextField numCustomersField;
    private TextField releaseIntervalField;
    private TextField retrievalIntervalField;
    private TextField ticketsPerReleaseField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Real-Time Event Ticketing System");

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Create configuration panel
        VBox configPanel = createConfigurationPanel();
        
        // Create control panel
        HBox controlPanel = createControlPanel();
        
        // Create monitoring panel
        VBox monitoringPanel = createMonitoringPanel();

        // Add panels to main layout
        mainLayout.setTop(configPanel);
        mainLayout.setCenter(monitoringPanel);
        mainLayout.setBottom(controlPanel);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set up periodic UI updates
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateUI()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        primaryStage.setOnCloseRequest(e -> {
            if (ticketingSystem != null) {
                ticketingSystem.stop();
            }
            Platform.exit();
        });
    }

    private VBox createConfigurationPanel() {
        VBox configPanel = new VBox(10);
        configPanel.setPadding(new Insets(10));
        configPanel.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");

        Label titleLabel = new Label("System Configuration");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        // Create input fields
        maxCapacityField = new TextField();
        numVendorsField = new TextField();
        numCustomersField = new TextField();
        releaseIntervalField = new TextField();
        retrievalIntervalField = new TextField();
        ticketsPerReleaseField = new TextField();

        // Create field containers
        VBox fieldsContainer = new VBox(10);
        fieldsContainer.getChildren().addAll(
            createLabeledField("Maximum Ticket Capacity:", maxCapacityField),
            createLabeledField("Number of Vendors:", numVendorsField),
            createLabeledField("Number of Customers:", numCustomersField),
            createLabeledField("Ticket Release Interval (ms):", releaseIntervalField),
            createLabeledField("Ticket Retrieval Interval (ms):", retrievalIntervalField),
            createLabeledField("Tickets Per Release:", ticketsPerReleaseField)
        );

        Button configureButton = new Button("Configure System");
        configureButton.setOnAction(e -> configureSystem());

        configPanel.getChildren().addAll(titleLabel, fieldsContainer, configureButton);
        return configPanel;
    }

    private HBox createLabeledField(String labelText, TextField field) {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(labelText);
        label.setPrefWidth(180);
        field.setPrefWidth(100);
        container.getChildren().addAll(label, field);
        return container;
    }

    private HBox createControlPanel() {
        HBox controlPanel = new HBox(10);
        controlPanel.setPadding(new Insets(10));
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");

        Button startButton = new Button("Start System");
        Button stopButton = new Button("Stop System");

        startButton.setOnAction(e -> {
            if (isConfigured) {
                ticketingSystem.start();
                startButton.setDisable(true);
                stopButton.setDisable(false);
            } else {
                showAlert("System not configured", "Please configure the system first.");
            }
        });

        stopButton.setOnAction(e -> {
            ticketingSystem.stop();
            startButton.setDisable(false);
            stopButton.setDisable(true);
        });
        stopButton.setDisable(true);

        controlPanel.getChildren().addAll(startButton, stopButton);
        return controlPanel;
    }

    private VBox createMonitoringPanel() {
        VBox monitoringPanel = new VBox(10);
        monitoringPanel.setPadding(new Insets(10));
        monitoringPanel.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5;");

        Label titleLabel = new Label("System Monitoring");
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

        availableTicketsLabel = new Label("Available Tickets: 0");
        customerStatsBox = new VBox(5);
        customerStatsBox.setPadding(new Insets(5));

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(10);
        logArea.setWrapText(true);

        monitoringPanel.getChildren().addAll(
            titleLabel,
            availableTicketsLabel,
            new Label("Customer Statistics:"),
            customerStatsBox,
            new Label("System Log:"),
            logArea
        );

        return monitoringPanel;
    }

    private void configureSystem() {
        try {
            int maxCapacity = Integer.parseInt(maxCapacityField.getText());
            int numVendors = Integer.parseInt(numVendorsField.getText());
            int numCustomers = Integer.parseInt(numCustomersField.getText());
            long releaseInterval = Long.parseLong(releaseIntervalField.getText());
            long retrievalInterval = Long.parseLong(retrievalIntervalField.getText());
            int ticketsPerRelease = Integer.parseInt(ticketsPerReleaseField.getText());

            // Validate inputs
            if (maxCapacity <= 0 || numVendors <= 0 || numCustomers <= 0 || 
                releaseInterval <= 0 || retrievalInterval <= 0 || ticketsPerRelease <= 0) {
                showAlert("Invalid Input", "All values must be positive numbers.");
                return;
            }

            ticketingSystem = new TicketingSystem(maxCapacity);
            ticketingSystem.initialize(numVendors, numCustomers, releaseInterval, retrievalInterval, ticketsPerRelease);
            isConfigured = true;
            
            showAlert("Success", "System configured successfully!");
            
            // Disable configuration fields
            maxCapacityField.setDisable(true);
            numVendorsField.setDisable(true);
            numCustomersField.setDisable(true);
            releaseIntervalField.setDisable(true);
            retrievalIntervalField.setDisable(true);
            ticketsPerReleaseField.setDisable(true);

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter valid numbers in all fields.");
        }
    }

    private void updateUI() {
        if (ticketingSystem != null) {
            Platform.runLater(() -> {
                // Update available tickets
                TicketPool pool = ticketingSystem.getTicketPool();
                availableTicketsLabel.setText("Available Tickets: " + 
                    pool.getAvailableTickets() + "/" + 
                    pool.getMaxTicketCapacity());

                // Update customer statistics
                customerStatsBox.getChildren().clear();
                for (Customer customer : ticketingSystem.getCustomers()) {
                    Label statsLabel = new Label(customer.getCustomerId() + 
                        " purchased: " + customer.getTicketsPurchased() + " tickets");
                    customerStatsBox.getChildren().add(statsLabel);
                }
            });
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 