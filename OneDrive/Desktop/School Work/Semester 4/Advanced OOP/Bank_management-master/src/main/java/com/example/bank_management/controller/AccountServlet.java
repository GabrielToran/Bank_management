package com.example.bank_management.controller;
import com.example.bank_management.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/accounts/*")
public class AccountServlet extends HttpServlet {
    private Bank bank = Bank.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Customer ID is required to list accounts
            String customerIdParam = request.getParameter("customerId");

            if (customerIdParam != null) {
                try {
                    int customerId = Integer.parseInt(customerIdParam);
                    Customer customer = bank.getCustomer(customerId);

                    if (customer != null) {
                        List<Account> accounts = bank.getCustomerAccounts(customerId);
                        request.setAttribute("customer", customer);
                        request.setAttribute("accounts", accounts);
                        request.getRequestDispatcher("/WEB-INF/views/account/list.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
            }
        } else {
            try {
                // Get specific account
                int accountId = Integer.parseInt(pathInfo.substring(1));
                Account account = bank.getAccount(accountId);

                if (account != null) {
                    request.setAttribute("account", account);

                    // Determine which JSP to forward to based on account type
                    String viewPath;
                    if (account instanceof SavingsAccount) {
                        viewPath = "/WEB-INF/views/account/savings.jsp";
                    } else if (account instanceof CheckingAccount) {
                        viewPath = "/WEB-INF/views/account/checking.jsp";
                    } else {
                        viewPath = "/WEB-INF/views/account/view.jsp";
                    }

                    request.getRequestDispatcher(viewPath).forward(request, response);
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
        // Create a new account
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            String customerIdParam = request.getParameter("customerId");
            String accountType = request.getParameter("accountType");
            String initialDepositParam = request.getParameter("initialDeposit");

            if (customerIdParam != null && accountType != null && initialDepositParam != null) {
                try {
                    int customerId = Integer.parseInt(customerIdParam);
                    double initialDeposit = Double.parseDouble(initialDepositParam);

                    Account account = null;

                    if ("savings".equals(accountType)) {
                        String interestRateParam = request.getParameter("interestRate");
                        double interestRate = interestRateParam != null ? Double.parseDouble(interestRateParam) : 0.01;
                        account = bank.createSavingsAccount(customerId, initialDeposit, interestRate);
                    } else if ("checking".equals(accountType)) {
                        String overdraftLimitParam = request.getParameter("overdraftLimit");
                        double overdraftLimit = overdraftLimitParam != null ? Double.parseDouble(overdraftLimitParam) : 100.0;
                        account = bank.createCheckingAccount(customerId, initialDeposit, overdraftLimit);
                    }

                    if (account != null) {
                        response.sendRedirect(request.getContextPath() + "/accounts/" + account.getAccountId());
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create account");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Delete an account
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            try {
                int accountId = Integer.parseInt(pathInfo.substring(1));
                boolean deleted = bank.deleteAccount(accountId);

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