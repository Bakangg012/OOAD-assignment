package com.example.demo.models;

public class InvestmentAccount extends Account {
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double MIN_OPENING_BALANCE = 500.0;

    public InvestmentAccount(Customer customer, String branch, double initialDeposit) {
        super(customer, branch);
        if (initialDeposit < MIN_OPENING_BALANCE) {
            throw new IllegalArgumentException("Investment account requires minimum BWP500 opening balance");
        }
        deposit(initialDeposit);
    }

    public InvestmentAccount(String accountNumber, double balance, Customer customer, String branch) {
        super(accountNumber, balance, customer, branch);
    }

    @Override
    public String getAccountType() {
        return "INVESTMENT";
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > getBalance()) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        setBalance(getBalance() - amount);
    }

    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }

    @Override
    public String toString() {
        return "Investment Account [" + getAccountNumber() + "] Balance: BWP" + getBalance();
    }
}