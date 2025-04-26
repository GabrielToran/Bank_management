<!-- webapp/WEB-INF/views/transaction/list.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bank Management System - Transaction History</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h1>Transaction History</h1>

    <div class="mb-3">
        <a href="<url value='/accounts/${account.accountId}'/>" class="btn btn-secondary">Back to Account</a>
    </div>

    <div class="card mb-4">
        <div class="card-header">
            <h5>Account Information</h5>
        </div>
        <div class="card-body">
            <p><strong>Account Number:</strong> ${account.accountNumber}</p>
            <p><strong>Account Type:</strong>
                <choose>
                    <when test="${account['class'].simpleName == 'SavingsAccount'}">Savings</when>
                    <when test="${account['class'].simpleName == 'CheckingAccount'}">Checking</when>
                    <otherwise>Unknown</otherwise>
                </choose>
            </p>
            <p><strong>Current Balance:</strong> $<fmt:formatNumber value="${account.balance}" pattern="#,##0.00"/></p>
        </div>
    </div>

    <h3>Transactions</h3>

    <if test="${empty transactions}">
        <div class="alert alert-info" role="alert">
            No transactions found for this account.
        </div>
    </if>

    <if test="${not empty transactions}">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Transaction ID</th>
                <th>Date</th>
                <th>Type</th>
                <th>Amount</th>
                <th>Description</th>
            </tr>
            </thead>
            <tbody>
            <forEach var="transaction" items="${transactions}">
                <tr>
                    <td>${transaction.transactionId}</td>
                    <td><formatDate value="${transaction.transactionDate}" pattern="MM/dd/yyyy HH:mm:ss"/></td>
                    <td>${transaction.type}</td>
                    <td>$<fmt:formatNumber value="${transaction.amount}" pattern="#,##0.00"/></td>
                    <td>${transaction.description}</td>
                </tr>
            </forEach>
            </tbody>
        </table>
    </if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>