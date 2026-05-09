<%@ include file="includes/header.jsp" %>
<%@ page import="java.util.List, com.ecommerce.model.Product" %>
<h2>Our Products</h2>
<div class="row">
<%
    List<Product> products = (List<Product>) request.getAttribute("products");
    for(Product p : products) {
%>
    <div class="col-md-4 mb-4">
        <div class="card h-100">
            <img src="images/<%= p.getImage() %>" class="card-img-top" alt="<%= p.getName() %>" style="height:200px; object-fit:cover;">
            <div class="card-body">
                <h5 class="card-title"><%= p.getName() %></h5>
                <p class="card-text">$<%= String.format("%.2f", p.getPrice()) %></p>
                <a href="products?id=<%= p.getId() %>" class="btn btn-outline-primary">Details</a>
            </div>
        </div>
    </div>
<% } %>
</div>
<%@ include file="includes/footer.jsp" %>