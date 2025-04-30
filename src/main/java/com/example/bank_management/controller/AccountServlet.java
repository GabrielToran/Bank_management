package com.example.bank_management.controller;
import com.example.bank_management.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.example.bank_management.model.AccountDAO;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.List;



@WebServlet(urlPatterns = {"/accounts", "/accounts/*", "/accounts/create"})
public class AccountServlet extends HttpServlet {

    private AccountDAO accountDAO;
    private CustomerDAO customerDAO;
    private TransactionDAO transactionDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        accountDAO = new AccountDAO();
        customerDAO = new CustomerDAO();
        transactionDAO = new TransactionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();

        if (pathInfo == null && "/accounts".equals(servletPath)) {
            // List accounts for a customer
            String customerIdParam = request.getParameter("customerId");
            if (customerIdParam != null && !customerIdParam.isEmpty()) {
                int customerId = Integer.parseInt(customerIdParam);
                Customer customer = customerDAO.getCustomerById(customerId);
                List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId);

                request.setAttribute("customer", customer);
                request.setAttribute("accounts", accounts);
                request.getRequestDispatcher("/WEB-INF/views/account/list.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/customers");
            }
        } else if ("/create".equals(pathInfo) || (pathInfo == null && servletPath.endsWith("/create"))) {
            // Show create account form
            request.getRequestDispatcher("/WEB-INF/views/account/create.jsp").forward(request, response);
        } else if (pathInfo != null && pathInfo.matches("/\\d+")) {
            // View account details
            int accountId = Integer.parseInt(pathInfo.substring(1));
            Account account = accountDAO.getAccountById(accountId);

            if (account != null) {
                request.setAttribute("account", account);
                request.getRequestDispatcher("/WEB-INF/views/account/view.jsp").forward(request, response);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("errorMessage", "Account not found");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("create".equals(action)) {
            // Create new account
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            String accountType = request.getParameter("accountType");
            double initialDeposit = Double.parseDouble(request.getParameter("initialDeposit"));

            Account account;
            if ("checking".equals(accountType)) {
                double overdraftLimit = 0;
                String overdraftLimitParam = request.getParameter("overdraftLimit");
                if (overdraftLimitParam != null && !overdraftLimitParam.isEmpty()) {
                    overdraftLimit = Double.parseDouble(overdraftLimitParam);
                }
                account = new CheckingAccount(0, customerId, initialDeposit, overdraftLimit);
            } else {
                double interestRate = 0;
                String interestRateParam = request.getParameter("interestRate");
                if (interestRateParam != null && !interestRateParam.isEmpty()) {
                    interestRate = Double.parseDouble(interestRateParam);
                }
                account = new SavingsAccount(0, customerId, initialDeposit, interestRate);
            }

            boolean created = accountDAO.createAccount(account, accountType);
            if (created) {
                // Create initial deposit transaction
                Transaction transaction = new Transaction(0, account.getAccountId(), "deposit", initialDeposit,
                        LocalDate.now(), "Initial deposit");
                transactionDAO.createTransaction(transaction);

                session.setAttribute("successMessage", "Account created successfully");
            } else {
                session.setAttribute("errorMessage", "Failed to create account");
            }
            response.sendRedirect(request.getContextPath() + "/accounts?customerId=" + customerId);
        } else if ("deposit".equals(action)) {
            // Process deposit
            int accountId = Integer.parseInt(request.getParameter("accountId"));
            double amount = Double.parseDouble(request.getParameter("amount"));

            Account account = accountDAO.getAccountById(accountId);
            if (account != null) {
                boolean success = account.deposit(amount);
                if (success) {
                    accountDAO.updateAccount(account);

                    // Create deposit transaction
                    Transaction transaction = new Transaction(0, accountId, "deposit", amount,
                            LocalDate.now(), "Deposit");
                    transactionDAO.createTransaction(transaction);

                    session.setAttribute("successMessage", "Deposit successful");
                } else {
                    session.setAttribute("errorMessage", "Invalid deposit amount");
                }
                response.sendRedirect(request.getContextPath() + "/accounts/" + accountId);
            } else {
                session.setAttribute("errorMessage", "Account not found");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else if ("withdraw".equals(action)) {
            // Process withdrawal
            int accountId = Integer.parseInt(request.getParameter("accountId"));
            double amount = Double.parseDouble(request.getParameter("amount"));

            Account account = accountDAO.getAccountById(accountId);
            if (account != null) {
                boolean success = account.withdraw(amount);
                if (success) {

                    accountDAO.updateAccountBalance(accountId,balance);

                    // Create withdrawal transaction
                    Transaction transaction = new Transaction(0, accountId, "withdraw", amount,
                            LocalDate.now(), "Withdrawal");
                    transactionDAO.createTransaction(transaction);

                    session.setAttribute("successMessage", "Withdrawal successful");
                } else {
                    session.setAttribute("errorMessage", "Withdrawal failed. Insufficient funds or invalid amount.");
                }
                response.sendRedirect(request.getContextPath() + "/accounts/" + accountId);
            } else {
                session.setAttribute("errorMessage", "Account not found");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();

        if (pathInfo != null && pathInfo.matches("/\\d+")) {
            int accountId = Integer.parseInt(pathInfo.substring(1));
            Account account = accountDAO.getAccountById(accountId);

            if (account != null) {
                int customerId = account.getCustomerId();

                // First delete associated transactions
                transactionDAO.deleteTransactionsByAccountId(accountId);

                // Then delete the account
                boolean deleted = accountDAO.deleteAccount(accountId);
                if (deleted) {
                    session.setAttribute("successMessage", "Account deleted successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to delete account");
                }
                response.sendRedirect(request.getContextPath() + "/accounts?customerId=" + customerId);
            } else {
                session.setAttribute("errorMessage", "Account not found");
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}