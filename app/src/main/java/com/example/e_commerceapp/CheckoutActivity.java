package com.example.e_commerceapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutActivity extends AppCompatActivity {
    
    private RecyclerView orderSummaryRecycler;
    private TextView totalAmountText, deliveryChargeText, finalAmountText;
    private EditText addressEdit, phoneEdit;
    private AppCompatButton placeOrderBtn;
    private ImageView backButton;
    private RadioGroup paymentMethodGroup;
    private RadioButton codRadio, onlineRadio;
    private CartManager cartManager;
    private CheckoutAdapter checkoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        
        initViews();
        setupOrderSummary();
        calculateTotal();
        setupClickListeners();
    }

    private void initViews() {
        cartManager = CartManager.getInstance();
        backButton = findViewById(R.id.backButton);
        orderSummaryRecycler = findViewById(R.id.orderSummaryRecycler);
        totalAmountText = findViewById(R.id.totalAmountText);
        deliveryChargeText = findViewById(R.id.deliveryChargeText);
        finalAmountText = findViewById(R.id.finalAmountText);
        addressEdit = findViewById(R.id.addressEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        codRadio = findViewById(R.id.codRadio);
        onlineRadio = findViewById(R.id.onlineRadio);
        placeOrderBtn = findViewById(R.id.placeOrderBtn);
    }

    private void setupOrderSummary() {
        checkoutAdapter = new CheckoutAdapter(this, cartManager.getCartItems());
        orderSummaryRecycler.setLayoutManager(new LinearLayoutManager(this));
        orderSummaryRecycler.setAdapter(checkoutAdapter);
    }

    private void calculateTotal() {
        double cartTotal = cartManager.getCartTotal();
        double deliveryCharge = cartTotal > 500 ? 0 : 50;
        double finalTotal = cartTotal + deliveryCharge;
        
        totalAmountText.setText("₹" + String.format("%.0f", cartTotal));
        deliveryChargeText.setText(deliveryCharge == 0 ? "FREE" : "₹" + String.format("%.0f", deliveryCharge));
        finalAmountText.setText("₹" + String.format("%.0f", finalTotal));
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        placeOrderBtn.setOnClickListener(v -> {
            String address = addressEdit.getText().toString().trim();
            String phone = phoneEdit.getText().toString().trim();
            
            if (address.isEmpty()) {
                addressEdit.setError("Address is required");
                return;
            }
            
            if (phone.isEmpty()) {
                phoneEdit.setError("Phone number is required");
                return;
            }
            
            if (paymentMethodGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }
            
            placeOrder();
        });
    }

    private void placeOrder() {
        String paymentMethod = codRadio.isChecked() ? "Cash on Delivery" : "Online Payment";
        placeOrderBtn.setText(codRadio.isChecked() ? "Placing Order..." : "Processing Payment...");
        placeOrderBtn.setEnabled(false);
        
        // Simulate order placement
        new android.os.Handler().postDelayed(() -> {
            // Save order to history
            OrderManager.getInstance().addOrder(paymentMethod, cartManager.getCartItems(), cartManager.getCartTotal());
            
            cartManager.getCartItems().clear();
            android.content.Intent intent = new android.content.Intent(this, OrderSuccessActivity.class);
            intent.putExtra("paymentMethod", paymentMethod);
            startActivity(intent);
            finish();
        }, 2000);
    }
}