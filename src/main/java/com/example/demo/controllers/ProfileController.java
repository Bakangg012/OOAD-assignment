package com.example.demo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {

    @FXML private Label customerIdLabel;
    @FXML private Label customerNameLabel;
    @FXML private Label totalAccountsLabel;
    @FXML private Label totalBalanceLabel;

    @FXML
    public void initialize() {
        // Set sample data
        customerIdLabel.setText("CUST-001");
        customerNameLabel.setText("John Doe");
        totalAccountsLabel.setText("3");
        totalBalanceLabel.setText("$3,500.00");
    }

    @FXML
    public void backToDashboard(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/dashboard.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Banking System - Dashboard");
    }

    @FXML
    public void editProfile() {
        // TODO: Implement profile editing
        System.out.println("Editing profile...");
    }
}