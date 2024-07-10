package main.java.com.inventory;

import main.java.com.inventory.dao.OrderManagementSystem;
import main.java.com.inventory.dao.ProductDao;
import main.java.com.inventory.model.Order;
import main.java.com.inventory.model.Product;

import java.sql.SQLException;
import java.util.Scanner;

public class InventoryApp {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("Inventory Management System");
            System.out.println("1. Product Management -> Add, Update, Delete, and view Products");
            System.out.println("2. Stock Management -> add, update, and retrieve stock Details");
            System.out.println("3. Order Processing -> create and view Orders");
            System.out.println("4. Generate Sales Report");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            OrderManagementSystem oms = new OrderManagementSystem();

            switch (choice) {
                case 1:
                    productManagement();
                    break;
                case 2:
                    stockManagement();
                    break;
                case 3:
                    orderProcessing();
                    break;
                case 4:
                    oms.generateSalesReport();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 5);
    }

    private static void productManagement() {

        System.out.println("Product Management ");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.println("4. View Product");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        OrderManagementSystem oms = new OrderManagementSystem();


        try {
            switch (choice) {
                case 1:
                    oms.addProduct();
                    break;
                case 2:
                    oms.updateProduct();
                    break;
                case 3:
                    oms.deleteProduct();
                    break;
                case 4:
                    oms.viewProduct();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void stockManagement() {
        System.out.println("Stock Management");
        System.out.println("1. Add Stock");
        System.out.println("2. Update Stock");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        OrderManagementSystem oms = new OrderManagementSystem();


        switch (choice) {
            case 1:
                oms.addStock();
                break;
            case 2:
                oms.updateStock();
                break;
            case 3:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void orderProcessing() {
        System.out.println("Order Management");
        System.out.println("1. Create Order");
        System.out.println("2. View Order");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        OrderManagementSystem oms = new OrderManagementSystem();


        switch (choice) {
            case 1:
                oms.createOrder();
                break;
            case 2:
                oms.viewOrderDetails();
                break;
            case 3:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

}
