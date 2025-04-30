package com.example.bank_management.controller;

import com.example.bank_management.model.Bank;
import com.example.bank_management.model.Customer;
import com.example.bank_management.model.CustomerDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/customers", "/customers/*"})
public class CustomerServlet extends HttpServlet {

    private CustomerDAO customerDAO;

    @Override
    public void init() {
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();

        if (pathInfo == null && "/customers".equals(servletPath)) {
            // List all customers or search
            String searchQuery = request.getParameter("search");
            List<Customer> customers;

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                customers = customerDAO.searchCustomers(searchQuery);
                request.setAttribute("searchQuery", searchQuery);
            } else {
                customers = customerDAO.getAllCustomers();
            }

            request.setAttribute("customers", customers);
            request.getRequestDispatcher("/WEB-INF/views/customer/list.jsp").forward(request, response);

        } else if (pathInfo != null && pathInfo.matches("/\\d+")) {
            // View specific customer
            int customerId = Integer.parseInt(pathInfo.substring(1));
            Customer customer = customerDAO.getCustomerById(customerId);

            if (customer != null) {
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/WEB-INF/views/customer/view.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
            }

        } else if (pathInfo != null && pathInfo.equals("/create")) {
            // Show create form
            request.getRequestDispatcher("/WEB-INF/views/customer/create.jsp").forward(request, response);

        } else if (pathInfo != null && pathInfo.matches("/\\d+/edit")) {
            // Show edit form
            int customerId = Integer.parseInt(pathInfo.substring(1, pathInfo.indexOf("/edit")));
            Customer customer = customerDAO.getCustomerById(customerId);

            if (customer != null) {
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/WEB-INF/views/customer/edit.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
            }

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();

        // Check for HTTP method override (for PUT and DELETE)
        String methodParam = request.getParameter("_method");
        if (methodParam != null) {
            if ("put".equalsIgnoreCase(methodParam)) {
                doPut(request, response);
                return;
            } else if ("delete".equalsIgnoreCase(methodParam)) {
                doDelete(request, response);
                return;
            }
        }

        // Regular POST - Create a new customer
        if (pathInfo == null) {
            Customer customer = new Customer();
            customer.setFirstName(request.getParameter("firstName"));
            customer.setLastName(request.getParameter("lastName"));
            customer.setEmail(request.getParameter("email"));
            customer.setPhone(request.getParameter("phone"));
            customer.setAddress(request.getParameter("address"));

            if (customerDAO.createCustomer(customer)) {
                session.setAttribute("successMessage", "Customer created successfully!");
                response.sendRedirect(request.getContextPath() + "/customers");
            } else {
                request.setAttribute("errorMessage", "Failed to create customer");
                request.getRequestDispatcher("/WEB-INF/views/customer/create.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int customerId = Integer.parseInt(pathInfo.substring(1));
            Customer customer = new Customer();
            customer.setCustomerId(customerId);
            customer.setFirstName(request.getParameter("firstName"));
            customer.setLastName(request.getParameter("lastName"));
            customer.setEmail(request.getParameter("email"));
            customer.setPhone(request.getParameter("phone"));
            customer.setAddress(request.getParameter("address"));

            if (customerDAO.updateCustomer(customer)) {
                session.setAttribute("successMessage", "Customer updated successfully!");
                response.sendRedirect(request.getContextPath() + "/customers/" + customerId);
            } else {
                request.setAttribute("errorMessage", "Failed to update customer");
                request.setAttribute("customer", customer);
                request.getRequestDispatcher("/WEB-INF/views/customer/edit.jsp").forward(request, response);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int customerId = Integer.parseInt(pathInfo.substring(1));

            if (customerDAO.deleteCustomer(customerId)) {
                session.setAttribute("successMessage", "Customer deleted successfully!");
            } else {
                session.setAttribute("errorMessage", "Failed to delete customer. They may have associated accounts.");
            }

            response.sendRedirect(request.getContextPath() + "/customers");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}