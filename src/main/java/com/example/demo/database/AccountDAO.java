package com.example.demo.database;

import com.example.demo.models.Account;
import com.example.demo.models.Customer;
import com.example.demo.models.SavingsAccount;
import com.example.demo.models.InvestmentAccount;
import com.example.demo.models.ChequeAccount;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountDAO {

    public void createAccount(Account account) {
        String sql = "INSERT INTO accounts (id, customer_id, account_type, account_number, balance, branch, employer, company_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, UUID.randomUUID().toString());
            pstmt.setString(2, account.getCustomer().getCustomerId());
            pstmt.setString(3, account.getAccountType());
            pstmt.setString(4, account.getAccountNumber());
            pstmt.setDouble(5, account.getBalance());
            pstmt.setString(6, account.getBranch());

            // Set employer and company address for cheque accounts
            if (account instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) account;
                pstmt.setString(7, chequeAccount.getEmployer());
                pstmt.setString(8, chequeAccount.getCompanyAddress());
            } else {
                pstmt.setString(7, null);
                pstmt.setString(8, null);
            }

            pstmt.executeUpdate();
            System.out.println("✅ Account saved to database: " + account.getAccountNumber() + " (" + account.getAccountType() + ")");

        } catch (SQLException e) {
            System.out.println("❌ Error saving account: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Account> getAccountsByCustomerId(String customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Account account = resultSetToAccount(rs);
                if (account != null) {
                    accounts.add(account);
                }
            }

            System.out.println("✅ Loaded " + accounts.size() + " accounts for customer " + customerId);

        } catch (SQLException e) {
            System.out.println("❌ Error getting accounts for customer " + customerId + ": " + e.getMessage());
        }
        return accounts;
    }

    public void updateAccountBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNumber);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✅ Updated balance for account " + accountNumber + " to BWP" + newBalance);
            } else {
                System.out.println("❌ No account found with number: " + accountNumber);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error updating account balance: " + e.getMessage());
        }
    }

    private Account resultSetToAccount(ResultSet rs) throws SQLException {
        String accountNumber = rs.getString("account_number");
        String customerId = rs.getString("customer_id");
        String accountType = rs.getString("account_type");
        double balance = rs.getDouble("balance");
        String branch = rs.getString("branch");
        String employer = rs.getString("employer");
        String companyAddress = rs.getString("company_address");

        // Get the customer object
        CustomerDAO customerDAO = new CustomerDAO();
        Customer customer = customerDAO.findCustomerById(customerId);

        if (customer == null) {
            System.out.println("❌ Customer not found for account: " + accountNumber);
            return null;
        }

        Account account;
        try {
            // Use the constructors that preserve account number and balance from database
            switch (accountType.toUpperCase()) {
                case "SAVINGS":
                    account = new SavingsAccount(accountNumber, balance, customer, branch);
                    break;
                case "INVESTMENT":
                    account = new InvestmentAccount(accountNumber, balance, customer, branch);
                    break;
                case "CHEQUE":
                    account = new ChequeAccount(accountNumber, balance, customer, branch, employer, companyAddress);
                    break;
                default:
                    System.out.println("❌ Unknown account type: " + accountType);
                    return null;
            }

            System.out.println("✅ Loaded account from DB: " + accountNumber + " | Balance: BWP" + balance + " | Type: " + accountType);
            return account;

        } catch (Exception e) {
            System.out.println("❌ Error creating account from result set: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Method to get all accounts (for debugging)
    public List<Account> getAllAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Account account = resultSetToAccount(rs);
                if (account != null) {
                    allAccounts.add(account);
                }
            }

            System.out.println("✅ Loaded " + allAccounts.size() + " total accounts from database");

        } catch (SQLException e) {
            System.out.println("❌ Error getting all accounts: " + e.getMessage());
        }
        return allAccounts;
    }
}