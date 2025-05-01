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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/accounts/*")
public class AccountServlet extends HttpServlet {
    private final Bank bank = Bank.getInstance();

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
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
            }
        } else if (pathInfo.equals("/new")) {
            String customerId = request.getParameter("customerId");
            if (customerId != null) {
                request.setAttribute("customerId", customerId);
                request.getRequestDispatcher("/WEB-INF/views/account/new.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Customer ID is required");
            }
        } else if (pathInfo.matches("^/customer/\\d+$")) {
            System.out.println("Received Get request in Account DoGet");
            int customerId = Integer.parseInt(pathInfo.substring("/customer/".length()));
            Customer customer = bank.getCustomer(customerId);

            if (customer != null) {
                List<Account> accounts = bank.getCustomerAccounts(customerId);
                request.setAttribute("customer", customer);
                request.setAttribute("accounts", accounts);
                request.getRequestDispatcher("/WEB-INF/views/account/list.jsp").forward(request, response); // <- Add this
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
            }
        }
        else {
            try {
                int accountId = Integer.parseInt(pathInfo.substring(1));
                Account account = bank.getAccount(accountId);

                if (account != null) {
                    request.setAttribute("account", account);

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("doPost pathInfo: " + pathInfo);

        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing path info.");
            return;
        }

        Pattern pattern = Pattern.compile("^/customer/([0-9]+)$");
        Matcher matcher = pattern.matcher(pathInfo);

        if (matcher.matches()) {
            int customerId = Integer.parseInt(matcher.group(1));

            String accountType = request.getParameter("accountType");
            String initialDepositParam = request.getParameter("initialDeposit");
            String interestRateParam = request.getParameter("interestRate");
            String overdraftLimitParam = request.getParameter("overdraftLimit");

            System.out.println("----------------Account Debugging info----------------");
            System.out.println("Looking for customer with ID: " + customerId);
            System.out.println("accountType: " + accountType);
            System.out.println("initialDeposit: " + initialDepositParam);
            System.out.println("interestRate: " + interestRateParam);
            System.out.println("overdraftLimit: " + overdraftLimitParam);
            System.out.println("----------------End of Debug lines----------------");

            try {
                System.out.println("Try Catch statement reached.");
                double initialDeposit = Double.parseDouble(initialDepositParam);
                System.out.println("Initial deposit statement reached.");
                Customer customer = bank.getCustomer(customerId);
                System.out.println("Customer assigning statement reached.");
                if (customer == null) {
                    System.out.println("Customer not fund!");
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Customer not found");
                    return;  // Stop further execution if the customer doesn't exist
                }
                System.out.println("Past the customer == null check.");
                Account account;
                if ("savings".equals(accountType)) {
                    System.out.println("Received POST request for /accounts/customer with Saving Account");
                    double interestRate = (interestRateParam != null && !interestRateParam.isEmpty()) ?
                            Double.parseDouble(interestRateParam) : 1.0;
                    account = bank.createSavingsAccount(customerId, initialDeposit, interestRate);
                } else if ("checking".equals(accountType)) {
                    System.out.println("Received POST request for /accounts/customer with Checking account Account");
                    double overdraftLimit = (overdraftLimitParam != null && !overdraftLimitParam.isEmpty()) ?
                            Double.parseDouble(overdraftLimitParam) : 100.0;
                    account = bank.createCheckingAccount(customerId, initialDeposit, overdraftLimit);
                } else {
                    System.out.println("Invalid Account Type!");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid account type");
                    return;  // Stop further execution if account type is invalid
                }
                System.out.println("Past the account if checks.");
                AccountDAO.saveAccount(account);
                // Now that everything is fine, perform the redirect
                String redirectUrl = request.getContextPath() + "/accounts/customer/" + customerId;
                System.out.println("Redirecting to: " + redirectUrl);
                response.sendRedirect(redirectUrl);

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save account");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unsupported path: " + pathInfo);
        }
    }


    @Override
    public void init() throws ServletException {
        System.out.println("AccountServlet initialized with mapping: /accounts/*");
        super.init();
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