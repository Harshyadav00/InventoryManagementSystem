package main.java.com.inventory.model;

public class OrderItem {

    private int productID;
    private String productName;
    private int quantity;
    private double price;

    // Getters and Setters...

    @Override
    public String toString() {
        return "Product ID: " + productID + ", Product Name: " + productName + ", Quantity: " + quantity + ", Price: " + price;
    }

}
