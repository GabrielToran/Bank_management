package com.example.bank_management.controller;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Bank;
import com.example.bank_management.model.Customer;
import com.example.bank_management.model.Transaction;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private Bank bank = Bank.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Fetch all customers
        List<Customer> customers = bank.getAllCustomers();
        int customerCount = customers.size();

        // Fetch all accounts
        List<Account> accounts = bank.getAllAccounts();  // assumes Bank provides this
        int accountCount = accounts.size();

        // Sum up total balance
        double totalBalance = accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();

        // Fetch all transactions
        List<Transaction> transactions = bank.getAllTransactions();  // assumes Bank provides this
        int transactionCount = transactions.size();

        // Put metrics into request
        request.setAttribute("customerCount", customerCount);
        request.setAttribute("accountCount", accountCount);
        request.setAttribute("totalBalance", totalBalance);
        request.setAttribute("transactionCount", transactionCount);

        // Forward to JSP
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
                .forward(request, response);
    }
}
