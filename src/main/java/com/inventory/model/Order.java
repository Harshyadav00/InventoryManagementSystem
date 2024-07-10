package main.java.com.inventory.model;

import java.util.Date;
import java.util.List;

public class Order {

    private int orderID;
    private Date orderDate;
    private String customerName;
    private List<OrderItem> orderItems;



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(orderID).append("\n");
        sb.append("Order Date: ").append(orderDate).append("\n");
        sb.append("Customer Name: ").append(customerName).append("\n");
        sb.append("Order Items:\n");
        for (OrderItem item : orderItems) {
            sb.append(item).append("\n");
        }
        return sb.toString();
    }


}
