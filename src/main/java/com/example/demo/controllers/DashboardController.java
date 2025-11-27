package com.example.demo.controllers;

import com.example.demo.Main;
import com.example.demo.services.BankingServiceDAO;
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
    private BankingServiceDAO bankingService = Main.getBankingService();
    private Account selectedAccount;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        updateDisplay();
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
            try {
                // CORRECTED: Using transactions.fxml instead of TransactionView.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/transactions.fxml"));
                Parent root = loader.load();

                // You'll need to create a TransactionsController that matches your FXML
                // TransactionController controller = loader.getController();
                // controller.setAccount(selectedAccount);

                Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
                stage.setTitle("Transaction History - " + selectedAccount.getAccountNumber());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Cannot load transaction history: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Please select an account first");
        }
    }

    @FXML
    private void handleOpenNewAccount() {
        try {
            // CORRECTED: Using new-account.fxml instead of NewAccountView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/new-account.fxml"));
            Parent root = loader.load();

            NewAccountController controller = loader.getController();
            controller.setCustomer(customer);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Open New Account - " + customer.getFirstName());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot load new account form: " + e.getMessage());
        }
    }

    @FXML
    private void handleManageProfile() {
        try {
            // CORRECTED: Using customer-profile.fxml instead of CustomerView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/customer-profile.fxml"));
            Parent root = loader.load();

            // You'll need to create a CustomerProfileController that matches your FXML
            // CustomerController controller = loader.getController();
            // controller.setCustomer(customer);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Manage Profile - " + customer.getFirstName());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot load profile form: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewAccounts() {
        try {
            // Using accounts.fxml for viewing all accounts
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/accounts.fxml"));
            Parent root = loader.load();

            // You'll need to create an AccountsController
            // AccountsController controller = loader.getController();
            // controller.setCustomer(customer);

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("All Accounts - " + customer.getFirstName());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Cannot load accounts view: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Logout");
            confirmAlert.setHeaderText("Are you sure you want to logout?");
            confirmAlert.setContentText("You will be redirected to the login screen.");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/login.fxml"));
                Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));
                stage.setTitle("Banking System - Login");

                showAlert("Success", "Logged out successfully!");
            }
        } catch (IOException e) {
            showAlert("Error", "Logout failed: " + e.getMessage());
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