package com.example.bank_management.controller;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Bank;
import com.example.bank_management.model.Transaction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/transactions/*")
public class TransactionServlet extends HttpServlet {
    private Bank bank = Bank.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // List all transactions for a specific account
            String accountIdParam = request.getParameter("accountId");
            String typeFilter = request.getParameter("type"); // deposit, withdraw
            String dateFilter = request.getParameter("date"); // (optional future expansion)

            if (accountIdParam != null) {
                try {
                    int accountId = Integer.parseInt(accountIdParam);
                    Account account = bank.getAccount(accountId);

                    if (account != null) {
                        List<Transaction> transactions = bank.getAccountTransactions(accountId);

                        if (typeFilter != null && !typeFilter.isEmpty()) {
                            transactions = transactions.stream()
                                    .filter(t -> t.getType().equalsIgnoreCase(typeFilter))
                                    .collect(Collectors.toList());
                        }

                        request.setAttribute("account", account);
                        request.setAttribute("transactions", transactions);
                        request.setAttribute("typeFilter", typeFilter);

                        request.getRequestDispatcher("/WEB-INF/views/transaction/list.jsp").forward(request, response);
                    } else {
                        request.setAttribute("errorMessage", "Account not found.");
                        request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Account ID");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID is required");
            }
        } else {
            try {
                // View a specific transaction
                int transactionId = parseId(pathInfo);
                Transaction transaction = bank.getTransaction(transactionId);

                if (transaction != null) {
                    request.setAttribute("transaction", transaction);
                    request.getRequestDispatcher("/WEB-INF/views/transaction/view.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Transaction not found.");
                    request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Transaction ID");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create a new transaction
        String accountIdParam = request.getParameter("accountId");
        String type = request.getParameter("type");
        String amountParam = request.getParameter("amount");
        String description = request.getParameter("description");

        if (accountIdParam != null && type != null && amountParam != null) {
            try {
                int accountId = Integer.parseInt(accountIdParam);
                double amount = Double.parseDouble(amountParam);

                if (amount <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Amount must be positive.");
                    return;
                }

                Transaction transaction = bank.createTransaction(accountId, type, amount, description);

                if (transaction != null) {
                    request.getSession().setAttribute("successMessage", "Transaction created successfully!");
                    response.sendRedirect(request.getContextPath() + "/transactions?accountId=" + accountId);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create transaction");
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number provided");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
        }
    }

    private int parseId(String pathInfo) {
        return Integer.parseInt(pathInfo.substring(1));
    }
}
