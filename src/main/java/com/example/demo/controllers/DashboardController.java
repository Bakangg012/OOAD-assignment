package com.example.demo.controllers;

import com.example.demo.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label customerIdLabel;
    @FXML private Button logoutButton;
    @FXML private Button newAccountButton;
    @FXML private Button viewAccountsButton;
    @FXML private Button depositButton;
    @FXML private Button withdrawButton;

    private String currentCustomerId;
    private String currentCustomerName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up button actions
        logoutButton.setOnAction(e -> handleLogout());
        newAccountButton.setOnAction(e -> handleNewAccount());
        viewAccountsButton.setOnAction(e -> handleViewAccounts());
        depositButton.setOnAction(e -> handleDeposit());
        withdrawButton.setOnAction(e -> handleWithdraw());

        loadCustomerData();
    }

    private void loadCustomerData() {
        this.currentCustomerId = "CSE23012";
        this.currentCustomerName = "Bakang Gabanthate";

        welcomeLabel.setText("Welcome, " + currentCustomerName + "!");
        customerIdLabel.setText("Customer ID: " + currentCustomerId);
    }

    private void handleLogout() {
        System.out.println("Logging out customer: " + currentCustomerId);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                clearSessionData();
                Main.showLoginScene();
            }
        });
    }

    private void handleNewAccount() {
        System.out.println("Opening new account form for: " + currentCustomerId);
        showAlert("Coming Soon", "New Account feature will be available soon!");
    }

    private void handleViewAccounts() {
        System.out.println("Viewing accounts for: " + currentCustomerId);
        showAlert("Info", "View Accounts feature coming soon!");
    }

    private void handleDeposit() {
        System.out.println("Deposit requested by: " + currentCustomerId);
        showAlert("Info", "Deposit feature coming soon!");
    }

    private void handleWithdraw() {
        System.out.println("Withdrawal requested by: " + currentCustomerId);
        showAlert("Info", "Withdrawal feature coming soon!");
    }

    private void clearSessionData() {
        this.currentCustomerId = null;
        this.currentCustomerName = null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}