package com.example.demo.models;

public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(Customer customer, String branch) {
        super(customer, branch);
    }

    public SavingsAccount(String accountNumber, double balance, Customer customer, String branch) {
        super(accountNumber, balance, customer, branch);
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        throw new InsufficientFundsException("Savings account does not allow withdrawals");
    }

    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }

    @Override
    public String toString() {
        return "Savings Account [" + getAccountNumber() + "] Balance: BWP" + getBalance();
    }
}