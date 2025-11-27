package com.example.demo.controllers;

import com.example.demo.Main;
import com.example.demo.services.BankingServiceDAO;
import com.example.demo.models.Customer;
import com.example.demo.models.Account;
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

    @FXML
    private Label employerLabel;

    @FXML
    private Label companyAddressLabel;

    private Customer customer;
    private BankingServiceDAO bankingService = Main.getBankingService();

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

            // Account-specific validation
            if ("INVESTMENT".equals(accountType) && initialDeposit < 500.0) {
                showMessage("Investment account requires minimum BWP 500.00", true);
                return;
            }

            if (initialDeposit < 10.0) {
                showMessage("Minimum initial deposit is BWP 10.00", true);
                return;
            }

            // Additional validation for cheque accounts
            if ("CHEQUE".equals(accountType)) {
                if (employerField.getText().trim().isEmpty() || companyAddressField.getText().trim().isEmpty()) {
                    showMessage("Please fill all required fields for cheque account", true);
                    return;
                }
            }

            // Create the account using BankingServiceDAO
            Account newAccount = null;
            String branch = "Main Branch"; // You can make this a field if needed

            switch (accountType) {
                case "SAVINGS":
                    newAccount = bankingService.openSavingsAccount(customer, branch, initialDeposit);
                    break;
                case "INVESTMENT":
                    newAccount = bankingService.openInvestmentAccount(customer, branch, initialDeposit);
                    break;
                case "CHEQUE":
                    newAccount = bankingService.openChequeAccount(customer, branch,
                            employerField.getText().trim(),
                            companyAddressField.getText().trim(),
                            initialDeposit);
                    break;
            }

            if (newAccount != null) {
                String successMessage = String.format(
                        "Account created successfully!\n" +
                                "Account Number: %s\n" +
                                "Type: %s\n" +
                                "Initial Deposit: BWP%.2f\n" +
                                "Customer: %s",
                        newAccount.getAccountNumber(),
                        newAccount.getAccountType(),
                        initialDeposit,
                        customer.getFullName()
                );

                showMessage(successMessage, false);

                // Wait a moment then return to dashboard
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                javafx.application.Platform.runLater(() -> {
                                    returnToDashboard();
                                });
                            }
                        },
                        2000 // 2 second delay
                );

            } else {
                showMessage("Failed to create account. Please try again.", true);
            }

        } catch (NumberFormatException e) {
            showMessage("Please enter a valid deposit amount", true);
        } catch (Exception e) {
            showMessage("Error creating account: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        returnToDashboard();
    }

    private void returnToDashboard() {
        try {
            System.out.println("Returning to dashboard...");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            DashboardController dashboardController = loader.getController();
            dashboardController.setCustomer(customer);

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