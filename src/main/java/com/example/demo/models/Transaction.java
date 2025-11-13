package com.example.demo.models;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private double amount;
    private String type;
    private LocalDateTime timestamp;

    public Transaction(String id, double amount, String type) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
