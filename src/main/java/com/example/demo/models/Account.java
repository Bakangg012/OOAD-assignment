package com.example.demo.models;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected Customer customer;
    protected String branch;

    public Account(Customer customer, String branch) {
        this.accountNumber = "ACC" + System.currentTimeMillis();
        this.balance = 0.0;
        this.customer = customer;
        this.branch = branch;
    }

    public Account(String accountNumber, double balance, Customer customer, String branch) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.customer = customer;
        this.branch = branch;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public abstract String getAccountType();
    public abstract void withdraw(double amount) throws InsufficientFundsException;
    public abstract double calculateInterest();

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public Customer getCustomer() { return customer; }
    public String getBranch() { return branch; }

    protected void setBalance(double balance) { this.balance = balance; }
}