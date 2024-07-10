package main.java.com.inventory.dao;

import main.java.com.inventory.model.Product;

import java.sql.SQLException;
import java.util.*;

public class OrderManagementSystem {

    private final OrderProcessor orderProcessor ;
    private final ProductDao productDao  ;
    private final StockDao stockDao  ;
    private final Scanner scanner;

    public OrderManagementSystem() {
        this.orderProcessor = new OrderProcessor();
        this.productDao = new ProductDao();
        this.stockDao = new StockDao() ;
        this.scanner = new Scanner(System.in);
    }

    public void addProduct() throws SQLException {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product description: ");
        String description = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter product stock quantity: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Product product = new Product(0, name, description, price, stockQuantity);
        productDao.addProduct(product);
        System.out.println("Product added successfully!");
    }

    public void updateProduct() throws SQLException {
        System.out.print("Enter product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter new product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new product description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new product stock quantity: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Product product = new Product(id, name, description, price, stockQuantity);
        productDao.updateProduct(product);
        System.out.println("Product updated successfully!");
    }

    public void deleteProduct() throws SQLException {
        System.out.print("Enter product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        productDao.deleteProduct(id);
        System.out.println("Product deleted successfully!");
    }

    public void viewProduct() throws SQLException {
        System.out.print("Enter product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        Product product = productDao.getProduct(id);
        if (product != null) {
            System.out.println("Product Details:");
            System.out.println("ID: " + product.getId());
            System.out.println("Name: " + product.getName());
            System.out.println("Description: " + product.getDesc());
            System.out.println("Price: " + product.getPrice());
            System.out.println("Stock Quantity: " + product.getStockQuantity());
        } else {
            System.out.println("Product not found!");
        }
    }

    public void addStock() {
        System.out.println("\nAdding Stock");
        System.out.println("Enter product ID to update:");
        int productID = scanner.nextInt();

        System.out.println("Enter stock quantity to add:");
        int addQuantity = scanner.nextInt();

        stockDao.addStock(productID, addQuantity);
    }

    public void updateStock() {
        System.out.println("\nUpdating Stock");
        System.out.println("Enter product ID to update:");
        int productID = scanner.nextInt();

        System.out.println("Enter new stock quantity:");
        int newQuantity = scanner.nextInt();

        stockDao.updateStock(productID, newQuantity);
    }

    public void createOrder() {
        System.out.println("Enter customer name:");
        String customerName = scanner.nextLine();

        Map<Integer, Integer> productsAndQuantities = new HashMap<>();
        boolean addMoreProducts = true;

        while (addMoreProducts) {
            System.out.println("Enter product ID:");
            int productID = scanner.nextInt();

            System.out.println("Enter quantity:");
            int quantity = scanner.nextInt();

            productsAndQuantities.put(productID, quantity);

            System.out.println("Do you want to add more products? (yes/no)");
            String choice = scanner.next();
            if (!choice.equalsIgnoreCase("yes")) {
                addMoreProducts = false;
            }
        }

        orderProcessor.createOrder(customerName, productsAndQuantities);
    }

    public void viewOrderDetails() {
        System.out.println("\nViewing Order Details");
        System.out.println("Enter order ID:");
        int orderID = scanner.nextInt();

        orderProcessor.getOrderInfo(orderID);

    }

    public void generateSalesReport() {
        String filePath = "sales_report.csv";
        orderProcessor.generateSalesReport(filePath);
    }

}
