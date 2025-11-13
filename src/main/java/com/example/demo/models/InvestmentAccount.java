package com.example.demo.models;

public class InvestmentAccount extends Account {
    private double returnRate;

    public InvestmentAccount(String accountNumber, double balance, double returnRate) {
        super(accountNumber, balance);
        this.returnRate = returnRate;
    }

    public double getReturnRate() {
        return returnRate;
    }
}
