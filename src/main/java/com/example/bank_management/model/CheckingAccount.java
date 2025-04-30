package com.example.bank_management.model;

import java.util.Date;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    // Constructors
    public CheckingAccount() {
        super();
    }

    public CheckingAccount(int accountId, int customerId, double balance, double overdraftLimit) {
        super(accountId, customerId, balance);
        this.overdraftLimit = overdraftLimit;
    }

    // Getters and Setters
    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (getBalance() + overdraftLimit < amount) {
            throw new IllegalStateException("Insufficient funds and overdraft limit exceeded");
        }

        setBalance(getBalance() - amount);
    }
}