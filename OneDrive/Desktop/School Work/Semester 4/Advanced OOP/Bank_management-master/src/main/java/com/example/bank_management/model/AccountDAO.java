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

public class AccountDAO {
    public static void saveAccount(Account account) throws SQLException {
        String sql = "INSERT INTO ACCOUNT (ACCOUNT_NUMBER, BALANCE, OPEN_DATE, CUSTOMER_ID, ACCOUNT_TYPE, INTEREST_RATE, OVERDRAFT_LIMIT) " +
                "VALUES (?, ?, CURRENT_DATE, ?, ?, ?, ?)";
        System.out.println("Saving account function reached.");
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, account.getBalance());
            stmt.setInt(3, account.getCustomerId());
            if (account instanceof SavingsAccount) {
                stmt.setString(4, "savings");
                stmt.setDouble(5, ((SavingsAccount) account).getInterestRate());
                stmt.setNull(6, java.sql.Types.DOUBLE);
            } else if (account instanceof CheckingAccount) {
                stmt.setString(4, "checking");
                stmt.setNull(5, java.sql.Types.DOUBLE);
                stmt.setDouble(6, ((CheckingAccount) account).getOverdraftLimit());
            } else {
                throw new IllegalArgumentException("Unknown account type");
            }

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR SAVING ACCOUNT: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

