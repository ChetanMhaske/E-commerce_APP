package com.example.e_commerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class OrderSuccessActivity extends AppCompatActivity {
    
    private TextView orderIdText, deliveryDateText, paymentMethodText;
    private AppCompatButton continueShoppingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        
        initViews();
        setupOrderDetails();
        setupClickListeners();
    }

    private void initViews() {
        orderIdText = findViewById(R.id.orderIdText);
        deliveryDateText = findViewById(R.id.deliveryDateText);
        paymentMethodText = findViewById(R.id.paymentMethodText);
        continueShoppingBtn = findViewById(R.id.continueShoppingBtn);
    }

    private void setupOrderDetails() {
        // Generate random order ID
        String orderId = "ORD" + new Random().nextInt(100000);
        orderIdText.setText("Order ID: " + orderId);
        
        // Calculate delivery date (3-5 days from now)
        long deliveryTime = System.currentTimeMillis() + (3 + new Random().nextInt(3)) * 24 * 60 * 60 * 1000L;
        String deliveryDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date(deliveryTime));
        deliveryDateText.setText("Expected delivery: " + deliveryDate);
        
        // Show payment method
        String paymentMethod = getIntent().getStringExtra("paymentMethod");
        if (paymentMethod != null) {
            paymentMethodText.setText("Payment: " + paymentMethod);
        }
    }

    private void setupClickListeners() {
        continueShoppingBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to checkout
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}