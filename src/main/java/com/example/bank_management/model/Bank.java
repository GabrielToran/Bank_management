package com.example.bank_management.model;
import java.util.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.AccountDAO;
import com.example.bank_management.model.Customer;
import com.example.bank_management.model.CustomerDAO;
import com.example.bank_management.model.Transaction;
import com.example.bank_management.model.TransactionDAO;

public class Bank {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    /**
     * Constructor with dependencies
     */
    public Bank(CustomerDAO customerDAO, AccountDAO accountDAO, TransactionDAO transactionDAO) {
        this.customerDAO = customerDAO;
        this.accountDAO = accountDAO;
        this.transactionDAO = transactionDAO;
    }

    /**
     * Gets the total number of customers in the bank
     */
    public int getCustomerCount() throws SQLException {
        return customerDAO.getCustomerCount();
    }

    /**
     * Gets the total number of accounts in the bank
     */
    public int getAccountCount() throws SQLException {
        return accountDAO.getCount();
    }

    /**
     * Gets the total balance of all accounts in the bank
     */
    public BigDecimal getTotalBalance() throws SQLException {
        // Use the new method that returns BigDecimal
        return new BigDecimal(accountDAO.getTotalBalance());
    }

    /**
     * Gets the total number of transactions in the bank
     */
    public int getTransactionCount() throws SQLException {
        return transactionDAO.getTransactionCount();
    }

    /**
     * Creates a new account for a customer
     */
    public Account createAccount(long customerId, String accountType, double initialDeposit,
                                 double interestRate, double overdraftLimit) throws SQLException {
        // Verify the customer exists
        Customer customer = customerDAO.getCustomerById((int)customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }

        // Create the appropriate account type
        Account account;
        if ("savings".equalsIgnoreCase(accountType)) {
            account = new SavingsAccount();
            ((SavingsAccount) account).setInterestRate(interestRate);
        } else if ("checking".equalsIgnoreCase(accountType)) {
            account = new CheckingAccount();
            ((CheckingAccount) account).setOverdraftLimit(overdraftLimit);
        } else {
            throw new IllegalArgumentException("Invalid account type");
        }

        // Set account properties
        account.setCustomerId((int)customerId);
        account.setBalance(0);

        // Save the account
        account = accountDAO.save(account);

        // Make initial deposit if specified
        if (initialDeposit > 0) {
            deposit(account.getAccountId(), initialDeposit, "Initial deposit");
        }

        return account;
    }

    /**
     * Makes a deposit to an account
     */
    public Transaction deposit(long accountId, double amount, String description) throws SQLException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = accountDAO.getAccountById((int) accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        // Update the account balance
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        accountDAO.update(account);

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setAccountId((int) accountId);
        transaction.setType("deposit");
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());
        transaction.setDescription(description);

        return transactionDAO.save(transaction);
    }

    /**
     * Makes a withdrawal from an account
     */
    public Transaction withdraw(long accountId, double amount, String description) throws SQLException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Account account = accountDAO.getAccountById((int) accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        double newBalance = account.getBalance() - amount;

        // Check if withdrawal is allowed
        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            double overdraftLimit = checkingAccount.getOverdraftLimit();

            // Check if withdrawal would exceed overdraft limit
            if (newBalance < -overdraftLimit) {
                throw new IllegalArgumentException("Withdrawal would exceed overdraft limit");
            }
        } else {
            // For savings accounts, don't allow negative balance
            if (newBalance < 0) {
                throw new IllegalArgumentException("Insufficient funds");
            }
        }

        // Update the account balance
        account.setBalance(newBalance);
        accountDAO.update(account);

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setAccountId((int) accountId);
        transaction.setType("withdraw");
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());
        transaction.setDescription(description);

        return transactionDAO.save(transaction);
    }

    /**
     * Transfers money between accounts
     */
    public void transfer(long fromAccountId, long toAccountId, double amount, String description)
            throws SQLException {
        // Start a transaction
        withdraw(fromAccountId, amount, "Transfer to account #" + toAccountId + ": " + description);
        deposit(toAccountId, amount, "Transfer from account #" + fromAccountId + ": " + description);
    }

    /**
     * Applies interest to all savings accounts
     */
    public void applyInterest() throws SQLException {
        List<SavingsAccount> savingsAccounts = accountDAO.findAllSavingsAccounts();

        for (SavingsAccount account : savingsAccounts) {
            double interestAmount = account.getBalance() * account.getInterestRate() / 12.0; // Monthly interest

            if (interestAmount > 0) {
                deposit(account.getAccountId(), interestAmount, "Monthly interest");
            }
        }
    }

    /**
     * Deletes an account if it has a zero balance
     */
    public void deleteAccount(long accountId) throws SQLException {
        Account account = accountDAO.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }

        if (account.getBalance() != 0) {
            throw new IllegalArgumentException("Cannot delete account with non-zero balance");
        }

        // Delete all transactions for this account first
        transactionDAO.deleteByAccountId(accountId);

        // Then delete the account
        accountDAO.delete(accountId);
    }
}