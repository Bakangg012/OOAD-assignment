package com.example.demo;

import com.example.demo.models.*;

import java.util.ArrayList;
import java.util.List;

public class BankingSystem {
    private static final List<Account> accounts = new ArrayList<>();

    public static void addAccount(Account a) {
        accounts.add(a);
    }

    public static List<Account> getAccounts() {
        return accounts;
    }

    // basic seed data
    static {
        SavingsAccount s = new SavingsAccount("S-1001", 500.0, 0.02);
        ChequeAccount c = new ChequeAccount("C-2001", 1000.0, 200.0);
        InvestmentAccount i = new InvestmentAccount("I-3001", 2000.0, 0.05);
        addAccount(s);
        addAccount(c);
        addAccount(i);
    }
}
