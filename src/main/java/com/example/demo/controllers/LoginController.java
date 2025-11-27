package com.example.demo.controllers;

import com.example.demo.Main;
import com.example.demo.services.BankingServiceDAO;
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

    @FXML private TextField fullNameField;
    @FXML private PasswordField passwordField;

    private BankingServiceDAO bankingService = Main.getBankingService();

    @FXML
    private void handleLogin() {
        String fullName = fullNameField.getText().trim();
        String password = passwordField.getText().trim();

        if (fullName.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both name and password");
            return;
        }

        Customer customer = bankingService.findCustomerByName(fullName);

        if (customer != null && customer.getPassword().equals(password)) {
            showDashboard(customer);
        } else {
            showAlert("Error", "Invalid name or password. Please try John Smith, Jane Doe, etc.");
        }
    }

    @FXML
    private void handleRegister() {
        showScene("/com/example/demo/register.fxml", "Register New Customer");
    }

    private void showDashboard(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setCustomer(customer);
            // REMOVE THIS LINE: controller.setBankingService(bankingService);

            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Bank Dashboard - " + customer.getFirstName());

        } catch (IOException e) {
            showAlert("Error", "Cannot load dashboard: " + e.getMessage());
        }
    }

    private void showScene(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) fullNameField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle(title);
        } catch (IOException e) {
            showAlert("Error", "Cannot load scene: " + e.getMessage());
            e.printStackTrace();
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