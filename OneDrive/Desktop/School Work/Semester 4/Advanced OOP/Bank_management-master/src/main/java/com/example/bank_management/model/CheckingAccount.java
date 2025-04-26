package com.example.bank_management.model;

import java.util.Date;

public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount() {
        super();
        this.overdraftLimit = 100.0; // Default overdraft limit
    }

    public CheckingAccount(int accountId, String accountNumber, double balance, int customerId, double overdraftLimit) {
        super(accountId, accountNumber, balance, customerId);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= -overdraftLimit) {
            this.balance -= amount;
            Transaction transaction = new Transaction(0, this.accountId, "WITHDRAWAL", amount, new Date());
            this.transactions.add(transaction);
            return true;
        }
        return false;
    }

    // Getters and Setters
    public double getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(double overdraftLimit) { this.overdraftLimit = overdraftLimit; }

    @Override
    public String toString() {
        return "CheckingAccount{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", openDate=" + openDate +
                ", overdraftLimit=" + overdraftLimit +
                '}';
    }
}