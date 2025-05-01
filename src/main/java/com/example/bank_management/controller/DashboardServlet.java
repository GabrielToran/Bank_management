package com.example.bank_management.controller;
import com.example.bank_management.model.Bank;
import com.example.bank_management.model.Customer;

import com.example.bank_management.model.DatabaseConnection;
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
        List<Customer> customers = DatabaseConnection.getAllCustomers();

        request.setAttribute("customers", customers);
        request.setAttribute("customerCount", customers.size());

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}
