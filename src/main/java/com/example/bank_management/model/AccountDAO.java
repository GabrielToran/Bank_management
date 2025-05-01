package com.example.bank_management.model;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.CheckingAccount;
import com.example.bank_management.model.SavingsAccount;
import com.example.bank_management.model.DatabaseConnection;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private static final String INSERT_ACCOUNT_SQL =
            "INSERT INTO ACCOUNT (ACCOUNT_NUMBER, BALANCE, OPEN_DATE, CUSTOMER_ID, ACCOUNT_TYPE, INTEREST_RATE, OVERDRAFT_LIMIT) " +
                    "VALUES (?, ?, CURRENT_DATE, ?, ?, ?, ?)";
    private static final String SELECT_ACCOUNT_BY_ID = "SELECT * FROM ACCOUNT WHERE ACCOUNT_ID = ?";
    private static final String SELECT_ACCOUNTS_BY_CUSTOMER_ID = "SELECT * FROM ACCOUNT WHERE CUSTOMER_ID = ?";
    private static final String DELETE_ACCOUNT_SQL = "DELETE FROM ACCOUNT WHERE ACCOUNT_ID = ?";

    // Save account to database (called after account is created in memory)
    public static boolean saveAccount(Account account) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_ACCOUNT_SQL, Statement.RETURN_GENERATED_KEYS)) {


            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, account.getBalance());
            stmt.setInt(3, account.getCustomerId());

            if (getAccountById(account.getCustomerId()) != null) {
                System.out.println("Account already exists.");
                return false; // Account already exists
            }

            if (account instanceof SavingsAccount) {
                stmt.setString(4, "savings");
                stmt.setDouble(5, ((SavingsAccount) account).getInterestRate());
                stmt.setNull(6, Types.DOUBLE);
            } else if (account instanceof CheckingAccount) {
                stmt.setString(4, "checking");
                stmt.setNull(5, Types.DOUBLE);
                stmt.setDouble(6, ((CheckingAccount) account).getOverdraftLimit());
            } else {
                throw new IllegalArgumentException("Unsupported account type.");
            }

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve account by account ID
    public static Account getAccountById(int accountId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SELECT_ACCOUNT_BY_ID)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String accountNumber = rs.getString("ACCOUNT_NUMBER");
                double balance = rs.getDouble("BALANCE");
                int customerId = rs.getInt("CUSTOMER_ID");
                String type = rs.getString("ACCOUNT_TYPE");
                double interestRate = rs.getDouble("INTEREST_RATE");
                double overdraftLimit = rs.getDouble("OVERDRAFT_LIMIT");

                if ("savings".equalsIgnoreCase(type)) {
                    return new SavingsAccount(accountId, accountNumber, balance, customerId, interestRate);
                } else if ("checking".equalsIgnoreCase(type)) {
                    return new CheckingAccount(accountId, accountNumber, balance, customerId, overdraftLimit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get all accounts for a specific customer
    public static List<Account> getAccountsByCustomerId(int customerId) {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(SELECT_ACCOUNTS_BY_CUSTOMER_ID)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int accountId = rs.getInt("ACCOUNT_ID");
                String accountNumber = rs.getString("ACCOUNT_NUMBER");
                double balance = rs.getDouble("BALANCE");
                String type = rs.getString("ACCOUNT_TYPE");
                double interestRate = rs.getDouble("INTEREST_RATE");
                double overdraftLimit = rs.getDouble("OVERDRAFT_LIMIT");

                if ("savings".equalsIgnoreCase(type)) {
                    accounts.add(new SavingsAccount(accountId, accountNumber, balance, customerId, interestRate));
                } else if ("checking".equalsIgnoreCase(type)) {
                    accounts.add(new CheckingAccount(accountId, accountNumber, balance, customerId, overdraftLimit));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    // Delete account by ID
    public boolean deleteAccountById(int accountId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(DELETE_ACCOUNT_SQL)) {

            stmt.setInt(1, accountId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}


