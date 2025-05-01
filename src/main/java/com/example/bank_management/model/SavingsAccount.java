package com.example.bank_management.model;
import java.util.Date;
public class SavingsAccount extends Account  {
    private double interestRate;
    private Date lastInterestDate;

    public SavingsAccount() {
        super();
        this.interestRate = 0.01; // 1% default interest rate
        this.lastInterestDate = new Date();
    }

    public SavingsAccount(int accountId, String accountNumber, double balance, int customerId, double interestRate) {
        super(accountId, accountNumber, balance, customerId);
        this.interestRate = interestRate;
        this.lastInterestDate = new Date();
    }

    public void applyInterest() {
        double interest = this.balance * this.interestRate;
        this.balance += interest;
        this.lastInterestDate = new Date();

        Transaction transaction = new Transaction(0, this.accountId, "INTEREST", interest, new Date());
        this.transactions.add(transaction);
    }

    // Getters and Setters
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }

    public Date getLastInterestDate() { return lastInterestDate; }
    public void setLastInterestDate(Date lastInterestDate) { this.lastInterestDate = lastInterestDate; }

    @Override
    public String toString() {
        return "SavingsAccount{" +
                "accountId=" + accountId +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", openDate=" + openDate +
                ", interestRate=" + interestRate +
                '}';
    }

    @Override
    public String getAccountType() {
        return "Savings";
    }

}
