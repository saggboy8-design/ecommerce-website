package com.ecommerce.dao;

import com.ecommerce.model.*;
import com.ecommerce.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int createOrder(int userId, double totalPrice, List<CartItem> cartItems) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // Insert order
            String orderSql = "INSERT INTO orders (user_id, total_price) VALUES (?, ?)";
            PreparedStatement orderPs = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderPs.setInt(1, userId);
            orderPs.setDouble(2, totalPrice);
            orderPs.executeUpdate();
            ResultSet generatedKeys = orderPs.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Creating order failed, no ID obtained.");
            }
            int orderId = generatedKeys.getInt(1);
            generatedKeys.close();
            orderPs.close();

            // Insert order items
            String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement itemPs = conn.prepareStatement(itemSql);
            for (CartItem ci : cartItems) {
                itemPs.setInt(1, orderId);
                itemPs.setInt(2, ci.getProductId());
                itemPs.setInt(3, ci.getQuantity());
                itemPs.setDouble(4, ci.getProduct().getPrice());
                itemPs.addBatch();
            }
            itemPs.executeBatch();
            itemPs.close();

            // Clear the user's cart after order
            CartDAO cartDAO = new CartDAO();
            cartDAO.clearCart(userId);

            conn.commit();
            return orderId;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public List<Order> getOrdersByUser(int userId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String orderSql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(orderSql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUserId(rs.getInt("user_id"));
                    order.setTotalPrice(rs.getDouble("total_price"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    // fetch items for each order
                    order.setItems(getOrderItems(rs.getInt("id")));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    private List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.*, p.name, p.image FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem oi = new OrderItem();
                    oi.setId(rs.getInt("id"));
                    oi.setOrderId(rs.getInt("order_id"));
                    oi.setProductId(rs.getInt("product_id"));
                    oi.setQuantity(rs.getInt("quantity"));
                    oi.setPrice(rs.getDouble("price"));
                    Product p = new Product(rs.getInt("product_id"), rs.getString("name"),
                                            0.0, rs.getString("image"));
                    oi.setProduct(p);
                    items.add(oi);
                }
            }
        }
        return items;
    }
}