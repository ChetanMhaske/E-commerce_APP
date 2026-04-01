package com.example.e_commerceapp;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class OrderManager {
    private static OrderManager instance;
    private List<Order> orders;
    private static final String PREFS_NAME = "OrderHistory";
    private static final String ORDERS_KEY = "orders";
    private Context context;

    private OrderManager() {
        orders = new ArrayList<>();
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }
    
    public void init(Context context) {
        this.context = context;
        loadOrders();
    }

    public void addOrder(String paymentMethod, List<CartItem> items, double totalAmount) {
        String orderId = "ORD" + new Random().nextInt(100000);
        String orderDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        
        // Calculate delivery date (3-5 days from now)
        long deliveryTime = System.currentTimeMillis() + (3 + new Random().nextInt(3)) * 24 * 60 * 60 * 1000L;
        String deliveryDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date(deliveryTime));
        
        String status = "Confirmed";
        
        // Create copy of items to avoid reference issues
        List<CartItem> orderItems = new ArrayList<>();
        for (CartItem item : items) {
            orderItems.add(new CartItem(item.getProduct(), item.getQuantity()));
        }
        
        Order order = new Order(orderId, orderDate, deliveryDate, status, paymentMethod, totalAmount, orderItems);
        orders.add(0, order); // Add to beginning for newest first
        saveOrders();
    }
    
    private void saveOrders() {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String ordersJson = gson.toJson(orders);
            prefs.edit().putString(ORDERS_KEY, ordersJson).apply();
        }
    }
    
    private void loadOrders() {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String ordersJson = prefs.getString(ORDERS_KEY, null);
            if (ordersJson != null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Order>>(){}.getType();
                List<Order> savedOrders = gson.fromJson(ordersJson, listType);
                if (savedOrders != null) {
                    orders = savedOrders;
                }
            }
        }
    }

    public List<Order> getOrders() {
        return orders;
    }
}