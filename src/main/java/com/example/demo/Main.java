package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Use absolute path with leading slash
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Banking System - Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}