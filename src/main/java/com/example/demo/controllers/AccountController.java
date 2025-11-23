package com.example.demo.controllers;

import com.example.demo.services.BankingService;
import com.example.demo.models.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;

public class AccountController {

    @FXML private Label accountDetailsLabel;
    @FXML private ListView<String> transactionListView;
    @FXML private Button backButton;
    @FXML private Button downloadStatementButton;

    private BankingService bankingService;
    private Account account;

    public void setBankingService(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    public void setAccount(Account account) {
        this.account = account;
        updateDisplay();
    }

    private void updateDisplay() {
        if (account != null) {
            accountDetailsLabel.setText(account.getAccountType() + " Account: " +
                    account.getAccountNumber() + " - BWP" + account.getBalance());
            loadTransactions();
        }
    }

    private void loadTransactions() {
        transactionListView.getItems().clear();
        // Sample transactions - in real app, get from database via bankingService
        transactionListView.getItems().add("2024-01-15: Deposit - BWP1000.00");
        transactionListView.getItems().add("2024-01-20: Interest - BWP0.50");
        transactionListView.getItems().add("2024-01-25: Deposit - BWP500.00");
        transactionListView.getItems().add("2024-01-28: Withdrawal - BWP200.00");
        transactionListView.getItems().add("2024-02-01: Deposit - BWP1500.00");
    }

    @FXML
    private void handleBack() {
        try {
            // Navigate back to Dashboard
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/dashboard.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Bank Dashboard");
        } catch (IOException e) {
            showAlert("Error", "Cannot return to dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleDownloadStatement() {
        if (account != null) {
            showAlert("Statement Download",
                    "Generating statement for account: " + account.getAccountNumber() +
                            "\nThis would typically generate a PDF statement.");
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