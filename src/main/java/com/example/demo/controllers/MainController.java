package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label customerIdLabel;
    @FXML private Label balanceLabel;
    @FXML private Label accountNumberLabel;

    private String currentUser = "Baking Gabanthate";
    private String customerId = "CSE23012";
    private double currentBalance = 2500.00;
    private String accountNumber = "ACC784392";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateDashboard();
    }

    private void updateDashboard() {
        welcomeLabel.setText("Welcome, " + currentUser + "!");
        customerIdLabel.setText("Customer ID: " + customerId);
        balanceLabel.setText(String.format("Current Balance: BWP %.2f", currentBalance));
        accountNumberLabel.setText("Account: " + accountNumber);
    }

    // Check Balance - This might be what's missing
    @FXML
    private void handleCheckBalance() {
        System.out.println("Balance checked by: " + customerId);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Account Balance");
        alert.setHeaderText("Current Account Balance");
        alert.setContentText(String.format("Your current balance is: BWP %.2f", currentBalance));
        alert.showAndWait();
    }

    @FXML
    private void handleNewAccount() {
        System.out.println("Opening new account form for: " + customerId);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/new-account.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Banking System - Open New Account");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert("Cannot load new account form");
        }
    }

    @FXML
    private void handleDeposit() {
        System.out.println("Deposit requested by: " + customerId);
        showFeatureAlert("Deposit Funds", "Deposit functionality will be available soon!");
    }

    @FXML
    private void handleWithdraw() {
        System.out.println("Withdrawal requested by: " + customerId);
        showFeatureAlert("Withdraw Funds", "Withdrawal functionality will be available soon!");
    }

    @FXML
    private void handleViewTransactions() {
        System.out.println("Viewing transactions for: " + customerId);
        showFeatureAlert("Transaction History", "Transaction history will be available soon!");
    }

    @FXML
    private void handleTransferFunds() {
        System.out.println("Transfer requested by: " + customerId);
        showFeatureAlert("Transfer Funds", "Fund transfer functionality will be available soon!");
    }

    @FXML
    private void handleAccountSettings() {
        System.out.println("Settings requested by: " + customerId);
        showFeatureAlert("Account Settings", "Account settings will be available soon!");
    }

    @FXML
    private void handleLogout() {
        System.out.println("Logging out...");

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                System.out.println("User logged out successfully");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/login.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) welcomeLabel.getScene().getWindow();
                    stage.setScene(new Scene(root, 800, 600));
                    stage.setTitle("Banking System - Login");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showFeatureAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Feature Coming Soon");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Navigation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility methods
    public void updateUserBalance(double newBalance) {
        this.currentBalance = newBalance;
        updateDashboard();
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }
}