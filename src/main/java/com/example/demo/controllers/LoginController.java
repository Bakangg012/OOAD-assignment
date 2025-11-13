package com.example.demo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    // dummy credentials: admin / 1234
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String user = usernameField.getText();
        String pass = passwordField.getText();
        if ("admin".equals(user) && "1234".equals(pass)) {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/dashboard.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setTitle("Banking System - Dashboard");
            stage.setScene(new Scene(root));
        } else {
            // simple feedback by clearing password (minimal placeholder)
            passwordField.clear();
        }
    }
}
