package com.example.demo.services;

import com.example.demo.database.CustomerDAO;
import com.example.demo.database.AccountDAO;
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
    private AccountDAO accountDAO;
    private List<Account> accounts;

    public BankingServiceDAO() {
        // Initialize database and create tables
        DatabaseConnection.initializeDatabase();
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.accounts = new ArrayList<>();

        // Load all accounts from database
        loadAllAccounts();

        // Add sample data if no customers exist
        if (customerDAO.getAllCustomers().isEmpty()) {
            initializeSampleData();
        }

        System.out.println("BankingServiceDAO initialized with " + accounts.size() + " accounts");
    }

    private void loadAllAccounts() {
        accounts.clear();
        for (Customer customer : customerDAO.getAllCustomers()) {
            List<Account> customerAccounts = accountDAO.getAccountsByCustomerId(customer.getCustomerId());
            // Clear existing accounts and add fresh from database
            customer.getAccounts().clear();
            for (Account account : customerAccounts) {
                customer.addAccount(account);
                accounts.add(account);
                System.out.println("Loaded account: " + account.getAccountNumber() + " for " + customer.getFullName());
            }
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
        List<Customer> customers = customerDAO.getAllCustomers();
        // Load accounts for each customer
        for (Customer customer : customers) {
            List<Account> customerAccounts = accountDAO.getAccountsByCustomerId(customer.getCustomerId());
            customer.getAccounts().clear(); // Clear existing accounts
            customer.getAccounts().addAll(customerAccounts); // Add fresh from database
        }
        return customers;
    }

    // Account methods - UPDATED WITH DATABASE PERSISTENCE
    public Account openSavingsAccount(Customer customer, String branch, double initialDeposit) {
        try {
            Account account = new SavingsAccount(customer, branch);
            if (initialDeposit > 0) {
                account.deposit(initialDeposit);
            }

            // Save to database
            accountDAO.createAccount(account);

            // Add to customer and accounts list
            customer.addAccount(account);
            accounts.add(account);

            System.out.println("SUCCESS: Created Savings Account: " + account.getAccountNumber() + " for " + customer.getFullName());
            return account;
        } catch (Exception e) {
            System.out.println("ERROR creating savings account: " + e.getMessage());
            throw e;
        }
    }

    public Account openInvestmentAccount(Customer customer, String branch, double initialDeposit) {
        try {
            if (initialDeposit < 500.0) {
                throw new IllegalArgumentException("Investment account requires minimum BWP 500.00");
            }

            Account account = new InvestmentAccount(customer, branch, initialDeposit);

            // Save to database
            accountDAO.createAccount(account);

            // Add to customer and accounts list
            customer.addAccount(account);
            accounts.add(account);

            System.out.println("SUCCESS: Created Investment Account: " + account.getAccountNumber() + " for " + customer.getFullName());
            return account;
        } catch (Exception e) {
            System.out.println("ERROR creating investment account: " + e.getMessage());
            throw e;
        }
    }

    public Account openChequeAccount(Customer customer, String branch, String employer, String companyAddress, double initialDeposit) {
        try {
            Account account = new ChequeAccount(customer, branch, employer, companyAddress);
            if (initialDeposit > 0) {
                account.deposit(initialDeposit);
            }

            // Save to database
            accountDAO.createAccount(account);

            // Add to customer and accounts list
            customer.addAccount(account);
            accounts.add(account);

            System.out.println("SUCCESS: Created Cheque Account: " + account.getAccountNumber() + " for " + customer.getFullName());
            return account;
        } catch (Exception e) {
            System.out.println("ERROR creating cheque account: " + e.getMessage());
            throw e;
        }
    }

    // Transaction methods - UPDATED WITH DATABASE PERSISTENCE
    public void deposit(Account account, double amount) {
        try {
            account.deposit(amount);
            // Update balance in database
            accountDAO.updateAccountBalance(account.getAccountNumber(), account.getBalance());
            System.out.println("SUCCESS: Deposited BWP" + amount + " to account " + account.getAccountNumber());
        } catch (Exception e) {
            System.out.println("ERROR depositing: " + e.getMessage());
            throw e;
        }
    }

    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        try {
            account.withdraw(amount);
            // Update balance in database
            accountDAO.updateAccountBalance(account.getAccountNumber(), account.getBalance());
            System.out.println("SUCCESS: Withdrawn BWP" + amount + " from account " + account.getAccountNumber());
        } catch (Exception e) {
            System.out.println("ERROR withdrawing: " + e.getMessage());
            throw e;
        }
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts);
    }

    public List<Account> getCustomerAccounts(Customer customer) {
        // Always get fresh from database
        return accountDAO.getAccountsByCustomerId(customer.getCustomerId());
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
                    // Update balance in database
                    accountDAO.updateAccountBalance(account.getAccountNumber(), account.getBalance());
                }
            }
        }
    }

    private void initializeSampleData() {
        try {
            System.out.println("Initializing sample data...");

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
            e.printStackTrace();
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