package com.example.bank_management.model;
import com.example.bank_management.model.Transaction;
import com.example.bank_management.model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM TRANSACTION WHERE ACCOUNT_ID = ? ORDER BY TIMESTAMP DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(mapRowToTransaction(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT * FROM TRANSACTION WHERE TRANSACTION_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRowToTransaction(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean createTransaction(int accountId, String type, double amount, String description) {
        String insertTransactionSQL = "INSERT INTO TRANSACTION (ACCOUNT_ID, TYPE, AMOUNT, DESCRIPTION, TIMESTAMP) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        String updateBalanceSQL = "UPDATE ACCOUNT SET BALANCE = BALANCE + ? WHERE ACCOUNT_ID = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Apply correct sign based on type
            double signedAmount = type.equalsIgnoreCase("withdrawal") ? -amount : amount;

            // 1. Insert into TRANSACTION table
            try (PreparedStatement insertStmt = conn.prepareStatement(insertTransactionSQL)) {
                insertStmt.setInt(1, accountId);
                insertStmt.setString(2, type);
                insertStmt.setDouble(3, amount);
                insertStmt.setString(4, description);
                insertStmt.executeUpdate();
            }

            // 2. Update ACCOUNT balance
            try (PreparedStatement updateStmt = conn.prepareStatement(updateBalanceSQL)) {
                updateStmt.setDouble(1, signedAmount);
                updateStmt.setInt(2, accountId);
                updateStmt.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionId(rs.getInt("TRANSACTION_ID"));
        t.setAccountId(rs.getInt("ACCOUNT_ID"));
        t.setType(rs.getString("TYPE"));
        t.setAmount(rs.getDouble("AMOUNT"));
        t.setDescription(rs.getString("DESCRIPTION"));
        t.setTransactionDate(rs.getTimestamp("TIMESTAMP"));  //
        return t;
    }
}

