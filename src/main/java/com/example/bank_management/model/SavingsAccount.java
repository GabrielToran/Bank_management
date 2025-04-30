package com.example.bank_management.model;
import java.util.Date;
public class SavingsAccount extends Account {
    private double interestRate;

    // Constructors
    public SavingsAccount() {
        super();
    }

    public SavingsAccount(int accountId, int customerId, double balance, double interestRate) {
        super(accountId, customerId, balance);
        this.interestRate = interestRate;
    }

    // Getters and Setters
    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        if (getBalance() < amount) {
            throw new IllegalStateException("Insufficient funds");
        }

        setBalance(getBalance() - amount);
    }

    // Apply interest
    public void applyInterest() {
        double interest = getBalance() * interestRate;
        deposit(interest);
    }
}