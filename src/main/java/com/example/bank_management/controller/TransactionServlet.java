package com.example.bank_management.controller;

import com.example.bank_management.model.AccountDAO;
import com.example.bank_management.model.Account;


import com.example.bank_management.model.Transaction;
import com.example.bank_management.model.TransactionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/transactions", "/transactions/*"})
public class TransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;

    @Override
    public void init() {
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();

        try {
            if (pathInfo == null && "/transactions".equals(servletPath)) {
                // List transactions for an account
                listTransactions(request, response);
            } else if (pathInfo != null && pathInfo.matches("/\\d+")) {
                // View a specific transaction
                viewTransaction(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            handleError(request, response, e);
        }
    }

    private void listTransactions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Get account ID from request parameter
        String accountIdParam = request.getParameter("accountId");
        String typeFilter = request.getParameter("type");

        if (accountIdParam == null || accountIdParam.isEmpty() || "0".equals(accountIdParam)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        try {
            long accountId = Long.parseLong(accountIdParam);
            Account account = accountDAO.getAccountById((int) accountId);

            if (account == null) {
                setErrorMessage(request, "Account not found");
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            List<Transaction> transactions;
            if (typeFilter != null && !typeFilter.isEmpty()) {
                transactions = transactionDAO.getTransactionsByAccountId((int) accountId, typeFilter);
                request.setAttribute("typeFilter", typeFilter);
            } else {
                transactions = transactionDAO.findByAccountId(accountId);
            }

            request.setAttribute("account", account);
            request.setAttribute("transactions", transactions);
            request.getRequestDispatcher("/WEB-INF/views/transaction/list.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid account ID");
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    private void viewTransaction(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Extract transaction ID from path
        String pathInfo = request.getPathInfo();
        String transactionIdStr = pathInfo.substring(1);

        try {
            long transactionId = Long.parseLong(transactionIdStr);
            Transaction transaction = transactionDAO.getTransactionById((int) transactionId);

            if (transaction == null) {
                setErrorMessage(request, "Transaction not found");
                response.sendRedirect(request.getContextPath() + "/transactions?accountId=0");
                return;
            }

            request.setAttribute("transaction", transaction);
            request.getRequestDispatcher("/WEB-INF/views/transaction/view.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            setErrorMessage(request, "Invalid transaction ID");
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Process form submissions for transactions
        // Note: Transaction creation is typically handled by the AccountServlet
        // when processing deposits and withdrawals
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

    private void setSuccessMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        session.setAttribute("successMessage", message);
    }
}