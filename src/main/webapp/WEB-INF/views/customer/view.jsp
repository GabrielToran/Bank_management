<!-- webapp/WEB-INF/views/customer/view.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bank Management System - Customer Details</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h1>Customer Details</h1>

    <div class="mb-3">
        <a href="<c:url value='/customers/'/>" class="btn btn-secondary">Back to Customers</a>
        <a href="<c:url value='/customers/edit/${customer.customerId}'/>" class="btn btn-warning">Edit Customer</a>
    </div>


    <div class="card">
        <div class="card-header">
            <h5>Personal Information</h5>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <p><strong>Customer ID:</strong> ${customer.customerId}</p>
                    <p><strong>First Name:</strong> ${customer.firstName}</p>
                    <p><strong>Last Name:</strong> ${customer.lastName}</p>
                </div>
                <div class="col-md-6">
                    <p><strong>Email:</strong> ${customer.email}</p>
                    <p><strong>Phone:</strong> ${customer.phone}</p>
                    <p><strong>Address:</strong> ${customer.address}</p>
                </div>
            </div>
        </div>
    </div>

    <div class="mt-4">
        <h3>Accounts</h3>

        <c:if test="${empty customer.accounts}">
            <p>No accounts found for this customer.</p>
            <a href="${pageContext.request.contextPath}/accounts/new?customerId=${customer.customerId}" class="btn btn-success mb-3">Create Account for This Customer</a>
        </c:if>

        <c:if test="${not empty customer.accounts}">
            <a href="${pageContext.request.contextPath}/accounts/new?customerId=${customer.customerId}" class="btn btn-success mb-3">Create New Account</a>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Account Number</th>
                    <th>Type</th>
                    <th>Balance</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="account" items="${customer.accounts}">
                    <tr>
                        <td>${account.accountNumber}</td>
                        <td>
                            <c:choose>
                                <c:when test="${account['class'].simpleName == 'SavingsAccount'}">Savings</c:when>
                                <c:when test="${account['class'].simpleName == 'CheckingAccount'}">Checking</c:when>
                                <c:otherwise>Unknown</c:otherwise>
                            </c:choose>
                        </td>
                        <td>$${account.balance}</td>
                        <td>
                            <a href="<c:url value='/accounts/${account.accountId}'/>" class="btn btn-sm btn-info">View</a>
                            <a href="<c:url value='/transactions/?accountId=${account.accountId}'/>" class="btn btn-sm btn-primary">Transactions</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
