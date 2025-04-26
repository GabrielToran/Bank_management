<!-- webapp/WEB-INF/views/error/500.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bank Management System - Server Error</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5 text-center">
    <h1 class="text-danger">500 - Server Error</h1>
    <p class="lead">Something went wrong on our end. Please try again later.</p>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Return to Dashboard</a>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
