package com.ecommerce.dao;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // Add or update quantity
    public void addToCart(int userId, int productId, int quantity) throws SQLException {
        String checkSql = "SELECT id, quantity FROM cart WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, userId);
            checkPs.setInt(2, productId);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    // update quantity
                    int newQty = rs.getInt("quantity") + quantity;
                    String updateSql = "UPDATE cart SET quantity = ? WHERE id = ?";
                    try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                        updatePs.setInt(1, newQty);
                        updatePs.setInt(2, rs.getInt("id"));
                        updatePs.executeUpdate();
                    }
                } else {
                    String insertSql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
                    try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                        insertPs.setInt(1, userId);
                        insertPs.setInt(2, productId);
                        insertPs.setInt(3, quantity);
                        insertPs.executeUpdate();
                    }
                }
            }
        }
    }

    public List<CartItem> getCartItems(int userId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.id, c.quantity, p.id as pid, p.name, p.price, p.image " +
                     "FROM cart c JOIN products p ON c.product_id = p.id " +
                     "WHERE c.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem ci = new CartItem();
                    ci.setId(rs.getInt("id"));
                    ci.setUserId(userId);
                    ci.setProductId(rs.getInt("pid"));
                    ci.setQuantity(rs.getInt("quantity"));
                    Product p = new Product(rs.getInt("pid"), rs.getString("name"),
                                            rs.getDouble("price"), rs.getString("image"));
                    ci.setProduct(p);
                    items.add(ci);
                }
            }
        }
        return items;
    }

    public void updateQuantity(int cartId, int newQuantity) throws SQLException {
        String sql = "UPDATE cart SET quantity = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartId);
            ps.executeUpdate();
        }
    }

    public void removeItem(int cartId) throws SQLException {
        String sql = "DELETE FROM cart WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        }
    }

    public void clearCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    public int getCartItemCount(int userId) throws SQLException {
        String sql = "SELECT SUM(quantity) FROM cart WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}