package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting Banking Application...");

        // Debug: Check the exact path
        URL fxmlUrl = getClass().getResource("/com/example/demo/login.fxml");
        System.out.println("FXML URL: " + fxmlUrl);

        if (fxmlUrl == null) {
            throw new RuntimeException("Cannot find login.fxml at: /com/example/demo/login.fxml");
        }

        Parent root = FXMLLoader.load(fxmlUrl);
        primaryStage.setTitle("Banking System - Login");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        System.out.println("Launching Banking Application...");
        launch(args);
    }
}