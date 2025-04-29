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
                        request.setAttribute("errorMessage", "Customer not found.");
                        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Customer ID");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
            }
        } else {
            try {
                int accountId = parseId(pathInfo);
                Account account = bank.getAccount(accountId);

                if (account != null) {
                    request.setAttribute("account", account);

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
                    request.setAttribute("errorMessage", "Account not found.");
                    request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Account ID");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            handleCreateAccount(request, response);
        } else if ("deposit".equals(action)) {
            handleDeposit(request, response);
        } else if ("withdraw".equals(action)) {
            handleWithdraw(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void handleCreateAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String customerIdParam = request.getParameter("customerId");
        String accountType = request.getParameter("accountType");
        String initialDepositParam = request.getParameter("initialDeposit");

        if (customerIdParam != null && accountType != null && initialDepositParam != null) {
            try {
                int customerId = Integer.parseInt(customerIdParam);
                double initialDeposit = Double.parseDouble(initialDepositParam);

                if (initialDeposit < 0) {
                    request.setAttribute("errorMessage", "Initial deposit cannot be negative.");
                    request.getRequestDispatcher("/WEB-INF/views/account/create.jsp").forward(request, response);
                    return;
                }

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
                    request.getSession().setAttribute("successMessage", "Account created successfully!");
                    response.sendRedirect(request.getContextPath() + "/accounts/" + account.getAccountId());
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create account");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid numbers provided");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
        }
    }

    private void handleDeposit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accountIdParam = request.getParameter("accountId");
        String amountParam = request.getParameter("amount");

        if (accountIdParam != null && amountParam != null) {
            try {
                int accountId = Integer.parseInt(accountIdParam);
                double amount = Double.parseDouble(amountParam);

                if (amount <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Deposit amount must be positive.");
                    return;
                }

                boolean success = bank.deposit(accountId, amount);

                if (success) {
                    request.getSession().setAttribute("successMessage", "Deposit successful!");
                    response.sendRedirect(request.getContextPath() + "/accounts/" + accountId);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Deposit failed.");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid numbers provided");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
        }
    }

    private void handleWithdraw(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String accountIdParam = request.getParameter("accountId");
        String amountParam = request.getParameter("amount");

        if (accountIdParam != null && amountParam != null) {
            try {
                int accountId = Integer.parseInt(accountIdParam);
                double amount = Double.parseDouble(amountParam);

                if (amount <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Withdraw amount must be positive.");
                    return;
                }

                boolean success = bank.withdraw(accountId, amount);

                if (success) {
                    request.getSession().setAttribute("successMessage", "Withdrawal successful!");
                    response.sendRedirect(request.getContextPath() + "/accounts/" + accountId);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Withdrawal failed.");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid numbers provided");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && !pathInfo.equals("/")) {
            try {
                int accountId = parseId(pathInfo);
                boolean deleted = bank.deleteAccount(accountId);

                if (deleted) {
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Account ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
        }
    }

    private int parseId(String pathInfo) {
        return Integer.parseInt(pathInfo.substring(1));
    }
}
