<!-- webapp/WEB-INF/views/account/list.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<!DOCTYPE html>
<html>
<head>
    <title>Bank Management System - Account List</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h1>Account List for ${customer.firstName} ${customer.lastName}</h1>

    <div class="mb-3">
        <a href="<c:url value='/customers/${customer.customerId}'/>" class="btn btn-secondary">Back to Customer</a>
        <a href="<c:url value='/accounts/new?customerId=${customer.customerId}'/>" class="btn btn-success">Create New Account</a>
    </div>

    <c:if test="${empty accounts}">
        <div class="alert alert-info" role="alert">
            No accounts found for this customer.
        </div>
    </c:if>

    <c:if test="${not empty accounts}">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Account Number</th>
                <th>Type</th>
                <th>Balance</th>
                <th>Open Date</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="account" items="${accounts}">
                <tr>
                    <td>${account.accountNumber}</td>
                    <td>
                        <choose>
                            <c:when test="${account['class'].simpleName == 'SavingsAccount'}">Savings</c:when>
                            <when test="${account['class'].simpleName == 'CheckingAccount'}">Checking</when>
                            <otherwise>Unknown</otherwise>
                        </choose>
                    </td>
                    <td>$${account.balance}</td>
                    <td>${account.openDate}</td>
                    <td>
                        <a href="<c:url value='/accounts/${account.accountId}'/>" class="btn btn-sm btn-info">View</a>
                        <a href="<c:url value='/transactions?accountId=${account.accountId}'/>" class="btn btn-sm btn-primary">Transactions</a>
                        <button class="btn btn-sm btn-danger" onclick="deleteAccount(${account.accountId})">Close</button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</div>

<script>
    function deleteAccount(accountId) {
        if (confirm('Are you sure you want to close this account?')) {
            const xhr = new XMLHttpRequest();
            xhr.open('DELETE', '${pageContext.request.contextPath}/accounts/' + accountId);
            xhr.onload = function() {
                if (xhr.status === 200) {
                    alert('Account closed successfully');
                    window.location.reload();
                } else {
                    alert('Failed to close account');
                }
            };
            xhr.send();
        }
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>