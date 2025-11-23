package com.example.demo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class LogoutController {

    @FXML
    private void handleLogout() {
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout Confirmation");
        confirmAlert.setHeaderText("Are you sure you want to logout?");
        confirmAlert.setContentText("You will need to login again to access your accounts.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            performLogout();
        }
    }

    @FXML
    private void handleCancel() {
        // Go back to previous screen (dashboard)
        goBackToDashboard();
    }

    private void performLogout() {
        try {
            // Load login screen
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/views/LoginView.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Banking System - Login");
            stage.show();

            // Close current window
            Stage currentStage = (Stage) getCurrentStage();
            if (currentStage != null) {
                currentStage.close();
            }

            showAlert("Success", "You have been logged out successfully.");

        } catch (IOException e) {
            showAlert("Error", "Failed to logout: " + e.getMessage());
        }
    }

    private void goBackToDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/demo/views/DashboardView.fxml"));
            Stage stage = (Stage) getCurrentStage();
            if (stage != null) {
                stage.setScene(new Scene(root, 800, 600));
                stage.setTitle("Bank Dashboard");
            }
        } catch (IOException e) {
            showAlert("Error", "Cannot return to dashboard: " + e.getMessage());
        }
    }

    // Helper method to get current stage
    private Stage getCurrentStage() {
        try {
            // This would need to be adapted based on how you access the current stage
            // For now, we'll use a simple approach
            return new Stage();
        } catch (Exception e) {
            return null;
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