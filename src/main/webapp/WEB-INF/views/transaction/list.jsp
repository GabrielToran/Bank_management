<!-- WEB-INF/views/transaction/list.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Transactions</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />
<div class="container mt-4">
    <h1>Transactions for Account #${account.accountId}</h1>
    <div class="row mb-3">
        <div class="col-md-4">
            <form method="get" action="${pageContext.request.contextPath}/transactions">
                <input type="hidden" name="accountId" value="${account.accountId}" />
                <div class="input-group">
                    <select name="type" class="form-select">
                        <option value="" ${empty typeFilter ? 'selected' : ''}>All Types</option>
                        <option value="deposit" ${typeFilter=='deposit' ? 'selected' : ''}>Deposits</option>
                        <option value="withdraw" ${typeFilter=='withdraw' ? 'selected' : ''}>Withdrawals</option>
                    </select>
                    <button class="btn btn-outline-secondary" type="submit">Filter</button>
                </div>
            </form>
        </div>
    </div>
    <table class="table table-striped">
        <thead>
        <tr><th>ID</th><th>Type</th><th>Amount</th><th>Date</th><th>Description</th><th>Actions</th></tr>
        </thead>
        <tbody>
        <c:forEach var="t" items="${transactions}">
            <tr>
                <td>${t.transactionId}</td>
                <td>${t.type}</td>
                <td>$${t.amount}</td>
                <td>${t.date}</td>
                <td>${t.description}</td>
                <td><a href="${pageContext.request.contextPath}/transactions/${t.transactionId}" class="btn btn-sm btn-info">View</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="${pageContext.request.contextPath}/accounts/${account.accountId}" class="btn btn-secondary">Back to Account</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>