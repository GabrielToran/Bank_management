<!-- WEB-INF/views/account/create.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Create Account</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />
<div class="container mt-4">
    <h1>Create Account for ${param.customerId}</h1>
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/accounts">
        <input type="hidden" name="action" value="create" />
        <input type="hidden" name="customerId" value="${param.customerId}" />
        <div class="mb-3">
            <label class="form-label">Account Type</label>
            <select class="form-select" name="accountType">
                <option value="checking">Checking</option>
                <option value="savings">Savings</option>
            </select>
        </div>
        <div class="mb-3">
            <label class="form-label">Initial Deposit</label>
            <input type="number" step="0.01" class="form-control" name="initialDeposit" required />
        </div>
        <div class="mb-3" id="savingsFields" style="display:none;">
            <label class="form-label">Interest Rate</label>
            <input type="number" step="0.01" class="form-control" name="interestRate" placeholder="e.g. 0.02" />
        </div>
        <div class="mb-3" id="checkingFields" style="display:none;">
            <label class="form-label">Overdraft Limit</label>
            <input type="number" step="0.01" class="form-control" name="overdraftLimit" placeholder="e.g. 100.00" />
        </div>
        <button type="submit" class="btn btn-primary">Create Account</button>
        <a href="${pageContext.request.contextPath}/accounts?customerId=${param.customerId}" class="btn btn-secondary">Cancel</a>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const typeSelect = document.querySelector('select[name="accountType"]');
    const savingsFields = document.getElementById('savingsFields');
    const checkingFields = document.getElementById('checkingFields');
    typeSelect.addEventListener('change', () => {
        savingsFields.style.display = typeSelect.value === 'savings' ? 'block' : 'none';
        checkingFields.style.display = typeSelect.value === 'checking' ? 'block' : 'none';
    });
</script>
</body>
</html>