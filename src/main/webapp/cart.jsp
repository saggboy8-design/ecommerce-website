<%@ include file="includes/header.jsp" %>
<%@ page import="java.util.List, com.ecommerce.model.CartItem" %>
<h2>Shopping Cart</h2>
<%
    List<CartItem> cartItems = (List<CartItem>) request.getAttribute("cartItems");
    double grandTotal = 0;
    if (cartItems != null && !cartItems.isEmpty()) {
%>
<table class="table table-striped">
    <thead>
        <tr><th>Product</th><th>Price</th><th>Quantity</th><th>Subtotal</th><th>Action</th></tr>
    </thead>
    <tbody>
    <% for(CartItem ci : cartItems) {
        double subtotal = ci.getProduct().getPrice() * ci.getQuantity();
        grandTotal += subtotal;
    %>
        <tr>
            <td><%= ci.getProduct().getName() %></td>
            <td>$<%= String.format("%.2f", ci.getProduct().getPrice()) %></td>
            <td>
                <form action="cart" method="post" class="d-flex">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="cartId" value="<%= ci.getId() %>">
                    <input type="number" name="quantity" value="<%= ci.getQuantity() %>" min="1" class="form-control form-control-sm" style="width:70px;">
                    <button type="submit" class="btn btn-sm btn-outline-secondary ms-1">Update</button>
                </form>
            </td>
            <td>$<%= String.format("%.2f", subtotal) %></td>
            <td>
                <form action="cart" method="post">
                    <input type="hidden" name="action" value="remove">
                    <input type="hidden" name="cartId" value="<%= ci.getId() %>">
                    <button type="submit" class="btn btn-sm btn-danger">Remove</button>
                </form>
            </td>
        </tr>
    <% } %>
    </tbody>
</table>
<h4>Total: $<%= String.format("%.2f", grandTotal) %></h4>
<form action="orders" method="post">
    <button type="submit" class="btn btn-primary">Proceed to Checkout</button>
</form>
<% } else { %>
    <p>Your cart is empty. <a href="products">Continue shopping</a></p>
<% } %>
<%@ include file="includes/footer.jsp" %>