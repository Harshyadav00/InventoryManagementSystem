package main.java.com.inventory.model;

public class Product {

    private int id;
    private String name;
    private String desc;
    private double price ;
    private int stockQuantity;

    public Product(int id, String name, String desc, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public int getId() {
        return id;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public double getPrice() {
        return price;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }
}
