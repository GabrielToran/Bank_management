<!-- webapp/WEB-INF/views/account/savings.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bank Management System - Savings Account</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h1>Savings Account Details</h1>

    <div class="mb-3">
        <a href="<c:url value='/accounts/?customerId=${account.customerId}'/>" class="btn btn-secondary">Back to Accounts</a>
        <a href="<c:url value='/transactions/?accountId=${account.accountId}'/>" class="btn btn-primary">View Transactions</a>
    </div>

    <div class="card mb-4">
        <div class="card-header">
            <h5>Account Information</h5>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <p><strong>Account Number:</strong> ${account.accountNumber}</p>
                    <p><strong>Account Type:</strong> Savings</p>
                    <p><strong>Balance:</strong> $<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/></p>
                </div>
                <div class="col-md-6">
                    <p><strong>Open Date:</strong> <fmt:formatDate value="${account.openDate}" pattern="MM/dd/yyyy"/></p>
                    <p><strong>Interest Rate:</strong> <fmt:formatNumber value="${account.interestRate * 100}" pattern="#,##0.00"/>%</p>
                    <p><strong>Last Interest Applied:</strong> <fmt:formatDate value="${account.lastInterestDate}" pattern="MM/dd/yyyy"/></p>
                </div>
            </div>
        </div>
    </div>

    <div class="card mb-4">
        <div class="card-header">
            <h5>Make a Transaction</h5>
        </div>
        <div class="card-body">
            <form action="<c:url value='/transactions/'/>" method="post">
                <input type="hidden" name="accountId" value="${account.accountId}">

                <div class="mb-3">
                    <label for="type" class="form-label">Transaction Type</label>
                    <select class="form-select" id="type" name="type" required>
                        <option value="DEPOSIT">Deposit</option>
                        <option value="WITHDRAWAL">Withdrawal</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="amount" class="form-label">Amount ($)</label>
                    <input type="number" class="form-control" id="amount" name="amount" min="0.01" step="0.01" required>
                </div>

                <div class="mb-3">
                    <label for="description" class="form-label">Description (Optional)</label>
                    <input type="text" class="form-control" id="description" name="description">
                </div>

                <button type="submit" class="btn btn-primary">Submit Transaction</button>
            </form>
        </div>
    </div>

    <div>
        <h3>Recent Transactions</h3>
        <c:if test="${empty account.transactions}">
            <p>No transactions found for this account.</p>
        </c:if>

        <c:if test="${not empty account.transactions}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Type</th>
                    <th>Amount</th>
                    <th>Description</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="transaction" items="${account.transactions}" end="4">
                    <tr>
                        <td><fmt:formatDate value="${transaction.transactionDate}" pattern="MM/dd/yyyy HH:mm"/></td>
                        <td>${transaction.type}</td>
                        <td>$<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/></td>
                        <td>${transaction.description}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <a href="<c:url value='/transactions/?accountId=${account.accountId}'/>" class="btn btn-link">View All Transactions</a>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>