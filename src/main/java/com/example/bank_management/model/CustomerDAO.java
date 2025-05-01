package com.example.bank_management.model;
import java.sql.*;
import java.util.*;

public class CustomerDAO {
    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO CUSTOMER (first_name, last_name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_CUSTOMER_BY_ID = "SELECT * FROM CUSTOMER WHERE customer_id = ?";
    private static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM CUSTOMER";

    // Method to save a customer to the database
    public boolean saveCustomer(Customer customer) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_SQL)) {

            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.setString(5, customer.getAddress());

            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Customer createAndSaveCustomer(String firstName, String lastName, String email, String phone, String address) {
        // Create customer object without customerId
        Customer customer = new Customer(firstName, lastName, email, phone, address);

        // Insert customer into the database and retrieve the auto-generated ID
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CUSTOMER_SQL,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, customer.getFirstName());
            preparedStatement.setString(2, customer.getLastName());
            preparedStatement.setString(3, customer.getEmail());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.setString(5, customer.getAddress());

            int result = preparedStatement.executeUpdate();

            if (result > 0) {
                // Retrieve the auto-generated customer ID
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        customer.setCustomerId(generatedKeys.getInt(1)); // Set the generated customer ID
                    }
                }
            }

            return result > 0 ? customer : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    // Method to get a customer by their ID
    public Customer getCustomerById(int customerId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CUSTOMER_BY_ID)) {

            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");

                Customer customer = new Customer(customerId, firstName, lastName, email, phone, address);

                // ✅ Load accounts and attach to customer
                List<Account> accounts = AccountDAO.getAccountsByCustomerId(customerId);
                customer.setAccounts(accounts);

                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Method to retrieve all customers
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CUSTOMERS)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");

                Customer customer = new Customer(customerId, firstName, lastName, email, phone, address);
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}
