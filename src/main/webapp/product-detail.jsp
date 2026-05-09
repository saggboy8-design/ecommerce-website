<%@ include file="includes/header.jsp" %>
<%@ page import="com.ecommerce.model.Product, com.ecommerce.model.User" %>
<%
    Product product = (Product) request.getAttribute("product");
    User user = (User) session.getAttribute("user");
%>
<div class="row">
    <div class="col-md-6">
        <img src="images/<%= product.getImage() %>" class="img-fluid" alt="<%= product.getName() %>">
    </div>
    <div class="col-md-6">
        <h2><%= product.getName() %></h2>
        <h4 class="text-muted">$<%= String.format("%.2f", product.getPrice()) %></h4>
        <% if (user != null) { %>
        <form action="cart" method="post" class="mt-3">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="productId" value="<%= product.getId() %>">
            <div class="input-group mb-3">
                <input type="number" name="quantity" value="1" min="1" class="form-control" style="max-width:80px;">
                <button type="submit" class="btn btn-success">Add to Cart</button>
            </div>
        </form>
        <% } else { %>
            <a href="login" class="btn btn-warning">Login to Buy</a>
        <% } %>
    </div>
</div>
<%@ include file="includes/footer.jsp" %>