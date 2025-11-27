package com.example.demo.controllers;

import com.example.demo.models.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class NewAccountController {

    @FXML
    private ChoiceBox<String> accountTypeChoice;

    @FXML
    private TextField initialDepositField;

    @FXML
    private TextField employerField;

    @FXML
    private TextField companyAddressField;

    @FXML
    private Label messageLabel;

    // Remove mainContainer since it's not in your FXML
    // @FXML
    // private VBox mainContainer;

    @FXML
    private Label employerLabel;

    @FXML
    private Label companyAddressLabel;

    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        System.out.println("Customer set in NewAccountController: " +
                (customer != null ? customer.getFullName() : "null"));
    }

    @FXML
    public void initialize() {
        System.out.println("NewAccountController initialized!");

        // Initialize account types
        accountTypeChoice.getItems().addAll("SAVINGS", "INVESTMENT", "CHEQUE");

        // Set default selection
        accountTypeChoice.setValue("SAVINGS");

        // Add listener to show/hide cheque account fields
        accountTypeChoice.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    handleAccountTypeChange(newValue);
                }
        );

        // Add input validation for deposit field
        initialDepositField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                initialDepositField.setText(oldValue);
            }
        });

        // Initially hide cheque account fields
        hideChequeFields();
    }

    private void handleAccountTypeChange(String accountType) {
        if ("CHEQUE".equals(accountType)) {
            showChequeFields();
        } else {
            hideChequeFields();
        }
    }

    private void showChequeFields() {
        employerField.setVisible(true);
        employerField.setManaged(true);
        companyAddressField.setVisible(true);
        companyAddressField.setManaged(true);

        if (employerLabel != null) {
            employerLabel.setVisible(true);
            employerLabel.setManaged(true);
        }
        if (companyAddressLabel != null) {
            companyAddressLabel.setVisible(true);
            companyAddressLabel.setManaged(true);
        }
    }

    private void hideChequeFields() {
        employerField.setVisible(false);
        employerField.setManaged(false);
        companyAddressField.setVisible(false);
        companyAddressField.setManaged(false);

        if (employerLabel != null) {
            employerLabel.setVisible(false);
            employerLabel.setManaged(false);
        }
        if (companyAddressLabel != null) {
            companyAddressLabel.setVisible(false);
            companyAddressLabel.setManaged(false);
        }
    }

    @FXML
    private void handleOpenAccount() {
        try {
            // Check if customer is set
            if (customer == null) {
                showMessage("Error: No customer data available", true);
                return;
            }

            String accountType = accountTypeChoice.getValue();
            String depositText = initialDepositField.getText().trim();

            // Validation
            if (accountType == null || accountType.isEmpty()) {
                showMessage("Please select an account type", true);
                return;
            }

            if (depositText.isEmpty()) {
                showMessage("Please enter initial deposit amount", true);
                return;
            }

            double initialDeposit = Double.parseDouble(depositText);
            if (initialDeposit < 0) {
                showMessage("Initial deposit cannot be negative", true);
                return;
            }

            if (initialDeposit < 10.0) {
                showMessage("Minimum initial deposit is $10.00", true);
                return;
            }

            // Additional validation for cheque accounts
            if ("CHEQUE".equals(accountType)) {
                if (employerField.getText().trim().isEmpty() || companyAddressField.getText().trim().isEmpty()) {
                    showMessage("Please fill all required fields for cheque account", true);
                    return;
                }
            }

            // Here you would call your banking service to create the account
            System.out.println("Creating " + accountType + " account for customer: " +
                    customer.getFullName() + " with deposit: $" + initialDeposit);

            if ("CHEQUE".equals(accountType)) {
                System.out.println("Employer: " + employerField.getText());
                System.out.println("Company Address: " + companyAddressField.getText());
            }

            // FIXED: Using getFullName() instead of getLastName()
            String successMessage = String.format(
                    "Account created successfully!\nCustomer: %s\nType: %s\nInitial Deposit: $%.2f",
                    customer.getFullName(), accountType, initialDeposit
            );

            showMessage(successMessage, false);

            // Clear form after successful creation
            clearForm();

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid deposit amount", true);
        } catch (Exception e) {
            showMessage("Error creating account: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            System.out.println("Returning to dashboard...");

            // Load the dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            // Set the customer in the dashboard controller
            DashboardController dashboardController = loader.getController();
            dashboardController.setCustomer(customer);

            // FIXED: Use initialDepositField to get the scene instead of mainContainer
            Stage currentStage = (Stage) initialDepositField.getScene().getWindow();
            currentStage.setScene(new Scene(dashboardRoot, 800, 600));
            currentStage.setTitle("Banking Dashboard - " + customer.getFullName());

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot return to dashboard: " + e.getMessage());
        }
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle(isError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #27ae60;");
        messageLabel.setVisible(true);
    }

    private void clearForm() {
        initialDepositField.clear();
        employerField.clear();
        companyAddressField.clear();
        accountTypeChoice.setValue("SAVINGS");
        messageLabel.setVisible(false);
        hideChequeFields();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}