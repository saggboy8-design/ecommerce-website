package com.ecommerce.servlet;

import com.ecommerce.dao.CartDAO;
import com.ecommerce.model.User;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

@javax.servlet.annotation.WebServlet("/cart")
public class CartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        String action = request.getParameter("action");
        CartDAO cartDAO = new CartDAO();
        try {
            if ("add".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cartDAO.addToCart(user.getId(), productId, quantity);
            } else if ("update".equals(action)) {
                int cartId = Integer.parseInt(request.getParameter("cartId"));
                int newQty = Integer.parseInt(request.getParameter("quantity"));
                cartDAO.updateQuantity(cartId, newQty);
            } else if ("remove".equals(action)) {
                int cartId = Integer.parseInt(request.getParameter("cartId"));
                cartDAO.removeItem(cartId);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        // redirect to cart display page
        response.sendRedirect("cart");
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
            CartDAO cartDAO = new CartDAO();
            request.setAttribute("cartItems", cartDAO.getCartItems(user.getId()));
            request.getRequestDispatcher("cart.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}