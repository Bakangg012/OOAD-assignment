package com.example.demo.controllers;

import com.example.demo.services.BankingService;
import com.example.demo.models.Customer;
import com.example.demo.models.Account;
import com.example.demo.models.InsufficientFundsException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.ButtonType;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label customerInfoLabel;
    @FXML private ListView<String> accountsListView;
    @FXML private TextField depositAmountField;
    @FXML private TextField withdrawAmountField;
    @FXML private Label selectedAccountLabel;

    private Customer customer;
    private BankingService bankingService;
    private Account selectedAccount;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        updateDisplay();
    }

    public void setBankingService(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    private void updateDisplay() {
        if (customer != null) {
            welcomeLabel.setText("Welcome, " + customer.getFirstName() + "!");
            customerInfoLabel.setText("Customer ID: " + customer.getCustomerId() +
                    " | Phone: " + customer.getPhone());
            loadAccounts();
        }
    }

    private void loadAccounts() {
        accountsListView.getItems().clear();
        for (Account account : customer.getAccounts()) {
            String accountInfo = String.format("%s: %s - BWP%.2f",
                    account.getAccountType(),
                    account.getAccountNumber(),
                    account.getBalance());
            accountsListView.getItems().add(accountInfo);
        }

        if (!customer.getAccounts().isEmpty()) {
            accountsListView.getSelectionModel().select(0);
            selectAccount();
        }
    }

    @FXML
    private void selectAccount() {
        int selectedIndex = accountsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < customer.getAccounts().size()) {
            selectedAccount = customer.getAccounts().get(selectedIndex);
            selectedAccountLabel.setText("Selected: " + selectedAccount.getAccountType() +
                    " - " + selectedAccount.getAccountNumber());
        }
    }

    @FXML
    private void handleDeposit() {
        if (selectedAccount == null) {
            showAlert("Error", "Please select an account first");
            return;
        }

        try {
            double amount = Double.parseDouble(depositAmountField.getText());
            if (amount <= 0) {
                showAlert("Error", "Deposit amount must be positive");
                return;
            }

            bankingService.deposit(selectedAccount, amount);
            showAlert("Success", "Deposited BWP" + amount + " successfully");
            updateDisplay();
            depositAmountField.clear();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        } catch (Exception e) {
            showAlert("Error", "Deposit failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleWithdraw() {
        if (selectedAccount == null) {
            showAlert("Error", "Please select an account first");
            return;
        }

        try {
            double amount = Double.parseDouble(withdrawAmountField.getText());
            if (amount <= 0) {
                showAlert("Error", "Withdrawal amount must be positive");
                return;
            }

            bankingService.withdraw(selectedAccount, amount);
            showAlert("Success", "Withdrawn BWP" + amount + " successfully");
            updateDisplay();
            withdrawAmountField.clear();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        } catch (InsufficientFundsException e) {
            showAlert("Error", "Withdrawal failed: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Error", "Withdrawal failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewTransactions() {
        if (selectedAccount != null) {
            showAlert("Info", "Transaction history for " + selectedAccount.getAccountNumber() + " would appear here");
        } else {
            showAlert("Error", "Please select an account first");
        }
    }

    @FXML
    private void handleOpenNewAccount() {
        showScene("/com/example/demo/views/NewAccountView.fxml", "Open New Account");
    }

    @FXML
    private void handleManageProfile() {
        showScene("/com/example/demo/views/CustomerView.fxml", "Manage Profile");
    }

    @FXML
    private void handleLogout() {
        try {
            // Show logout confirmation
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Logout");
            confirmAlert.setHeaderText("Are you sure you want to logout?");
            confirmAlert.setContentText("You will be redirected to the login screen.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Load login screen
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/views/LoginView.fxml"));
                Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
                stage.setTitle("Banking System - Login");

                showAlert("Success", "Logged out successfully!");
            }
        } catch (IOException e) {
            showAlert("Error", "Logout failed: " + e.getMessage());
        }
    }

    private void showScene(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle(title);
        } catch (IOException e) {
            showAlert("Error", "Cannot load scene: " + e.getMessage());
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