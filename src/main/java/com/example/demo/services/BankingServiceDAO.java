package com.example.demo.services;

import com.example.demo.database.CustomerDAO;
import com.example.demo.database.DatabaseConnection;
import com.example.demo.models.Customer;
import com.example.demo.models.Account;
import com.example.demo.models.SavingsAccount;
import com.example.demo.models.InvestmentAccount;
import com.example.demo.models.ChequeAccount;
import com.example.demo.models.InsufficientFundsException;
import java.util.List;
import java.util.ArrayList;

public class BankingServiceDAO {
    private CustomerDAO customerDAO;
    private List<Account> accounts;

    public BankingServiceDAO() {
        // Initialize database and create tables
        DatabaseConnection.initializeDatabase();
        this.customerDAO = new CustomerDAO();
        this.accounts = new ArrayList<>();

        // Add sample data if no customers exist
        if (customerDAO.getAllCustomers().isEmpty()) {
            initializeSampleData();
        }
    }

    // Customer management
    public Customer createCustomer(String firstName, String surname, String address, String email, String phone, String password) {
        Customer customer = new Customer(firstName, surname, address, email, phone, password);
        customerDAO.createCustomer(customer);
        return customer;
    }

    public Customer findCustomerByName(String fullName) {
        return customerDAO.findCustomerByName(fullName);
    }

    public boolean updateCustomer(Customer customer) {
        customerDAO.updateCustomer(customer);
        return true;
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.getAllCustomers();
    }

    // Account methods
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

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    public List<Account> getCustomerAccounts(Customer customer) {
        return customer.getAccounts();
    }

    public Customer findCustomerById(String customerId) {
        for (Customer customer : getAllCustomers()) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

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

    private void initializeSampleData() {
        try {
            // Create sample customers
            Customer john = createCustomer("John", "Doe", "Gaborone", "john@email.com", "71123456", "password123");
            Customer jane = createCustomer("Jane", "Smith", "Francistown", "jane@email.com", "72123456", "password123");

            // Create sample accounts
            openSavingsAccount(john, "Main Branch", 1000.0);
            openInvestmentAccount(john, "Main Branch", 600.0);
            openChequeAccount(jane, "Francistown Branch", "ABC Company", "Francistown", 2000.0);

            System.out.println("Sample data initialized successfully");

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