package com.example.bank_management.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for database connection management.
 */
public class DatabaseConnection {
    // JDBC URL, username and password for the database
    private static final String JDBC_URL = "jdbc:h2:~/bankdp";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "";

    // JDBC driver name
    private static final String JDBC_DRIVER = "org.h2.Driver";

    static {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
            
            // Initialize database schema
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
                 Statement stmt = conn.createStatement()) {
                
                // Create customers table
                stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                    "customer_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "phone VARCHAR(20), " +
                    "address VARCHAR(200))");

                // Create accounts table
                stmt.execute("CREATE TABLE IF NOT EXISTS accounts (" +
                    "account_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "customer_id INT NOT NULL, " +
                    "balance DECIMAL(15,2) DEFAULT 0.00, " +
                    "account_type VARCHAR(20) NOT NULL, " +
                    "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))");

                // Create checking_accounts table
                stmt.execute("CREATE TABLE IF NOT EXISTS checking_accounts (" +
                    "account_id INT PRIMARY KEY, " +
                    "overdraft_limit DECIMAL(15,2) DEFAULT 0.00, " +
                    "FOREIGN KEY (account_id) REFERENCES accounts(account_id))");

                // Create savings_accounts table
                stmt.execute("CREATE TABLE IF NOT EXISTS savings_accounts (" +
                    "account_id INT PRIMARY KEY, " +
                    "interest_rate DECIMAL(5,2) DEFAULT 0.00, " +
                    "FOREIGN KEY (account_id) REFERENCES accounts(account_id))");

                // Create transactions table
                stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "transaction_id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "account_id INT NOT NULL, " +
                    "transaction_type VARCHAR(20) NOT NULL, " +
                    "amount DECIMAL(15,2) NOT NULL, " +
                    "transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "description VARCHAR(200), " +
                    "FOREIGN KEY (account_id) REFERENCES accounts(account_id))");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load JDBC driver: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database: " + e.getMessage());
        }
    }

    /**
     * Gets a connection to the database.
     *
     * @return Connection object to the database
     * @throws SQLException If a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    /**
     * Closes a connection safely.
     *
     * @param connection The connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}