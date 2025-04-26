package com.example.bank_management.controller;

import com.example.bank_management.model.Bank;
import com.example.bank_management.model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {
    private Bank bank = Bank.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all customers
            List<Customer> customers = bank.getAllCustomers();
            request.setAttribute("customers", customers);
            request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);
        } else {
            try {
                // Get specific customer
                int customerId = Integer.parseInt(pathInfo.substring(1));
                Customer customer = bank.getCustomer(customerId);

                if (customer != null) {
                    request.setAttribute("customer", customer);
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
        // For adding a new customer
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        Customer customer = bank.createCustomer(firstName, lastName, email, phone, address);

        response.sendRedirect(request.getContextPath() + "/customers/" + customer.getCustomerId());
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
