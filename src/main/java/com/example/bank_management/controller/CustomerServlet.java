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
import java.util.stream.Collectors;

@WebServlet("/customers/*")
public class CustomerServlet extends HttpServlet {
    private Bank bank = Bank.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Check if it's a search
            String searchQuery = request.getParameter("search");
            List<Customer> customers = bank.getAllCustomers();

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                String query = searchQuery.trim().toLowerCase();
                customers = customers.stream()
                        .filter(c -> c.getFirstName().toLowerCase().contains(query) ||
                                c.getLastName().toLowerCase().contains(query) ||
                                c.getEmail().toLowerCase().contains(query))
                        .collect(Collectors.toList());
                request.setAttribute("searchQuery", searchQuery);
            }

            request.setAttribute("customers", customers);
            request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);

        } else {
            // View a specific customer
            try {
                int customerId = parseId(pathInfo);
                Customer customer = bank.getCustomer(customerId);

                if (customer != null) {
                    request.setAttribute("customer", customer);
                    request.getRequestDispatcher("/WEB-INF/views/customer/view.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Customer not found.");
                    request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid customer ID.");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle creating a new customer
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        if (isEmpty(firstName) || isEmpty(lastName) || isEmpty(email)) {
            request.setAttribute("errorMessage", "First name, last name, and email are required.");
            request.getRequestDispatcher("/WEB-INF/views/customer/create.jsp").forward(request, response);
            return;
        }

        Customer customer = bank.createCustomer(firstName, lastName, email, phone, address);

        request.getSession().setAttribute("successMessage", "Customer created successfully!");
        response.sendRedirect(request.getContextPath() + "/customers/" + customer.getCustomerId());
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle updating a customer
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            try {
                int customerId = parseId(pathInfo);
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
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found.");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid customer ID.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle deleting a customer
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            try {
                int customerId = parseId(pathInfo);
                boolean deleted = bank.deleteCustomer(customerId);

                if (deleted) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found.");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid customer ID.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path.");
        }
    }

    private int parseId(String pathInfo) {
        return Integer.parseInt(pathInfo.substring(1));
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
