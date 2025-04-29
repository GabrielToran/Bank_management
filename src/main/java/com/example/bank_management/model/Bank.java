package com.example.bank_management.model;

import java.util.*;

import java.util.stream.Collectors;
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
    /** Deposit money into an account and record the transaction. */
    public boolean deposit(int accountId, double amount) {
        Account acct = accounts.get(accountId);
        if (acct == null || amount <= 0) return false;

        // 1) Update balance
        acct.setBalance(acct.getBalance() + amount);

        // 2) Create and store transaction
        int txId = nextTransactionId();
        Transaction t = new Transaction(
                txId,
                accountId,
                "deposit",
                amount,
                new Date()
        );
        transactions.put(txId, t);
        return true;
    }

    /** Withdraw money (respecting overdraft on checking) and record the transaction. */
    public boolean withdraw(int accountId, double amount) {
        Account acct = accounts.get(accountId);
        if (acct == null || amount <= 0) return false;

        // determine allowed funds
        double allowed = acct.getBalance();
        if (acct instanceof CheckingAccount) {
            allowed += ((CheckingAccount)acct).getOverdraftLimit();
        }
        if (amount > allowed) return false;

        // 1) Update balance
        acct.setBalance(acct.getBalance() - amount);

        // 2) Create and store transaction
        int txId = nextTransactionId();
        Transaction t = new Transaction(
                txId,
                accountId,
                "withdraw",
                amount,
                new Date()
        );
        transactions.put(txId, t);
        return true;
    }

    /** Return all transactions for a given account. */
    public List<Transaction> getAccountTransactions(int accountId) {
        return transactions.values().stream()
                .filter(t -> t.getAccountId() == accountId)

                .collect(Collectors.toList());
    }

    /** Helper: find the next unique transaction ID. */
    private int nextTransactionId() {
        return transactions.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0) + 1;
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

    public List<Account> getAllAccounts() {
        // Return a fresh list so callers can’t mutate your internal map
        return new ArrayList<>(accounts.values());
    }
    public List<Transaction> getAllTransactions() {
        // Same idea—wrap the values in a new list
        return new ArrayList<>(transactions.values());
    }


}