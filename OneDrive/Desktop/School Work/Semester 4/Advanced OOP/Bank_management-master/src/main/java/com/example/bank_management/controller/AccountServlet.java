package com.example.bank_management.controller;
import com.example.bank_management.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.example.bank_management.model.AccountDAO;
import java.util.List;

@WebServlet("/accounts/*")
public class AccountServlet extends HttpServlet {
    private final Bank bank = Bank.getInstance();

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
        }else if (pathInfo.equals("/new")) {
            String customerId = request.getParameter("customerId");
            if (customerId != null) {
                request.setAttribute("customerId", customerId);
                request.getRequestDispatcher("/WEB-INF/views/account/new.jsp").forward(request, response);
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
                        viewPath = "/WEB-INF/views/account/list.jsp";
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
    public void init() throws ServletException {
        System.out.println("AccountServlet initialized with mapping: /accounts/*");
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        System.out.println("doPost method called!");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Action parameter: " + request.getParameter("action"));
        if ("create".equals(action)) {
            String customerIdParam = request.getParameter("customerId").replaceAll("[^\\d]", "");
            String accountType = request.getParameter("accountType");
            String initialDepositParam = request.getParameter("initialDeposit");
            String interestRateParam = request.getParameter("interestRate");
            String overdraftLimitParam = request.getParameter("overdraftLimit");


            System.out.println("-------------Parameters and account---------------");
            System.out.println("Customer ID: " + customerIdParam);
            System.out.println("Account Type: " + accountType);
            System.out.println("initialDeposit: " + initialDepositParam);
            System.out.println("interestRate: " + interestRateParam);
            System.out.println("overdraftLimit: " + overdraftLimitParam);
            if (customerIdParam != null && accountType != null && initialDepositParam != null) {
                try {
                    System.out.println("I have reached this try catch statement.");
                    int customerId = Integer.parseInt(customerIdParam);
                    double initialDeposit = Double.parseDouble(initialDepositParam);

                    // Check if the customer exists in the database
                    Customer customer = bank.getCustomer(customerId);
                    if (customer == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                        return;
                    }

                    Account account = null;


                    // Create account based on type
                    if ("savings".equals(accountType)) {
                        interestRateParam = request.getParameter("interestRate");
                        double interestRate = (interestRateParam != null && !interestRateParam.isEmpty()) ?
                                Double.parseDouble(interestRateParam) : 0.01;  // Default to 0.01 if missing
                        account = bank.createSavingsAccount(customerId, initialDeposit, interestRate);
                    } else if ("checking".equals(accountType)) {
                        double overdraftLimit = (overdraftLimitParam != null && !overdraftLimitParam.isEmpty()) ?
                                Double.parseDouble(overdraftLimitParam) : 100.0;  // Default to 100.0 if missing
                        account = bank.createCheckingAccount(customerId, initialDeposit, overdraftLimit);
                    }

                    if (account != null) {
                        System.out.println("Account created successfully: " + account);
                        try {
                            AccountDAO.saveAccount(account); // Persist to H2
                            response.sendRedirect(request.getContextPath() + "/accounts?customerId=" + customerId); // Redirect to account list
                        } catch (Exception e) {
                            e.printStackTrace(); // Optional: log it
                            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error saving account to database");
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to create account");
                    }

                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
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