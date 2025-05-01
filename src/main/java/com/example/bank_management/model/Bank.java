package com.example.bank_management.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;
import java.util.List;

public class Bank {
    private static Bank instance;

    private Bank() {
        // No in-memory maps — everything goes through DAO
    }

    public static synchronized Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    // Customer methods
    public Customer createCustomer(String firstName, String lastName, String email, String phone, String address) {
        return new CustomerDAO().createAndSaveCustomer(firstName, lastName, email, phone, address);
    }

    public Customer getCustomer(int customerId) {
        return new CustomerDAO().getCustomerById(customerId);
    }

    public List<Customer> getAllCustomers() {
        return new CustomerDAO().getAllCustomers();
    }

    public boolean updateCustomer(Customer customer) {
        // You can implement this in CustomerDAO if needed.
        throw new UnsupportedOperationException("Update not implemented yet.");
    }

    public boolean deleteCustomer(int customerId) {
        // You can implement this in CustomerDAO if needed.
        throw new UnsupportedOperationException("Delete not implemented yet.");
    }

    // Account methods
    public SavingsAccount createSavingsAccount(int customerId, double initialDeposit, double interestRate) {
        Customer customer = getCustomer(customerId);
        if (customer == null) return null;

        int accountId = -1; // Placeholder, real ID comes from DB
        String accountNumber = "SAV" + String.format("%06d", (int) (Math.random() * 1000000));
        SavingsAccount account = new SavingsAccount(accountId, accountNumber, initialDeposit, customerId, interestRate);

        AccountDAO.saveAccount(account);

        return account;
    }

    public CheckingAccount createCheckingAccount(int customerId, double initialDeposit, double overdraftLimit) {
        Customer customer = getCustomer(customerId);
        if (customer == null) return null;

        int accountId = -1; // Placeholder
        String accountNumber = "CHK" + String.format("%06d", (int) (Math.random() * 1000000));
        CheckingAccount account = new CheckingAccount(accountId, accountNumber, initialDeposit, customerId, overdraftLimit);

        AccountDAO.saveAccount(account);

        return account;
    }

    // You'll need to add this method to AccountDAO
    public Account getAccount(int accountId) {
        return AccountDAO.getAccountById(accountId);
    }

    // You'll need to add this method to AccountDAO
    public List<Account> getCustomerAccounts(int customerId) {
        return AccountDAO.getAccountsByCustomerId(customerId);
    }

    public boolean deleteAccount(int accountId) {
        // Not implemented yet in DAO
        throw new UnsupportedOperationException("Delete not implemented yet.");
    }


    // Transaction methods would follow a similar DAO pattern — not implemented yet
}
