<!-- webapp/WEB-INF/views/account/new.jsp -->
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
        <div style="background: #eee; padding: 5px; margin-bottom: 10px;">
            Context Path: ${pageContext.request.contextPath}<br>
            Form will submit to: ${pageContext.request.contextPath}/accounts
        </div>
        <form action="${pageContext.request.contextPath}/accounts" method="post" style="border: 2px solid red; padding: 10px; margin: 20px 0;">
            <input type="hidden" name="action" value="create">
            <input type="hidden" name="customerId" value="${param.customerId}">
            <input type="hidden" name="accountType" value="checking">
            <input type="hidden" name="initialDeposit" value="100">
            <button type="submit">TEST SUBMIT</button>
        </form>
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/accounts" method="post">
                <input type="hidden" name="action" value="create">
                <input type="hidden" name="customerId" value="${param.customerId}">

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
            <script>
                document.addEventListener('DOMContentLoaded', function() {
                    document.querySelector('form').addEventListener('submit', function(e) {
                        console.log('Form submitted to: ' + this.action);
                        // For debugging, let's also log the form data
                        const formData = new FormData(this);
                        for (const [key, value] of formData.entries()) {
                            console.log(key + ': ' + value);
                        }
                    });
                });
            </script>
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

