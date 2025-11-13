package com.example.demo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewAccountController {

    @FXML private ComboBox<String> accountTypeCombo;
    @FXML private TextField initialDepositField;
    @FXML private TextField specialFeatureField;

    @FXML
    public void initialize() {
        // Initialize account types
        accountTypeCombo.getItems().addAll("Savings", "Cheque", "Investment");
    }

    @FXML
    public void backToDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/dashboard.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Banking System - Dashboard");
    }

    @FXML
    public void createAccount() {
        String accountType = accountTypeCombo.getValue();
        String depositText = initialDepositField.getText();
        String feature = specialFeatureField.getText();

        if (accountType == null || depositText.isEmpty()) {
            showAlert("Error", "Please fill in all required fields.");
            return;
        }

        try {
            double initialDeposit = Double.parseDouble(depositText);
            showAlert("Success", "Account created successfully!\nType: " + accountType + "\nDeposit: $" + initialDeposit);
            // TODO: Add account to banking system
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for initial deposit.");
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