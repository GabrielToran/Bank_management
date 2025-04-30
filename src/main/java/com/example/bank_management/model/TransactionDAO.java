package com.example.bank_management.model;
import com.example.bank_management.model.Transaction;
import com.example.bank_management.model.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class TransactionDAO {

    public boolean createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_id, type, amount, date, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, transaction.getAccountId());
            stmt.setString(2, transaction.getType());
            stmt.setDouble(3, transaction.getAmount());

            // Handle both LocalDate and LocalDateTime
            if (transaction.getDate() instanceof LocalDate) {
                stmt.setDate(4, java.sql.Date.valueOf(((LocalDate)transaction.getDate())));
            } else {
                stmt.setDate(4, java.sql.Date.valueOf((LocalDate)transaction.getDate()));
            }

            stmt.setString(5, transaction.getDescription());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaction.setTransactionId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractTransactionFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Added missing findByAccountId method used in TransactionServlet
    public List<Transaction> findByAccountId(long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY date DESC, transaction_id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(extractTransactionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByAccountId(int accountId, String typeFilter) {
        List<Transaction> transactions = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM transactions WHERE account_id = ?");

        if (typeFilter != null && !typeFilter.isEmpty()) {
            sqlBuilder.append(" AND type = ?");
        }

        sqlBuilder.append(" ORDER BY date DESC, transaction_id DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            stmt.setInt(1, accountId);
            if (typeFilter != null && !typeFilter.isEmpty()) {
                stmt.setString(2, typeFilter);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(extractTransactionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public boolean deleteTransactionsByAccountId(int accountId) {
        String sql = "DELETE FROM transactions WHERE account_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getTransactionCount() {
        String sql = "SELECT COUNT(*) FROM transactions";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Added method to support the Bank class
    public boolean deleteByAccountId(long accountId) {
        return deleteTransactionsByAccountId((int) accountId);
    }

    // Added method to support the Bank class
    public Transaction save(Transaction transaction) {
        if (createTransaction(transaction)) {
            return transaction;
        }
        return null;
    }

    // Added for Bank.java compatibility
    public int getCount() throws SQLException {
        return getTransactionCount();
    }

    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction(
                rs.getInt("transaction_id"),
                rs.getInt("account_id"),
                rs.getString("type"),
                rs.getDouble("amount"),
                rs.getDate("date").toLocalDate(),
                rs.getString("description")
        );
        return transaction;
    }
}