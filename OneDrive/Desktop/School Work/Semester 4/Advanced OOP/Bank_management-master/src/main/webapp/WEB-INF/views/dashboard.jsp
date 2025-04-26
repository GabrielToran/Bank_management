<!-- webapp/WEB-INF/views/dashboard.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bank Management System - Dashboard</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h1>Bank Management System</h1>
    <div class="row mt-4">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h5>Quick Stats</h5>
                </div>
                <div class="card-body">
                    <p>Total Customers: ${customerCount}</p>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h5>Quick Actions</h5>
                </div>
                <div class="card-body">
                    <a href="<url value='/customers'/>" class="btn btn-primary">View All Customers</a>
                    <a href="<url value='/customers/new'/>" class="btn btn-success">Add New Customer</a>
                </div>
            </div>
        </div>
    </div>

    <div class="mt-4">
        <h3>Recent Customers</h3>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <forEach var="customer" items="${customers}" end="4">
                <tr>
                    <td>${customer.customerId}</td>
                    <td>${customer.firstName} ${customer.lastName}</td>
                    <td>${customer.email}</td>
                    <td>
                        <a href="<url value='/customers/${customer.customerId}'/>" class="btn btn-sm btn-info">View</a>
                        <a href="<url value='/accounts?customerId=${customer.customerId}'/>" class="btn btn-sm btn-primary">Accounts</a>
                    </td>
                </tr>
            </forEach>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>










