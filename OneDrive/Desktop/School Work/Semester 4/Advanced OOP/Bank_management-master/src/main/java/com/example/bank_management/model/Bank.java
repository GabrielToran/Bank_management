package com.example.bank_management.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
    private static Bank instance;
    private Map<Integer, Customer> customers;
    private Map<Integer, Account> accounts;
    private Map<Integer, Transaction> transactions;
    private int nextCustomerId = 1;
    private int nextAccountId = 1;
    private int nextTransactionId = 1;

    private Bank() {
        customers = new HashMap<>();
        accounts = new HashMap<>();
        transactions = new HashMap<>();
    }

    public static synchronized Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    // Customer methods
    public Customer createCustomer(String firstName, String lastName, String email, String phone, String address) {
        int customerId = nextCustomerId++;
        Customer customer = new Customer(customerId, firstName, lastName, email, phone, address);
        customers.put(customerId, customer);
        return customer;
    }

    public Customer getCustomer(int customerId) {
        return customers.get(customerId);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public boolean updateCustomer(Customer customer) {
        if (customers.containsKey(customer.getCustomerId())) {
            customers.put(customer.getCustomerId(), customer);
            return true;
        }
        return false;
    }

    public boolean deleteCustomer(int customerId) {
        if (customers.containsKey(customerId)) {
            customers.remove(customerId);
            return true;
        }
        return false;
    }

    // Account methods
    public SavingsAccount createSavingsAccount(int customerId, double initialDeposit, double interestRate) {
        if (!customers.containsKey(customerId)) {
            return null;
        }

        int accountId = nextAccountId++;
        String accountNumber = "SAV" + String.format("%06d", accountId);

        SavingsAccount account = new SavingsAccount(accountId, accountNumber, initialDeposit, customerId, interestRate);
        accounts.put(accountId, account);

        Customer customer = customers.get(customerId);
        customer.addAccount(account);

        if (initialDeposit > 0) {
            int transactionId = nextTransactionId++;
            Transaction transaction = new Transaction(transactionId, accountId, "DEPOSIT", initialDeposit, account.getOpenDate(), "Initial deposit");
            transactions.put(transactionId, transaction);
            account.addTransaction(transaction);
        }

        return account;
    }

    public CheckingAccount createCheckingAccount(int customerId, double initialDeposit, double overdraftLimit) {
        if (!customers.containsKey(customerId)) {
            return null;
        }

        int accountId = nextAccountId++;
        String accountNumber = "CHK" + String.format("%06d", accountId);

        CheckingAccount account = new CheckingAccount(accountId, accountNumber, initialDeposit, customerId, overdraftLimit);
        accounts.put(accountId, account);

        Customer customer = customers.get(customerId);
        customer.addAccount(account);

        if (initialDeposit > 0) {
            int transactionId = nextTransactionId++;
            Transaction transaction = new Transaction(transactionId, accountId, "DEPOSIT", initialDeposit, account.getOpenDate(), "Initial deposit");
            transactions.put(transactionId, transaction);
            account.addTransaction(transaction);
        }

        return account;
    }

    public Account getAccount(int accountId) {
        return accounts.get(accountId);
    }

    public List<Account> getCustomerAccounts(int customerId) {
        List<Account> customerAccounts = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getCustomerId() == customerId) {
                customerAccounts.add(account);
            }
        }
        return customerAccounts;
    }

    public boolean deleteAccount(int accountId) {
        if (accounts.containsKey(accountId)) {
            Account account = accounts.get(accountId);
            Customer customer = customers.get(account.getCustomerId());

            if (customer != null) {
                customer.getAccounts().removeIf(a -> a.getAccountId() == accountId);
            }

            accounts.remove(accountId);
            return true;
        }
        return false;
    }

    // Transaction methods
    public Transaction createTransaction(int accountId, String type, double amount, String description) {
        if (!accounts.containsKey(accountId)) {
            return null;
        }

        Account account = accounts.get(accountId);
        boolean success = false;

        if ("DEPOSIT".equals(type)) {
            success = account.deposit(amount);
        } else if ("WITHDRAWAL".equals(type)) {
            success = account.withdraw(amount);
        }

        if (success) {
            int transactionId = nextTransactionId++;
            Transaction transaction = new Transaction(transactionId, accountId, type, amount, account.getOpenDate(), description);
            transactions.put(transactionId, transaction);
            return transaction;
        }

        return null;
    }

    public Transaction getTransaction(int transactionId) {
        return transactions.get(transactionId);
    }

    public List<Transaction> getAccountTransactions(int accountId) {
        List<Transaction> accountTransactions = new ArrayList<>();
        for (Transaction transaction : transactions.values()) {
            if (transaction.getAccountId() == accountId) {
                accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }
}