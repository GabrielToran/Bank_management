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

@WebServlet("/transactions/*")
public class TransactionServlet extends HttpServlet {
    private Bank bank = Bank.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Account ID is required to list transactions
            String accountIdParam = request.getParameter("accountId");

            if (accountIdParam != null) {
                try {
                    int accountId = Integer.parseInt(accountIdParam);
                    Account account = bank.getAccount(accountId);

                    if (account != null) {
                        List<Transaction> transactions = bank.getAccountTransactions(accountId);
                        request.setAttribute("account", account);
                        request.setAttribute("transactions", transactions);
                        request.getRequestDispatcher("/WEB-INF/views/transaction/list.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Account not found");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Account ID is required");
            }
        } else {
            try {
                // Get specific transaction
                int transactionId = Integer.parseInt(pathInfo.substring(1));
                Transaction transaction = bank.getTransaction(transactionId);

                if (transaction != null) {
                    request.setAttribute("transaction", transaction);
                    request.getRequestDispatcher("/WEB-INF/views/transaction/view.jsp").forward(request, response);
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
        // Create a new transaction
        String accountIdParam = request.getParameter("accountId");
        String type = request.getParameter("type");
        String amountParam = request.getParameter("amount");
        String description = request.getParameter("description");

        if (accountIdParam != null && type != null && amountParam != null) {
            try {
                int accountId = Integer.parseInt(accountIdParam);
                double amount = Double.parseDouble(amountParam);

                Transaction transaction = bank.createTransaction(accountId, type, amount, description);

                if (transaction != null) {
                    response.sendRedirect(request.getContextPath() + "/transactions?accountId=" + accountId);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create transaction");
                }
            }  catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    } else {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
    }
}
}