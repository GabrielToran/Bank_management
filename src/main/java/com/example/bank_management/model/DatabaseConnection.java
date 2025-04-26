package com.example.bank_management.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:h2:~/bankdb"; // Creates bankdb in your user home directory
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }
}
