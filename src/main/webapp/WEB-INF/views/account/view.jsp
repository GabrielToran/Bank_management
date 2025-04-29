<!-- WEB-INF/views/account/view.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Account Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />
<div class="container mt-4">
    <!-- Alerts -->
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="successMessage" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${sessionScope.errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="errorMessage" scope="session" />
    </c:if>
    <h1>Account #${account.accountId} Details</h1>
    <dl class="row">
        <dt class="col-sm-3">Type</dt><dd class="col-sm-9">${account.class.simpleName}</dd>
        <dt class="col-sm-3">Balance</dt><dd class="col-sm-9">$${account.balance}</dd>
        <c:if test="${account.hasProperty('interestRate')}">
            <dt class="col-sm-3">Interest Rate</dt><dd class="col-sm-9">${account.interestRate}</dd>
        </c:if>
        <c:if test="${account.hasProperty('overdraftLimit')}">
            <dt class="col-sm-3">Overdraft Limit</dt><dd class="col-sm-9">$${account.overdraftLimit}</dd>
        </c:if>
    </dl>
    <div class="row">
        <div class="col-md-6">
            <h4>Make a Deposit</h4>
            <form method="post" action="${pageContext.request.contextPath}/accounts">
                <input type="hidden" name="action" value="deposit" />
                <input type="hidden" name="accountId" value="${account.accountId}" />
                <div class="mb-3">
                    <label class="form-label">Amount</label>
                    <input type="number" step="0.01" name="amount" class="form-control" required />
                </div>
                <button type="submit" class="btn btn-success">Deposit</button>
            </form>
        </div>
        <div class="col-md-6">
            <h4>Make a Withdrawal</h4>
            <form method="post" action="${pageContext.request.contextPath}/accounts">
                <input type="hidden" name="action" value="withdraw" />
                <input type="hidden" name="accountId" value="${account.accountId}" />
                <div class="mb-3">
                    <label class="form-label">Amount</label>
                    <input type="number" step="0.01" name="amount" class="form-control" required />
                </div>
                <button type="submit" class="btn btn-warning">Withdraw</button>
            </form>
        </div>
    </div>
    <div class="mt-4">
        <a href="${pageContext.request.contextPath}/accounts?customerId=${account.customerId}" class="btn btn-secondary">Back to Accounts</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>