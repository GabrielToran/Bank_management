<!-- WEB-INF/views/customer/create.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Create Customer</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />
<div class="container mt-4">
  <h1>Create Customer</h1>
  <c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">${errorMessage}</div>
  </c:if>
  <form method="post" action="${pageContext.request.contextPath}/customers">
    <div class="mb-3">
      <label class="form-label">First Name</label>
      <input type="text" class="form-control" name="firstName" required />
    </div>
    <div class="mb-3">
      <label class="form-label">Last Name</label>
      <input type="text" class="form-control" name="lastName" required />
    </div>
    <div class="mb-3">
      <label class="form-label">Email</label>
      <input type="email" class="form-control" name="email" required />
    </div>
    <div class="mb-3">
      <label class="form-label">Phone</label>
      <input type="text" class="form-control" name="phone" />
    </div>
    <div class="mb-3">
      <label class="form-label">Address</label>
      <textarea class="form-control" name="address"></textarea>
    </div>
    <button type="submit" class="btn btn-primary">Create</button>
    <a href="${pageContext.request.contextPath}/customers" class="btn btn-secondary">Cancel</a>
  </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>