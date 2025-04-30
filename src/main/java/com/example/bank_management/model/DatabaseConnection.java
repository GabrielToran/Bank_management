package com.example.bank_management.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for database connection management.
 */
public class DatabaseConnection {
    // JDBC URL, username and password for the database
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/bank_management";
    private static final String JDBC_USER = "bank_user";
    private static final String JDBC_PASSWORD = "bank_password";

    // JDBC driver name
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load JDBC driver: " + e.getMessage());
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