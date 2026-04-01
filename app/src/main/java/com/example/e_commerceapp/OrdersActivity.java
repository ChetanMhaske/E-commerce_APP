package com.example.e_commerceapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrdersActivity extends AppCompatActivity {
    
    private RecyclerView ordersRecyclerView;
    private TextView emptyOrdersText;
    private ImageView backButton;
    private OrdersAdapter ordersAdapter;
    private OrderManager orderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        
        initViews();
        setupRecyclerView();
        updateOrdersUI();
    }

    private void initViews() {
        orderManager = OrderManager.getInstance();
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        emptyOrdersText = findViewById(R.id.emptyOrdersText);
        backButton = findViewById(R.id.backButton);
        
        backButton.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        ordersAdapter = new OrdersAdapter(this, orderManager.getOrders());
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersRecyclerView.setAdapter(ordersAdapter);
    }

    private void updateOrdersUI() {
        if (orderManager.getOrders().isEmpty()) {
            emptyOrdersText.setVisibility(android.view.View.VISIBLE);
            ordersRecyclerView.setVisibility(android.view.View.GONE);
        } else {
            emptyOrdersText.setVisibility(android.view.View.GONE);
            ordersRecyclerView.setVisibility(android.view.View.VISIBLE);
        }
        ordersAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateOrdersUI();
    }
}