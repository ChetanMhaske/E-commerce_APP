package com.example.e_commerceapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    
    private RecyclerView cartRecyclerView;
    private TextView totalAmountText, emptyCartText;
    private Button checkoutButton;
    private ImageView backButton;
    private CartAdapter cartAdapter;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        initViews();
        setupRecyclerView();
        updateCartUI();
    }

    private void initViews() {
        cartManager = CartManager.getInstance();
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalAmountText = findViewById(R.id.totalAmountText);
        emptyCartText = findViewById(R.id.emptyCartText);
        checkoutButton = findViewById(R.id.checkoutButton);
        backButton = findViewById(R.id.backButton);
        
        backButton.setOnClickListener(v -> finish());
        checkoutButton.setOnClickListener(v -> {
            if (cartManager.getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                android.content.Intent intent = new android.content.Intent(this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this, cartManager.getCartItems(), this::updateCartUI);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void updateCartUI() {
        List<CartItem> items = cartManager.getCartItems();
        if (items.isEmpty()) {
            emptyCartText.setVisibility(android.view.View.VISIBLE);
            cartRecyclerView.setVisibility(android.view.View.GONE);
            totalAmountText.setText("Total: ₹0");
            checkoutButton.setEnabled(false);
        } else {
            emptyCartText.setVisibility(android.view.View.GONE);
            cartRecyclerView.setVisibility(android.view.View.VISIBLE);
            totalAmountText.setText("Total: ₹" + String.format("%.0f", cartManager.getCartTotal()));
            checkoutButton.setEnabled(true);
        }
        cartAdapter.notifyDataSetChanged();
    }
}