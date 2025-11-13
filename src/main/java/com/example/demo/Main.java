package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLoginScene();
    }

    public static void showLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/demo/login.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            primaryStage.setTitle("Banking System - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error loading login scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showRegisterScene() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/demo/register.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            primaryStage.setTitle("Banking System - Register");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Error loading register scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showDashboardScene() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/demo/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 700);
            primaryStage.setTitle("Banking System - Dashboard");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
        } catch (Exception e) {
            System.err.println("Error loading dashboard scene: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showNewAccountScene() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/example/demo/new-account.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            primaryStage.setTitle("Banking System - Open New Account");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Error loading new account scene: " + e.getMessage());
            e.printStackTrace();
            // Temporary: Show message if file doesn't exist
            System.out.println("Create new-account.fxml file for this feature");
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting Banking System Application...");
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}