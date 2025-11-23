package com.example.demo.models;

public class ChequeAccount extends Account {
    private String employer;
    private String companyAddress;

    public ChequeAccount(Customer customer, String branch, String employer, String companyAddress) {
        super(customer, branch);
        this.employer = employer;
        this.companyAddress = companyAddress;
    }

    public ChequeAccount(String accountNumber, double balance, Customer customer, String branch, String employer, String companyAddress) {
        super(accountNumber, balance, customer, branch);
        this.employer = employer;
        this.companyAddress = companyAddress;
    }

    @Override
    public String getAccountType() {
        return "CHEQUE";
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
        return 0; // Cheque accounts don't pay interest
    }

    public String getEmployer() { return employer; }
    public String getCompanyAddress() { return companyAddress; }

    @Override
    public String toString() {
        return "Cheque Account [" + getAccountNumber() + "] Balance: BWP" + getBalance() + " | Employer: " + employer;
    }
}