package com.example.bank_management.model;

public abstract class Account {
    private int accountId;
    private int customerId;
    private double balance;

    // Constructor
    public Account() {}

    public Account(int accountId, int customerId, double balance) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.balance = balance;
    }

    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // Business methods
    public boolean deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
        return false;
    }

    public abstract void withdraw(double amount);

    // Helper method for JSP
    public boolean hasProperty(String propertyName) {
        try {
            this.getClass().getMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}