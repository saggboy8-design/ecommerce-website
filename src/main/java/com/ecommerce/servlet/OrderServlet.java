package com.ecommerce.servlet;

import com.ecommerce.dao.*;
import com.ecommerce.model.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@javax.servlet.annotation.WebServlet("/orders")
public class OrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        try {
            CartDAO cartDAO = new CartDAO();
            List<CartItem> cartItems = cartDAO.getCartItems(user.getId());
            if (cartItems.isEmpty()) {
                response.sendRedirect("cart");
                return;
            }
            double total = cartItems.stream()
                    .mapToDouble(ci -> ci.getProduct().getPrice() * ci.getQuantity())
                    .sum();
            OrderDAO orderDAO = new OrderDAO();
            int orderId = orderDAO.createOrder(user.getId(), total, cartItems);
            response.sendRedirect("orders");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        try {
            OrderDAO orderDAO = new OrderDAO();
            List<Order> orders = orderDAO.getOrdersByUser(user.getId());
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("order-history.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}