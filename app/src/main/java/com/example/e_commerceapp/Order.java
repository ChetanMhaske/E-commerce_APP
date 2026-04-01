package com.example.e_commerceapp;

import java.util.List;

public class Order {
    private String orderId;
    private String orderDate;
    private String deliveryDate;
    private String status;
    private String paymentMethod;
    private double totalAmount;
    private List<CartItem> items;

    public Order(String orderId, String orderDate, String deliveryDate, String status, 
                 String paymentMethod, double totalAmount, List<CartItem> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getOrderDate() { return orderDate; }
    public String getDeliveryDate() { return deliveryDate; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getTotalAmount() { return totalAmount; }
    public List<CartItem> getItems() { return items; }
    
    public int getItemCount() {
        int count = 0;
        for (CartItem item : items) {
            count += item.getQuantity();
        }
        return count;
    }
}