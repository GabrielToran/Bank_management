<!-- WEB-INF/views/transaction/view.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Transaction Details</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/navbar.jsp" />
<div class="container mt-4">
  <h1>Transaction #${transaction.transactionId}</h1>
  <dl class="row">
    <dt class="col-sm-3">Type</dt><dd class="col-sm-9">${transaction.type}</dd>
    <dt class="col-sm-3">Amount</dt><dd class="col-sm-9">$${transaction.amount}</dd>
    <dt class="col-sm-3">Date</dt><dd class="col-sm-9">${transaction.date}</dd>
    <dt class="col-sm-3">Description</dt><dd class="col-sm-9">${transaction.description}</dd>
  </dl>
  <a href="${pageContext.request.contextPath}/transactions?accountId=${transaction.accountId}" class="btn btn-secondary">Back to Transactions</a>
</div>
</body>
</html>
</body>
</html>
