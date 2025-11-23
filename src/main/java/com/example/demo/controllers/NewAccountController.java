package com.example.demo.controllers;

import com.example.demo.services.BankingService;
import com.example.demo.models.Customer;
import com.example.demo.models.Account;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class NewAccountController {

    @FXML private ChoiceBox<String> accountTypeChoice;
    @FXML private TextField initialDepositField;
    @FXML private TextField employerField;
    @FXML private TextField companyAddressField;

    private BankingService bankingService;
    private Customer customer;

    public void setBankingService(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        initialize();
    }

    private void initialize() {
        accountTypeChoice.getItems().addAll("SAVINGS", "INVESTMENT", "CHEQUE");
        accountTypeChoice.setValue("SAVINGS");

        // Show/hide cheque account fields based on selection
        accountTypeChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isCheque = "CHEQUE".equals(newVal);
            employerField.setVisible(isCheque);
            companyAddressField.setVisible(isCheque);
        });
    }

    @FXML
    private void handleOpenAccount() {
        try {
            String accountType = accountTypeChoice.getValue();
            double initialDeposit = Double.parseDouble(initialDepositField.getText());

            Account newAccount;

            switch (accountType) {
                case "SAVINGS":
                    newAccount = bankingService.openSavingsAccount(customer, "Main Branch", initialDeposit);
                    break;
                case "INVESTMENT":
                    newAccount = bankingService.openInvestmentAccount(customer, "Main Branch", initialDeposit);
                    break;
                case "CHEQUE":
                    String employer = employerField.getText();
                    String companyAddress = companyAddressField.getText();
                    newAccount = bankingService.openChequeAccount(customer, "Main Branch", employer, companyAddress, initialDeposit);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid account type");
            }

            showAlert("Success", "New " + accountType + " account opened successfully!\nAccount Number: " + newAccount.getAccountNumber());
            handleBack();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid initial deposit amount");
        } catch (Exception e) {
            showAlert("Error", "Account opening failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        showAlert("Info", "Back to Dashboard");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}