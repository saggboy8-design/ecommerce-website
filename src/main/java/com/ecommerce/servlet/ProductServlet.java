package com.ecommerce.servlet;

import com.ecommerce.dao.ProductDAO;
import com.ecommerce.model.Product;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@javax.servlet.annotation.WebServlet("/products")
public class ProductServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productId = request.getParameter("id");
        ProductDAO dao = new ProductDAO();
        try {
            if (productId != null) {
                Product product = dao.getProductById(Integer.parseInt(productId));
                request.setAttribute("product", product);
                request.getRequestDispatcher("product-detail.jsp").forward(request, response);
            } else {
                List<Product> products = dao.getAllProducts();
                request.setAttribute("products", products);
                request.getRequestDispatcher("products.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}