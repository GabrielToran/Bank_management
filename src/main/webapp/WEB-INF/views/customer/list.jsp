<!-- webapp/WEB-INF/views/customer/list.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Bank Management System - Customer List</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-4">
    <h1>Customer List</h1>

    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Back to Dashboard</a>
        <a href="${pageContext.request.contextPath}/customers/new" class="btn btn-success">Add New Customer</a>
    </div>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="customer" items="${customers}">
            <tr>
                <td>${customer.customerId}</td>
                <td>${customer.firstName} ${customer.lastName}</td>
                <td>${customer.email}</td>
                <td>${customer.phone}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/customers/${customer.customerId}" class="btn btn-sm btn-info">View</a>
                    <a href="${pageContext.request.contextPath}/accounts/customerId=${customer.customerId}" class="btn btn-sm btn-primary">Accounts</a>
                    <a href="${pageContext.request.contextPath}/customers/edit/${customer.customerId}" class="btn btn-sm btn-warning">Edit</a>
                    <button class="btn btn-sm btn-danger" onclick="deleteCustomer(${customer.customerId})">Delete</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    function deleteCustomer(customerId) {
        if (confirm('Are you sure you want to delete this customer?')) {
            const xhr = new XMLHttpRequest();
            xhr.open('DELETE', '${pageContext.request.contextPath}/customers/' + customerId);
            xhr.onload = function() {
                if (xhr.status === 200) {
                    alert('Customer deleted successfully');
                    window.location.reload();
                } else {
                    alert('Failed to delete customer');
                }
            };
            xhr.send();
        }
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

