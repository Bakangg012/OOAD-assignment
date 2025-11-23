package com.example.demo.controllers;

import com.example.demo.services.BankingService;
import com.example.demo.models.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {

    @FXML private TextField customerIdField;
    @FXML private PasswordField passwordField;

    private BankingService bankingService = new BankingService();

    @FXML
    private void handleLogin() {
        String customerId = customerIdField.getText().trim();

        if (customerId.isEmpty()) {
            showAlert("Error", "Please enter your Customer ID");
            return;
        }

        Customer customer = bankingService.findCustomerById(customerId);

        if (customer != null) {
            showDashboard(customer);
        } else {
            showAlert("Error", "Customer ID not found. Please try CUST001, CUST002, etc.");
        }
    }

    @FXML
    private void handleRegister() {
        // CORRECTED PATH - Remove "/views/" from the path
        showScene("/com/example/demo/register.fxml", "Register New Customer");
    }

    private void showDashboard(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankingService(bankingService);

            Stage stage = (Stage) customerIdField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Bank Dashboard - " + customer.getFirstName());

        } catch (IOException e) {
            showAlert("Error", "Cannot load dashboard: " + e.getMessage());
        }
    }

    private void showScene(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) customerIdField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle(title);
        } catch (IOException e) {
            showAlert("Error", "Cannot load scene: " + e.getMessage());
            e.printStackTrace(); // Add this for debugging
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}