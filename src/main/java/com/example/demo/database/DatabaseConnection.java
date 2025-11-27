package com.example.demo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:./banking_system;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            // Create Customers table
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS customers (" +
                            "id VARCHAR(50) PRIMARY KEY, " +
                            "first_name VARCHAR(50), " +
                            "surname VARCHAR(50), " +
                            "address VARCHAR(255), " +
                            "email VARCHAR(100), " +
                            "phone VARCHAR(20), " +
                            "password VARCHAR(100))"
            );

            // Create Accounts table
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS accounts (" +
                            "id VARCHAR(50) PRIMARY KEY, " +
                            "customer_id VARCHAR(50), " +
                            "account_type VARCHAR(20), " +
                            "account_number VARCHAR(20), " +
                            "balance DOUBLE, " +
                            "branch VARCHAR(100), " +
                            "employer VARCHAR(100), " +
                            "company_address VARCHAR(255), " +
                            "FOREIGN KEY (customer_id) REFERENCES customers(id))"
            );

            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }
}