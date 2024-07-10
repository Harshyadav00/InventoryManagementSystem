package main.java.com.inventory.dao;


import main.java.com.inventory.model.Product;
import main.java.com.inventory.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductDao {

    public void addProduct(Product product){
        String query = "Insert into products(name, description, price, stockQuantity) values (?,?,?,?)" ;

        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)){
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDesc());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getStockQuantity());

            pstmt.executeUpdate() ;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product){
        String query = "Update products set name = ?, description=?, price=?, stockQuantity=? where productID = ?" ;

        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDesc());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getStockQuantity());
            pstmt.setInt(5, product.getId());

            pstmt.executeUpdate() ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int productId){
        String query = " Delete from products where productID = ?" ;

        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, productId);

            pstmt.executeUpdate() ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Product getProduct(int productId) {
        String query = "Select * from products where productID = ?";

        try (Connection con = JDBCUtil.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query) ) {
            pstmt.setInt(1, productId);

            ResultSet rs = pstmt.executeQuery() ;

            if(rs.next() ) {
                return new Product(
                        rs.getInt("productID"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("stockQuantity")
                        );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null ;

    }

}
