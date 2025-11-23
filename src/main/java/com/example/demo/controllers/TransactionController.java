package com.example.demo.controllers;

import com.example.demo.models.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;

public class TransactionController {

    @FXML private Label accountInfoLabel;
    @FXML private ListView<String> transactionsListView;

    private Account account;

    public void setAccount(Account account) {
        this.account = account;
        updateDisplay();
    }

    private void updateDisplay() {
        if (account != null) {
            accountInfoLabel.setText("Transaction History - " + account.getAccountType() + " Account: " + account.getAccountNumber());
            loadTransactions();
        }
    }

    private void loadTransactions() {
        transactionsListView.getItems().clear();
        // Sample transaction data
        transactionsListView.getItems().add("2024-01-01: Opening Balance - BWP0.00");
        transactionsListView.getItems().add("2024-01-15: Deposit - BWP1000.00");
        transactionsListView.getItems().add("2024-01-20: Monthly Interest - BWP0.50");
        transactionsListView.getItems().add("2024-01-25: Deposit - BWP500.00");
        transactionsListView.getItems().add("2024-02-01: Current Balance - BWP" + account.getBalance());
    }

    @FXML
    private void handleBack() {
        try {
            // Navigate back to Dashboard
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/views/DashboardView.fxml"));
            Stage stage = (Stage) accountInfoLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Bank Dashboard");
        } catch (IOException e) {
            showAlert("Error", "Cannot return to dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh() {
        loadTransactions();
        showAlert("Info", "Transaction history refreshed");
    }

    @FXML
    private void handlePrint() {
        showAlert("Info", "Print functionality would be implemented here");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}