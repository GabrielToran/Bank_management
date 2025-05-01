package com.example.bank_management.model;

import java.sql.*;
import java.util.*;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:h2:tcp://localhost/~/bankdb";
    private static final String JDBC_USERNAME = "sa";
    private static final String JDBC_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver"); // Add this line to force loading
        } catch (ClassNotFoundException e) {
            throw new SQLException("Unable to load H2 JDBC Driver", e);
        }
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    // Inside DatabaseConnection.java
    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT CUSTOMER_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE, ADDRESS FROM CUSTOMER";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(resultSet.getInt("CUSTOMER_ID"));
                customer.setFirstName(resultSet.getString("FIRST_NAME"));
                customer.setFirstName(resultSet.getString("LAST_NAME"));// or split into firstName/lastName if needed
                customer.setEmail(resultSet.getString("EMAIL"));
                customer.setPhone(resultSet.getString("PHONE"));
                customer.setAddress(resultSet.getString("ADDRESS"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }




    // Method to save customer data
    public static void saveCustomer(String name, String email, String phoneNumber) {
        String sql = "INSERT INTO customers (name, email, phone_number) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phoneNumber);

            // Execute the insert query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
