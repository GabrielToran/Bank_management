package com.example.bank_management.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Account {
    protected int accountId;
    protected String accountNumber;
    protected double balance;
    protected Date openDate;
    protected int customerId;
    protected List<Transaction> transactions;

    public Account() {
        this.transactions = new ArrayList<>();
        this.openDate = new Date();
    }

    public Account(int accountId, String accountNumber, double balance, int customerId) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.customerId = customerId;
        this.openDate = new Date();
        this.transactions = new ArrayList<>();
    }

    // Common methods
    public boolean deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            Transaction transaction = new Transaction(0, this.accountId, "DEPOSIT", amount, new Date());
            this.transactions.add(transaction);
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            Transaction transaction = new Transaction(0, this.accountId, "WITHDRAWAL", amount, new Date());
            this.transactions.add(transaction);
            return true;
        }
        return false;
    }

    // Getters and Setters
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public Date getOpenDate() { return openDate; }
    public void setOpenDate(Date openDate) { this.openDate = openDate; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
