package main.java.com.inventory.dao;

import main.java.com.inventory.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockDao {

    public void addStock(int productId, int quantityToAdd) {
        String sql = "UPDATE products SET stockQuantity = stockQuantity + ? WHERE productID = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, quantityToAdd);
            statement.setInt(2, productId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println(quantityToAdd + " units of stock added for Product ID " + productId);
            } else {
                System.out.println("Failed to add stock for Product ID " + productId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStock(int productId, int newStockQuantity) {
        String sql = "UPDATE products SET stockQuantity = ? WHERE productID = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, newStockQuantity);
            statement.setInt(2, productId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Stock quantity updated for Product ID " + productId);
            } else {
                System.out.println("Failed to update stock quantity for Product ID " + productId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getStockInfo(int productId) {
        String sql = "SELECT * FROM products WHERE productID = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int stockQuantity = resultSet.getInt("stockQuantity");

                    System.out.println("Product ID: " + productId);
                    System.out.println("Name: " + name);
                    System.out.println("Stock Quantity: " + stockQuantity);
                } else {
                    System.out.println("Product ID " + productId + " not found");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
