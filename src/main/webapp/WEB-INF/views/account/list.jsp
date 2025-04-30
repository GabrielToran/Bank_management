<!-- WEB-INF/views/account/list.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Accounts</title>
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

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h1>Accounts for ${customer.firstName} ${customer.lastName}</h1>
        <a href="${pageContext.request.contextPath}/accounts/create?customerId=${customer.customerId}" class="btn btn-primary">Add Account</a>
    </div>
    <table class="table table-hover">
        <thead><tr><th>ID</th><th>Type</th><th>Balance</th><th>Actions</th></tr></thead>
        <tbody>
        <c:forEach var="a" items="${accounts}">
            <tr>
                <td>${a.accountId}</td>
                <td>${requestScope['accountType_' += a.accountId]}</td>
                <td>$${a.balance}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/accounts/${a.accountId}" class="btn btn-sm btn-info">View</a>
                    <button type="button" class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteAcct${a.accountId}">Delete</button>
                    <!-- Delete Modal -->
                    <div class="modal fade" id="deleteAcct${a.accountId}" tabindex="-1">
                        <div class="modal-dialog"><div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Confirm Delete</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                            </div>
                            <div class="modal-body">
                                Delete account #${a.accountId}?
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <form method="post" action="${pageContext.request.contextPath}/accounts/${a.accountId}" style="display:inline;">
                                    <input type="hidden" name="_method" value="delete" />
                                    <button type="submit" class="btn btn-danger">Delete</button>
                                </form>
                            </div>
                        </div></div>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>