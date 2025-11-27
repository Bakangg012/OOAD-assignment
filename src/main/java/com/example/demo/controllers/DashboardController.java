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
    @FXML private Label totalBalanceLabel;

    private Customer customer;
    private BankingServiceDAO bankingService = Main.getBankingService();
    private Account selectedAccount;

    @FXML
    private void initialize() {
        // Set up list view selection listener
        accountsListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selectAccount()
        );
    }

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
            updateTotalBalance();
        }
    }

    private void loadAccounts() {
        accountsListView.getItems().clear();
        System.out.println("Loading accounts for customer: " + customer.getFullName());
        System.out.println("Number of accounts: " + customer.getAccounts().size());

        for (Account account : customer.getAccounts()) {
            String accountInfo = String.format("%s: %s - BWP%.2f",
                    account.getAccountType(),
                    account.getAccountNumber(),
                    account.getBalance());
            accountsListView.getItems().add(accountInfo);
            System.out.println("Added account to list: " + accountInfo);
        }

        if (!customer.getAccounts().isEmpty()) {
            accountsListView.getSelectionModel().select(0);
            selectAccount();
        } else {
            selectedAccountLabel.setText("No accounts available");
            selectedAccount = null;
        }
    }

    private void updateTotalBalance() {
        double total = 0.0;
        for (Account account : customer.getAccounts()) {
            total += account.getBalance();
        }
        totalBalanceLabel.setText(String.format("Total Balance: BWP%.2f", total));
    }

    private void selectAccount() {
        int selectedIndex = accountsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < customer.getAccounts().size()) {
            selectedAccount = customer.getAccounts().get(selectedIndex);
            selectedAccountLabel.setText("Selected: " + selectedAccount.getAccountType() +
                    " - " + selectedAccount.getAccountNumber());
            System.out.println("Selected account: " + selectedAccount.getAccountNumber());
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
            showAlert("Success", "Deposited BWP" + amount + " successfully to account " + selectedAccount.getAccountNumber());
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
            showAlert("Success", "Withdrawn BWP" + amount + " successfully from account " + selectedAccount.getAccountNumber());
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
    private void handleRefresh() {
        updateDisplay();
        showAlert("Info", "Accounts list refreshed");
    }

    @FXML
    private void handleViewTransactions() {
        if (selectedAccount != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/transactions.fxml"));
                Parent root = loader.load();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/customer-profile.fxml"));
            Parent root = loader.load();
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
        // Refresh the current view
        updateDisplay();
        showAlert("Info", "Accounts view refreshed");
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