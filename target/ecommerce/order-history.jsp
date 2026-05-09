<%@ include file="includes/header.jsp" %>
<%@ page import="java.util.List, com.ecommerce.model.Order, com.ecommerce.model.OrderItem" %>
<h2>Your Orders</h2>
<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    if (orders != null && !orders.isEmpty()) {
        for(Order order : orders) {
%>
    <div class="card mb-3">
        <div class="card-header">
            Order #<%= order.getId() %> | Date: <%= order.getOrderDate() %> | Total: $<%= String.format("%.2f", order.getTotalPrice()) %>
        </div>
        <div class="card-body">
            <table class="table table-sm">
                <thead><tr><th>Product</th><th>Price</th><th>Qty</th><th>Subtotal</th></tr></thead>
                <tbody>
                <% for(OrderItem oi : order.getItems()) { %>
                    <tr>
                        <td><%= oi.getProduct().getName() %></td>
                        <td>$<%= String.format("%.2f", oi.getPrice()) %></td>
                        <td><%= oi.getQuantity() %></td>
                        <td>$<%= String.format("%.2f", oi.getPrice() * oi.getQuantity()) %></td>
                    </tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
<%
        }
    } else {
%>
    <p>No orders yet. <a href="products">Shop now</a></p>
<% } %>
<%@ include file="includes/footer.jsp" %>