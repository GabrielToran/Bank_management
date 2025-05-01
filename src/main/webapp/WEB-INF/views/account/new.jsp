<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bank Management System - Create New Account</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h1>Create New Account</h1>

    <div class="mb-3">
        <a href="<c:url value='/accounts/?customerId=${param.customerId}'/>" class="btn btn-secondary">Back to Accounts</a>
    </div>

    <div class="card">
        <div class="card-header">
            <h5>Account Information</h5>
        </div>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/accounts/customer/${param.customerId}" method="post">
            <div class="mb-3">
                    <label for="accountType" class="form-label">Account Type</label>
                    <select class="form-select" id="accountType" name="accountType" required onchange="showAdditionalFields()">
                        <option value="">Select Account Type</option>
                        <option value="savings">Savings Account</option>
                        <option value="checking">Checking Account</option>
                    </select>
                </div>

                <div class="mb-3">
                    <label for="initialDeposit" class="form-label">Initial Deposit ($)</label>
                    <input type="number" class="form-control" id="initialDeposit" name="initialDeposit" min="0" step="0.01" required>
                </div>

                <div id="savingsFields" style="display: none;">
                    <div class="mb-3">
                        <label for="interestRate" class="form-label">Interest Rate (%)</label>
                        <input type="number" class="form-control" id="interestRate" name="interestRate" min="0" step="0.01" value="1.0">
                        <div class="form-text">Annual interest rate as a percentage (e.g., 1.0 for 1%).</div>
                    </div>
                </div>

                <div id="checkingFields" style="display: none;">
                    <div class="mb-3">
                        <label for="overdraftLimit" class="form-label">Overdraft Limit ($)</label>
                        <input type="number" class="form-control" id="overdraftLimit" name="overdraftLimit" min="0" step="0.01" value="100.0">
                    </div>
                </div>

                <button type="submit" class="btn btn-primary">Create Account</button>
            </form>
        </div>
    </div>
</div>

<script>
    function showAdditionalFields() {
        const accountType = document.getElementById('accountType').value;
        document.getElementById('savingsFields').style.display = accountType === 'savings' ? 'block' : 'none';
        document.getElementById('checkingFields').style.display = accountType === 'checking' ? 'block' : 'none';
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>


