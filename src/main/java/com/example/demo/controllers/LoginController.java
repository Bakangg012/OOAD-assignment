package com.example.demo.controllers;

import com.example.demo.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField customerIdField;
    @FXML private PasswordField pinField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> handleRegister());
    }

    private void handleLogin() {
        String customerId = customerIdField.getText();
        String pin = pinField.getText();

        if (customerId.isEmpty() || pin.isEmpty()) {
            showAlert("Error", "Please enter both Customer ID and PIN");
            return;
        }

        // Simple authentication for demo
        if (authenticateUser(customerId, pin)) {
            System.out.println("Login successful for: " + customerId);
            Main.showDashboardScene();
        } else {
            showAlert("Login Failed", "Invalid Customer ID or PIN");
        }
    }

    private void handleRegister() {
        System.out.println("Opening registration form");
        Main.showRegisterScene();
    }

    private boolean authenticateUser(String customerId, String pin) {
        // Demo authentication - accept any input
        return !customerId.isEmpty() && !pin.isEmpty();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}