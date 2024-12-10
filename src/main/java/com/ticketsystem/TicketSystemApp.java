package com.ticketsystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TicketSystemApp extends Application {
    private TicketPool ticketPool;
    private TextArea logArea;
    private Label statusLabel;
    private VBox vendorBox;
    private VBox customerBox;
    private ScheduledExecutorService scheduler;

    @Override
    public void start(Stage primaryStage) {
        // Initialize components
        ticketPool = new TicketPool();
        ticketPool.setMaxCapacity(50);
        initializeUI(primaryStage);
        startUpdateThread();
    }

    private void initializeUI(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Create main sections
        vendorBox = createVendorSection();
        customerBox = createCustomerSection();
        VBox centerBox = createCenterSection();

        // Add sections to root
        root.setLeft(vendorBox);
        root.setCenter(centerBox);
        root.setRight(customerBox);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Ticket System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createVendorSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.TOP_CENTER);
        box.setPrefWidth(300);

        Label title = new Label("Vendors");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button addVendorBtn = new Button("Add New Vendor");
        addVendorBtn.setMaxWidth(Double.MAX_VALUE);
        addVendorBtn.setOnAction(e -> showAddVendorDialog());

        box.getChildren().addAll(title, addVendorBtn);
        return box;
    }

    private VBox createCustomerSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.TOP_CENTER);
        box.setPrefWidth(300);

        Label title = new Label("Customers");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button addCustomerBtn = new Button("Add New Customer");
        addCustomerBtn.setMaxWidth(Double.MAX_VALUE);
        addCustomerBtn.setOnAction(e -> showAddCustomerDialog());

        box.getChildren().addAll(title, addCustomerBtn);
        return box;
    }

    private VBox createCenterSection() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.TOP_CENTER);

        // Status section
        statusLabel = new Label("System Status");
        statusLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Log area
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(20);
        logArea.setWrapText(true);

        // Control buttons
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER);

        Button clearLogsBtn = new Button("Clear Logs");
        clearLogsBtn.setOnAction(e -> logArea.clear());

        Button saveStateBtn = new Button("Save State");
        saveStateBtn.setOnAction(e -> {
            ticketPool.saveTickets();
            logArea.appendText("System state saved.\n");
        });

        controlBox.getChildren().addAll(clearLogsBtn, saveStateBtn);

        box.getChildren().addAll(statusLabel, logArea, controlBox);
        return box;
    }

    private void showAddVendorDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add New Vendor");

        // Create the custom dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField releaseRateField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Release Rate (ms):"), 0, 1);
        grid.add(releaseRateField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    int releaseRate = Integer.parseInt(releaseRateField.getText());
                    
                    Vendor vendor = new Vendor(ticketPool, releaseRate);
                    Thread vendorThread = new Thread(vendor, name);
                    vendorThread.start();

                    // Add vendor to UI
                    Button vendorBtn = new Button(name);
                    vendorBtn.setMaxWidth(Double.MAX_VALUE);
                    vendorBox.getChildren().add(vendorBtn);

                    logArea.appendText("Added new vendor: " + name + "\n");
                } catch (NumberFormatException ex) {
                    showError("Invalid input", "Please enter valid numbers.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAddCustomerDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField retrievalRateField = new TextField();
        CheckBox vipCheckBox = new CheckBox("VIP Customer");
        ComboBox<Integer> vipLevelBox = new ComboBox<>();
        vipLevelBox.getItems().addAll(1, 2, 3);
        vipLevelBox.setDisable(true);

        vipCheckBox.setOnAction(e -> vipLevelBox.setDisable(!vipCheckBox.isSelected()));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Retrieval Rate (ms):"), 0, 1);
        grid.add(retrievalRateField, 1, 1);
        grid.add(vipCheckBox, 0, 2);
        grid.add(vipLevelBox, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String name = nameField.getText();
                    int retrievalRate = Integer.parseInt(retrievalRateField.getText());
                    
                    Customer customer;
                    if (vipCheckBox.isSelected() && vipLevelBox.getValue() != null) {
                        customer = new VIPCustomer(ticketPool, retrievalRate, vipLevelBox.getValue());
                    } else {
                        customer = new Customer(ticketPool, retrievalRate);
                    }

                    Thread customerThread = new Thread(customer, name);
                    customerThread.start();

                    // Add customer to UI
                    Button customerBtn = new Button(name + (vipCheckBox.isSelected() ? " (VIP)" : ""));
                    customerBtn.setMaxWidth(Double.MAX_VALUE);
                    customerBox.getChildren().add(customerBtn);

                    logArea.appendText("Added new customer: " + name + "\n");
                } catch (NumberFormatException ex) {
                    showError("Invalid input", "Please enter valid numbers.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void startUpdateThread() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                int totalTickets = ticketPool.getTotalCount();
                int ticketsSold = ticketPool.getTicketsSold();
                statusLabel.setText(String.format("Available Tickets: %d | Sold: %d", 
                    totalTickets, ticketsSold));
            });
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
        ticketPool.shutdown();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 