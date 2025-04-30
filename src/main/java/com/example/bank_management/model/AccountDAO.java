package com.example.bank_management.model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AccountDAO {

    // Create a new account
    public boolean createAccount(Account account, String accountType) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert into base accounts table
            String sql = "INSERT INTO accounts (customer_id, balance, account_type) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, account.getCustomerId());
            stmt.setDouble(2, account.getBalance());


            if (account instanceof CheckingAccount) {
                accountType = "checking";
            } else if (account instanceof SavingsAccount) {
                accountType = "savings";
            }
            stmt.setString(3, accountType);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating account failed, no rows affected.");
            }

            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int accountId = generatedKeys.getInt(1);
                account.setAccountId(accountId);

                // Insert into specific account type tables
                if (account instanceof CheckingAccount) {
                    sql = "INSERT INTO checking_accounts (account_id, overdraft_limit) VALUES (?, ?)";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, accountId);
                    stmt.setDouble(2, ((CheckingAccount) account).getOverdraftLimit());
                } else if (account instanceof SavingsAccount) {
                    sql = "INSERT INTO savings_accounts (account_id, interest_rate) VALUES (?, ?)";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, accountId);
                    stmt.setDouble(2, ((SavingsAccount) account).getInterestRate());
                }

                stmt.executeUpdate();
                conn.commit();
                return true;
            } else {
                throw new SQLException("Creating account failed, no ID obtained.");
            }

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    // Get account by ID
    public Account getAccountById(int accountId) {
        String sql = "SELECT a.*, c.overdraft_limit, s.interest_rate " +
                "FROM accounts a " +
                "LEFT JOIN checking_accounts c ON a.account_id = c.account_id " +
                "LEFT JOIN savings_accounts s ON a.account_id = s.account_id " +
                "WHERE a.account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Get all accounts by customer ID
    public List<Account> getAccountsByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.*, c.overdraft_limit, s.interest_rate " +
                "FROM accounts a " +
                "LEFT JOIN checking_accounts c ON a.account_id = c.account_id " +
                "LEFT JOIN savings_accounts s ON a.account_id = s.account_id " +
                "WHERE a.customer_id = ? " +
                "ORDER BY a.account_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    // Get all accounts
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.*, c.overdraft_limit, s.interest_rate " +
                "FROM accounts a " +
                "LEFT JOIN checking_accounts c ON a.account_id = c.account_id " +
                "LEFT JOIN savings_accounts s ON a.account_id = s.account_id " +
                "ORDER BY a.account_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    // Update account balance
    public boolean updateAccountBalance(int accountId, double balance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, balance);
            stmt.setInt(2, accountId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update full account - method to support Bank class
    public boolean updateAccount(Account account) {
        return updateAccountBalance(account.getAccountId(), account.getBalance());
    }

    // Update method to support the Bank class
    public boolean update(Account account) {
        return updateAccount(account);
    }

    // Delete account
    public boolean deleteAccount(int accountId) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // First, check if there are any transactions for this account
            String checkTransactions = "SELECT COUNT(*) FROM transactions WHERE account_id = ?";
            stmt = conn.prepareStatement(checkTransactions);
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // There are transactions, cannot delete
                conn.rollback();
                return false;
            }

            // Delete from specific account tables first
            String sql = "DELETE FROM checking_accounts WHERE account_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.executeUpdate();

            sql = "DELETE FROM savings_accounts WHERE account_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            stmt.executeUpdate();

            // Then delete from main accounts table
            sql = "DELETE FROM accounts WHERE account_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, accountId);
            int result = stmt.executeUpdate();

            conn.commit();
            return result > 0;

        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Delete method to support the Bank class
    public boolean delete(long accountId) {
        return deleteAccount((int) accountId);
    }
    // Get total balance of all accounts
    public BigDecimal getTotalBalance() {
        String sql = "SELECT SUM(balance) FROM accounts";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    } // Get total balance method to support Bank class
    public BigDecimal getTotalBalanceAsBigDecimal() {
        return new BigDecimal(getTotalBalance());
    }



    // Get account count
    public int getAccountCount() {
        String sql = "SELECT COUNT(*) FROM accounts";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Get count method to support Bank class
    public int getCount() {
        return getAccountCount();
    }

    // Find by id method to support Bank class
    public Account findById(long accountId) {
        return getAccountById((int) accountId);
    }

    // Method to support Bank class
    public Account save(Account account) {
        if (account.getAccountId() == 0) {
            String accountType = account instanceof CheckingAccount ? "checking" : "savings";
            if (createAccount(account, accountType)) {
                return account;
            }
        } else {
            if (updateAccount(account)) {
                return account;
            }
        }
        return null;
    }

    // Method to support Bank class
    public List<SavingsAccount> findAllSavingsAccounts() {
        List<SavingsAccount> savingsAccounts = new ArrayList<>();
        String sql = "SELECT a.*, s.interest_rate " +
                "FROM accounts a " +
                "JOIN savings_accounts s ON a.account_id = s.account_id " +
                "WHERE a.account_type = 'savings' " +
                "ORDER BY a.account_id";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                SavingsAccount account = new SavingsAccount(
                        rs.getInt("account_id"),
                        rs.getInt("customer_id"),
                        rs.getDouble("balance"),
                        rs.getDouble("interest_rate")
                );
                savingsAccounts.add(account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return savingsAccounts;
    }

    // Helper method to map ResultSet to Account object
    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = null;
        int accountId = rs.getInt("account_id");
        int customerId = rs.getInt("customer_id");
        double balance = rs.getDouble("balance");
        String accountType = rs.getString("account_type");

        if ("checking".equals(accountType)) {
            double overdraftLimit = rs.getDouble("overdraft_limit");
            account = new CheckingAccount(accountId, customerId, balance, overdraftLimit);
        } else if ("savings".equals(accountType)) {
            double interestRate = rs.getDouble("interest_rate");
            account = new SavingsAccount(accountId, customerId, balance, interestRate);
        }

        return account;
    }
}