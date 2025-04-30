<!-- WEB-INF/views/customer/view.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>View Customer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />
<div class="container mt-4">
    <h1>Customer Details</h1>
    <dl class="row">
        <dt class="col-sm-3">ID</dt><dd class="col-sm-9">${customer.customerId}</dd>
        <dt class="col-sm-3">Name</dt><dd class="col-sm-9">${customer.firstName} ${customer.lastName}</dd>
        <dt class="col-sm-3">Email</dt><dd class="col-sm-9">${customer.email}</dd>
        <dt class="col-sm-3">Phone</dt><dd class="col-sm-9">${customer.phone}</dd>
        <dt class="col-sm-3">Address</dt><dd class="col-sm-9">${customer.address}</dd>
    </dl>
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/accounts?customerId=${customer.customerId}" class="btn btn-primary">View Accounts</a>
        <a href="${pageContext.request.contextPath}/customers/${customer.customerId}/edit" class="btn btn-warning">Edit</a>
        <a href="${pageContext.request.contextPath}/customers" class="btn btn-secondary">Back to List</a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>