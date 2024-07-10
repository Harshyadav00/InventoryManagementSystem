package main.java.com.inventory.dao;

import main.java.com.inventory.util.JDBCUtil;

import java.io.*;
import java.sql.*;
import java.util.*;

public class OrderProcessor {

    public void createOrder(String customerName, Map<Integer, Integer> productsAndQuantities) {
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
            conn.setAutoCommit(false);

            // Check stock quantities before proceeding
            if (!areQuantitiesAvailable(conn, productsAndQuantities)) {
                System.out.println("Insufficient stock for one or more products.");
                rollbackConnection(conn);
                return;
            }

            int orderID = insertOrder(conn, customerName, productsAndQuantities);
            if (orderID != -1) {
                insertOrderItems(conn, orderID, productsAndQuantities);
                updateStockQuantities(conn, productsAndQuantities);
                conn.commit();
                System.out.println("Order created successfully with ID: " + orderID);
            } else {
                System.out.println("Failed to create order.");
            }

        } catch (SQLException e) {
            rollbackConnection(conn);
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

    private boolean areQuantitiesAvailable(Connection conn, Map<Integer, Integer> productsAndQuantities) throws SQLException {
        String sql = "SELECT productID, stockQuantity FROM products WHERE productID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            for (Map.Entry<Integer, Integer> entry : productsAndQuantities.entrySet()) {
                int productID = entry.getKey();
                int requestedQuantity = entry.getValue();

                statement.setInt(1, productID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int availableQuantity = resultSet.getInt("stockQuantity");
                        if (availableQuantity < requestedQuantity) {
                            return false;
                        }
                    } else {
                        // Product not found
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int insertOrder(Connection conn, String customerName, Map<Integer, Integer> productsAndQuantities) throws SQLException {
        String sql = "INSERT INTO orders (customerName, totalAmount) VALUES (?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            double totalAmount = calculateTotalAmount(conn, productsAndQuantities);
            statement.setString(1, customerName);
            statement.setDouble(2, totalAmount);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        }
    }

    private void insertOrderItems(Connection conn, int orderID, Map<Integer, Integer> productsAndQuantities) throws SQLException {
        String sql = "INSERT INTO order_items (orderID, productID, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            for (Map.Entry<Integer, Integer> entry : productsAndQuantities.entrySet()) {
                int productID = entry.getKey();
                int quantity = entry.getValue();
                statement.setInt(1, orderID);
                statement.setInt(2, productID);
                statement.setInt(3, quantity);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private void updateStockQuantities(Connection conn, Map<Integer, Integer> productsAndQuantities) throws SQLException {
        String sql = "UPDATE products SET stockQuantity = stockQuantity - ? WHERE productID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            for (Map.Entry<Integer, Integer> entry : productsAndQuantities.entrySet()) {
                int productID = entry.getKey();
                int quantity = entry.getValue();
                statement.setInt(1, quantity);
                statement.setInt(2, productID);
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    private double calculateTotalAmount(Connection conn, Map<Integer, Integer> productsAndQuantities) throws SQLException {
        double total = 0.0;
        String sql = "SELECT price FROM products WHERE productID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            for (Map.Entry<Integer, Integer> entry : productsAndQuantities.entrySet()) {
                int productID = entry.getKey();
                int quantity = entry.getValue();
                statement.setInt(1, productID);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        double price = resultSet.getDouble("price");
                        total += price * quantity;
                    } else {
                        throw new SQLException("Product with ID " + productID + " not found.");
                    }
                }
            }
        }
        return total;
    }

    private void rollbackConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true); // Restore auto-commit mode
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void getOrderInfo(int orderID) {
        String orderSql = "SELECT * FROM orders WHERE orderID = ?";
        String orderItemSql = "SELECT * FROM order_items WHERE orderID = ?";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement orderStatement = conn.prepareStatement(orderSql);
             PreparedStatement orderItemStatement = conn.prepareStatement(orderItemSql)) {

            // Retrieve order information
            orderStatement.setInt(1, orderID);
            try (ResultSet orderResultSet = orderStatement.executeQuery()) {
                if (orderResultSet.next()) {
                    String customerName = orderResultSet.getString("customerName");
                    double totalAmount = orderResultSet.getDouble("totalAmount");
                    Timestamp orderDate = orderResultSet.getTimestamp("orderDate");

                    System.out.println("Order ID: " + orderID);
                    System.out.println("Customer Name: " + customerName);
                    System.out.println("Total Amount: " + totalAmount);
                    System.out.println("Order Date: " + orderDate);

                    // Retrieve order items
                    orderItemStatement.setInt(1, orderID);
                    try (ResultSet orderItemResultSet = orderItemStatement.executeQuery()) {
                        System.out.println("Order Items:");
                        while (orderItemResultSet.next()) {
                            int productID = orderItemResultSet.getInt("productID");
                            int quantity = orderItemResultSet.getInt("quantity");
                            System.out.println("Product ID: " + productID + ", Quantity: " + quantity);
                        }
                    }

                } else {
                    System.out.println("Order with ID " + orderID + " not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void generateSalesReport(String filePath) {
        String sql = "SELECT o.orderID, o.orderDate, o.customerName, oi.productID, p.name, oi.quantity, p.price " +
                "FROM orders o " +
                "JOIN order_items oi ON o.orderID = oi.orderID " +
                "JOIN products p ON oi.productID = p.productID " ;



        try (Connection conn = JDBCUtil.getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            CSVExporter csvExporter = new CSVExporter();
            csvExporter.exportToCSV(filePath, resultSet);
            System.out.println("Sales report generated successfully at: " + filePath);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


}
