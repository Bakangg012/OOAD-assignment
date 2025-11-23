package com.example.demo.services;

import com.example.demo.models.Account;
import com.example.demo.models.SavingsAccount;
import com.example.demo.models.InvestmentAccount;
import com.example.demo.models.ChequeAccount;
import com.example.demo.models.Customer;
import com.example.demo.models.InsufficientFundsException;
import java.util.ArrayList;
import java.util.List;

public class BankingService {
    private List<Customer> customers;
    private List<Account> accounts;

    public BankingService() {
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
        initializeSampleData();
    }

    // Customer management
    public Customer createCustomer(String firstName, String surname, String address, String email, String phone) {
        Customer customer = new Customer(firstName, surname, address, email, phone);
        customers.add(customer);
        return customer;
    }

    // ADD THIS METHOD - Required by CustomerController
    public boolean updateCustomer(Customer customer) {
        // Update customer in the list
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId().equals(customer.getCustomerId())) {
                customers.set(i, customer);
                return true;
            }
        }
        return false;
    }

    // Account creation - USING YOUR EXACT CONSTRUCTORS
    public Account openSavingsAccount(Customer customer, String branch, double initialDeposit) {
        Account account = new SavingsAccount(customer, branch);
        if (initialDeposit > 0) {
            account.deposit(initialDeposit);
        }
        customer.addAccount(account);
        accounts.add(account);
        return account;
    }

    public Account openInvestmentAccount(Customer customer, String branch, double initialDeposit) {
        Account account = new InvestmentAccount(customer, branch, initialDeposit);
        customer.addAccount(account);
        accounts.add(account);
        return account;
    }

    public Account openChequeAccount(Customer customer, String branch, String employer, String companyAddress, double initialDeposit) {
        Account account = new ChequeAccount(customer, branch, employer, companyAddress);
        if (initialDeposit > 0) {
            account.deposit(initialDeposit);
        }
        customer.addAccount(account);
        accounts.add(account);
        return account;
    }

    // Transaction methods
    public void deposit(Account account, double amount) {
        account.deposit(amount);
    }

    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        account.withdraw(amount);
    }

    // Getters
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    public List<Account> getCustomerAccounts(Customer customer) {
        return customer.getAccounts();
    }

    // Find customer by ID
    public Customer findCustomerById(String customerId) {
        for (Customer customer : getAllCustomers()) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

    // Process monthly interest
    public void processMonthlyInterest() {
        for (Account account : accounts) {
            if (account instanceof SavingsAccount || account instanceof InvestmentAccount) {
                double interest = account.calculateInterest();
                if (interest > 0) {
                    account.deposit(interest);
                }
            }
        }
    }

    // Initialize with sample data - USING YOUR CONSTRUCTORS
    private void initializeSampleData() {
        try {
            // Create sample customers
            Customer john = createCustomer("John", "Doe", "Gaborone", "john@email.com", "71123456");
            Customer jane = createCustomer("Jane", "Smith", "Francistown", "jane@email.com", "72123456");

            // Create accounts using YOUR exact constructors
            openSavingsAccount(john, "Main Branch", 1000.0);
            openInvestmentAccount(john, "Main Branch", 600.0);
            openChequeAccount(jane, "Francistown Branch", "ABC Company", "Francistown", 2000.0);

        } catch (Exception e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }

    // Test method
    public void displayAllData() {
        System.out.println("=== BANKING SYSTEM DATA ===");

        for (Customer customer : getAllCustomers()) {
            System.out.println("Customer: " + customer.getFullName() + " (ID: " + customer.getCustomerId() + ")");

            for (Account account : customer.getAccounts()) {
                System.out.println("  - " + account.getAccountType() + " Account: " +
                        account.getAccountNumber() + " | Balance: BWP" + account.getBalance());
            }
        }
        System.out.println("=== DATA DISPLAY COMPLETE ===");
    }
}