<!-- webapp/WEB-INF/views/customer/new.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bank Management System - Add New Customer</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h1>Add New Customer</h1>

    <div class="mb-3">
        <a href="<url value='/customers'/>" class="btn btn-secondary">Back to Customers</a>
    </div>

    <div class="card">
        <div class="card-header">
            <h5>Customer Information</h5>
        </div>
        <div class="card-body">
            <form action="<url value='/customers'/>" method="post">
                <div class="mb-3">
                    <label for="firstName" class="form-label">First Name</label>
                    <input type="text" class="form-control" id="firstName" name="firstName" required>
                </div>
                <div class="mb-3">
                    <label for="lastName" class="form-label">Last Name</label>
                    <input type="text" class="form-control" id="lastName" name="lastName" required>
                </div>
                <div class="mb-3">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" name="email" required>
                </div>
                <div class="mb-3">
                    <label for="phone" class="form-label">Phone</label>
                    <input type="tel" class="form-control" id="phone" name="phone" required>
                </div>
                <div class="mb-3">
                    <label for="address" class="form-label">Address</label>
                    <textarea class="form-control" id="address" name="address" rows="3" required></textarea>
                </div>
                <button type="submit" class="btn btn-primary">Add Customer</button>
            </form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
