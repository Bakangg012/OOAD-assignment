package com.example.demo.database;

import com.example.demo.models.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public void createCustomer(Customer customer) {
        String sql = "INSERT INTO customers (id, first_name, surname, address, email, phone, password) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getSurname());
            pstmt.setString(4, customer.getAddress());
            pstmt.setString(5, customer.getEmail());
            pstmt.setString(6, customer.getPhone());
            pstmt.setString(7, customer.getPassword());

            pstmt.executeUpdate();
            System.out.println("Customer saved to database: " + customer.getFullName());

        } catch (SQLException e) {
            System.out.println("Error saving customer: " + e.getMessage());
        }
    }

    public Customer findCustomerByName(String fullName) {
        String sql = "SELECT * FROM customers WHERE first_name || ' ' || surname = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fullName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return resultSetToCustomer(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error finding customer: " + e.getMessage());
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(resultSetToCustomer(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error getting customers: " + e.getMessage());
        }
        return customers;
    }

    public void updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET first_name = ?, surname = ?, address = ?, email = ?, phone = ?, password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getSurname());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getPhone());
            pstmt.setString(6, customer.getPassword());
            pstmt.setString(7, customer.getCustomerId());

            pstmt.executeUpdate();
            System.out.println("Customer updated in database: " + customer.getFullName());

        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    private Customer resultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer(
                rs.getString("first_name"),
                rs.getString("surname"),
                rs.getString("address"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("password")
        );
        // Set the original ID
        // Note: You might need to add a setCustomerId method to your Customer class
        return customer;
    }
}