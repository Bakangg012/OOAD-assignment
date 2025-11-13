package com.example.demo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome to Banking System Dashboard!");
    }

    // Navigation methods for all the buttons
    @FXML
    public void showAccounts(ActionEvent event) {
        loadFXML("/com/example/demo/accounts.fxml", "Banking System - Accounts", event);
    }

    @FXML
    public void showNewAccount(ActionEvent event) {
        loadFXML("/com/example/demo/new-account.fxml", "Banking System - New Account", event);
    }

    @FXML
    public void showTransactions(ActionEvent event) {
        loadFXML("/com/example/demo/transactions.fxml", "Banking System - Transactions", event);
    }

    @FXML
    public void showProfile(ActionEvent event) {
        loadFXML("/com/example/demo/customer-profile.fxml", "Banking System - Profile", event);
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Banking System - Login");
    }

    // Helper method to load FXML files
    private void loadFXML(String fxmlPath, String title, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not load: " + fxmlPath + "\n\nThis feature is not fully implemented yet.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}