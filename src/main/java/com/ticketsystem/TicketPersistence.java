package com.ticketsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketPersistence {
    private static final String DB_URL = "jdbc:sqlite:tickets.db";
    private Connection conn;

    public TicketPersistence() {
        try {
            conn = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    private void createTables() {
        try (Statement stmt = conn.createStatement()) {
            // Create tickets table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tickets (
                    id INTEGER PRIMARY KEY,
                    event_name TEXT NOT NULL,
                    price REAL NOT NULL,
                    priority INTEGER DEFAULT 0
                )
            """);
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public void saveTickets(List<Ticket> tickets) {
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO tickets (id, event_name, price, priority) VALUES (?, ?, ?, ?)")) {
                
                // Clear existing tickets
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("DELETE FROM tickets");
                }

                // Insert new tickets
                for (Ticket ticket : tickets) {
                    pstmt.setInt(1, ticket.getId());
                    pstmt.setString(2, ticket.getEventName());
                    pstmt.setDouble(3, ticket.getPrice());
                    pstmt.setInt(4, ticket.getPriority());
                    pstmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error rolling back transaction: " + rollbackEx.getMessage());
            }
            System.err.println("Error saving tickets: " + e.getMessage());
        }
    }

    public List<Ticket> loadTickets() {
        List<Ticket> tickets = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tickets")) {
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String eventName = rs.getString("event_name");
                double price = rs.getDouble("price");
                int priority = rs.getInt("priority");
                tickets.add(new Ticket(id, eventName, price, priority));
            }
        } catch (SQLException e) {
            System.err.println("Error loading tickets: " + e.getMessage());
        }
        return tickets;
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
} 