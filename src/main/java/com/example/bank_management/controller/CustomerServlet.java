package com.example.bank_management.controller;

import com.example.bank_management.model.Bank;
import com.example.bank_management.model.Customer;
import com.example.bank_management.model.Account;
import com.example.bank_management.model.CustomerDAO;
import com.example.bank_management.model.AccountDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {
    private Bank bank = Bank.getInstance();



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        // Create an instance of CustomerDAO and AccountDAO
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO(); // AccountDAO instance

        if (pathInfo == null || pathInfo.equals("/")) {
            // Handle the /customers route (showing the customer list)
            List<Customer> customers = customerDAO.getAllCustomers(); // Use DAO
            request.setAttribute("customers", customers);
            request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);

        } else if (pathInfo.equals("/new")) {
            System.out.println("Reached /new customerServlet path handler.");
            request.getRequestDispatcher("/WEB-INF/views/customer/customerNew.jsp").forward(request, response);

        } else {
            try {
                // Handle /customers/{id} (viewing a specific customer)
                int customerId = Integer.parseInt(pathInfo.substring(1));
                Customer customer = customerDAO.getCustomerById(customerId); // Use DAO

                if (customer != null) {
                    // Retrieve the accounts associated with the customer using AccountDAO
                    List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId);
                    customer.setAccounts(accounts); // Set the accounts in the customer object

                    // Set the customer object as an attribute to be accessed in the JSP
                    request.setAttribute("customer", customer);

                    // Forward the request to the JSP page for rendering
                    request.getRequestDispatcher("/WEB-INF/views/customer/view.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            System.out.println("Creating customer: " + firstName + " " + lastName); // Debug log

            // Create and save the customer
            Customer customer = new CustomerDAO().createAndSaveCustomer(firstName, lastName, email, phone, address);

            if (customer == null) {
                System.out.println("Customer creation returned null"); // Debug log
                request.setAttribute("error", "Failed to create customer");
                request.getRequestDispatcher("/WEB-INF/views/customer/customerNew.jsp").forward(request, response);
                return;
            }

            System.out.println("Customer created with ID: " + customer.getCustomerId()); // Debug log
            response.sendRedirect(request.getContextPath() + "/customers/" + customer.getCustomerId());
        } catch (Exception e) {
            System.out.println("Exception in doPost: " + e.getMessage()); // Debug log
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/customer/customerNew.jsp").forward(request, response);
        }
    }



    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // For updating an existing customer
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            try {
                int customerId = Integer.parseInt(pathInfo.substring(1));
                Customer customer = bank.getCustomer(customerId);

                if (customer != null) {
                    String firstName = request.getParameter("firstName");
                    String lastName = request.getParameter("lastName");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    String address = request.getParameter("address");

                    customer.setFirstName(firstName);
                    customer.setLastName(lastName);
                    customer.setEmail(email);
                    customer.setPhone(phone);
                    customer.setAddress(address);

                    bank.updateCustomer(customer);

                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // For deleting a customer
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            try {
                int customerId = Integer.parseInt(pathInfo.substring(1));
                boolean deleted = bank.deleteCustomer(customerId);

                if (deleted) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
