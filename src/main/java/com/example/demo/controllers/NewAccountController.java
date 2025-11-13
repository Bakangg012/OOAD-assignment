package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class NewAccountController implements Initializable {

    @FXML private ChoiceBox<String> accountTypeChoice;
    @FXML private TextField initialDepositField;
    @FXML private TextField employerNameField;
    @FXML private TextField employerAddressField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up account types
        accountTypeChoice.getItems().addAll("Savings Account", "Investment Account", "Cheque Account");
        accountTypeChoice.setValue("Savings Account");

        // Show/hide employment fields based on account type
        accountTypeChoice.setOnAction(e -> toggleEmploymentFields());
        toggleEmploymentFields(); // Initial state
    }

    private void toggleEmploymentFields() {
        String selectedType = accountTypeChoice.getValue();
        boolean isChequeAccount = "Cheque Account".equals(selectedType);

        employerNameField.setVisible(isChequeAccount);
        employerAddressField.setVisible(isChequeAccount);

        // Also manage the labels if they exist
        // You might want to add fx:id to labels in FXML for complete control
    }

    @FXML
    private void handleOpenAccount() {
        String accountType = accountTypeChoice.getValue();
        String initialDepositText = initialDepositField.getText();

        if (!validateAccountOpening(accountType, initialDepositText)) {
            return;
        }

        double initialDeposit = Double.parseDouble(initialDepositText);
        String employerName = employerNameField.getText();
        String employerAddress = employerAddressField.getText();

        // Create new account
        boolean success = createNewAccount(accountType, initialDeposit, employerName, employerAddress);

        if (success) {
            showSuccessAlert("Account Opened",
                    "Your new " + accountType + " has been opened successfully!\n" +
                            "Initial Deposit: BWP " + initialDeposit);
            // Main.showDashboardScene();
        } else {
            showAlert("Account Opening Failed", "Error opening account. Please try again.");
        }
    }

    @FXML
    private void handleBackToDashboard() {
        // Main.showDashboardScene();
        System.out.println("Back to dashboard clicked");
    }

    private boolean validateAccountOpening(String accountType, String initialDepositText) {
        // Your validation logic here
        if (initialDepositText.isEmpty()) {
            showAlert("Validation Error", "Please enter initial deposit amount.");
            return false;
        }

        try {
            double deposit = Double.parseDouble(initialDepositText);

            if (deposit <= 0) {
                showAlert("Validation Error", "Initial deposit must be positive.");
                return false;
            }

            if ("Investment Account".equals(accountType) && deposit < 500.0) {
                showAlert("Validation Error", "Investment Account requires minimum deposit of BWP 500.00");
                return false;
            }

            if ("Cheque Account".equals(accountType) &&
                    (employerNameField.getText().isEmpty() || employerAddressField.getText().isEmpty())) {
                showAlert("Validation Error", "Cheque Account requires employer information.");
                return false;
            }

        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please enter a valid deposit amount.");
            return false;
        }

        return true;
    }

    private boolean createNewAccount(String accountType, double initialDeposit,
                                     String employerName, String employerAddress) {
        // TODO: Implement database save for new account
        System.out.println("Creating new " + accountType + " with deposit: BWP " + initialDeposit);

        if ("Cheque Account".equals(accountType)) {
            System.out.println("Employer: " + employerName + ", Address: " + employerAddress);
        }

        return true; // Simulate success
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}