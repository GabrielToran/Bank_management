package com.example.bank_management.controller;
import com.example.bank_management.model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private Bank bank;

    @Override
    public void init() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        this.bank = new Bank(customerDAO, accountDAO, transactionDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get counts and totals for dashboard
            int customerCount = bank.getCustomerCount();
            int accountCount = bank.getAccountCount();
            BigDecimal totalBalance = bank.getTotalBalance();
            int transactionCount = bank.getTransactionCount();

            // Set attributes for the dashboard view
            request.setAttribute("customerCount", customerCount);
            request.setAttribute("accountCount", accountCount);
            request.setAttribute("totalBalance", totalBalance);
            request.setAttribute("transactionCount", transactionCount);

            // Forward to the dashboard view
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            handleError(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Dashboard typically doesn't handle POST requests
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        e.printStackTrace();
        setErrorMessage(request, "An error occurred: " + e.getMessage());
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private void setErrorMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("errorMessage", message);
    }
}