package com.example.e_commerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {
    
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private List<Product> allProducts;
    private TextView categoryAll, categoryElectronics, categoryFashion, categoryFootwear, categoryHome;
    private ImageView logoutButton, favoritesButton, cartButton, profileButton, ordersButton;
    private String selectedCategory = "All";
    private FirebaseAuth mAuth;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initializeViews();
        setupRecyclerView();
        loadProducts();
    }
    
    private void initializeViews() {
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recyclerViewProducts);
        categoryAll = findViewById(R.id.categoryAll);
        categoryElectronics = findViewById(R.id.categoryElectronics);
        categoryFashion = findViewById(R.id.categoryFashion);
        categoryFootwear = findViewById(R.id.categoryFootwear);
        categoryHome = findViewById(R.id.categoryHome);
        logoutButton = findViewById(R.id.logoutButton);
        favoritesButton = findViewById(R.id.favoritesButton);
        cartButton = findViewById(R.id.cartButton);
        profileButton = findViewById(R.id.profileButton);
        ordersButton = findViewById(R.id.ordersButton);
        cartManager = CartManager.getInstance();
        OrderManager.getInstance().init(this);
        FavoritesManager.getInstance().init(this);
        
        setupCategoryClickListeners();
        setupLogoutListener();
        setupNavigationListeners();
    }
    
    private void setupRecyclerView() {
        productList = new ArrayList<>();
        allProducts = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        productAdapter.setOnProductClickListener(this);
        
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(productAdapter);
    }
    
    private void loadProducts() {
        // Electronics
        allProducts.add(new Product("1", "iPhone 15 Pro", "Latest Apple smartphone with A17 Pro chip", 134900, 149900, "https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400", 4.5f, 1250, "Electronics"));
        allProducts.add(new Product("2", "Samsung Galaxy S24", "Flagship Android phone with AI features", 79999, 89999, "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400", 4.3f, 890, "Electronics"));
        allProducts.add(new Product("3", "Sony WH-1000XM5", "Premium noise cancelling headphones", 29990, 34990, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400", 4.6f, 2340, "Electronics"));
        allProducts.add(new Product("4", "MacBook Air M2", "Lightweight laptop with M2 chip", 114900, 119900, "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400", 4.7f, 1890, "Electronics"));
        allProducts.add(new Product("5", "JBL Flip 6", "Portable waterproof Bluetooth speaker", 11999, 13999, "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400", 4.3f, 890, "Electronics"));
        
        // Fashion
        allProducts.add(new Product("6", "Levi's 501 Jeans", "Classic straight fit denim jeans", 3999, 4999, "https://images.unsplash.com/photo-1542272604-787c3835535d?w=400", 4.1f, 445, "Fashion"));
        allProducts.add(new Product("7", "H&M Cotton T-Shirt", "Basic cotton t-shirt in multiple colors", 799, 999, "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400", 3.9f, 1567, "Fashion"));
        allProducts.add(new Product("8", "Zara Blazer", "Professional blazer for office wear", 5999, 7999, "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400", 4.2f, 234, "Fashion"));
        allProducts.add(new Product("9", "Casual Hoodie", "Comfortable hoodie for everyday wear", 2499, 2999, "https://images.unsplash.com/photo-1556821840-3a63f95609a7?w=400", 4.0f, 678, "Fashion"));
        
        // Footwear
        allProducts.add(new Product("10", "Nike Air Max 270", "Comfortable running shoes for daily wear", 12995, 15995, "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400", 4.2f, 567, "Footwear"));
        allProducts.add(new Product("11", "Adidas Ultraboost 22", "Premium running shoes with boost technology", 16999, 19999, "https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=400", 4.4f, 723, "Footwear"));
        allProducts.add(new Product("12", "Formal Leather Shoes", "Classic black leather shoes for office", 4999, 6999, "https://images.unsplash.com/photo-1449824913935-59a10b8d2000?w=400", 4.1f, 345, "Footwear"));
        allProducts.add(new Product("13", "Casual Sneakers", "White casual sneakers for everyday use", 3499, 3999, "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=400", 3.8f, 456, "Footwear"));
        
        // Home
        allProducts.add(new Product("14", "Coffee Maker", "Automatic drip coffee maker", 8999, 9999, "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400", 4.3f, 234, "Home"));
        allProducts.add(new Product("15", "Table Lamp", "Modern LED table lamp with dimmer", 2999, 3499, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400", 4.0f, 123, "Home"));
        allProducts.add(new Product("16", "Throw Pillow Set", "Decorative cushions for sofa", 1999, 2499, "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400", 3.9f, 567, "Home"));
        allProducts.add(new Product("17", "Wall Clock", "Minimalist wall clock for living room", 1499, 1999, "https://images.unsplash.com/photo-1563861826100-9cb868fdbe1c?w=400", 4.2f, 89, "Home"));
        
        productList.addAll(allProducts);
        productAdapter.notifyDataSetChanged();
    }
    
    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("productId", product.getId());
        intent.putExtra("productName", product.getName());
        intent.putExtra("productDescription", product.getDescription());
        intent.putExtra("productPrice", product.getPrice());
        intent.putExtra("originalPrice", product.getOriginalPrice());
        intent.putExtra("imageUrl", product.getImageUrl());
        intent.putExtra("rating", product.getRating());
        intent.putExtra("reviewCount", product.getReviewCount());
        intent.putExtra("category", product.getCategory());
        startActivity(intent);
    }
    
    @Override
    public void onFavoriteClick(Product product) {
        if (cartManager.isFavorite(product.getId())) {
            cartManager.removeFromFavorites(product.getId());
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            cartManager.addToFavorites(product);
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupCategoryClickListeners() {
        categoryAll.setOnClickListener(v -> filterByCategory("All"));
        categoryElectronics.setOnClickListener(v -> filterByCategory("Electronics"));
        categoryFashion.setOnClickListener(v -> filterByCategory("Fashion"));
        categoryFootwear.setOnClickListener(v -> filterByCategory("Footwear"));
        categoryHome.setOnClickListener(v -> filterByCategory("Home"));
    }
    
    private void filterByCategory(String category) {
        selectedCategory = category;
        updateCategoryUI();
        
        productList.clear();
        if (category.equals("All")) {
            productList.addAll(allProducts);
        } else {
            for (Product product : allProducts) {
                if (product.getCategory().equals(category)) {
                    productList.add(product);
                }
            }
        }
        productAdapter.notifyDataSetChanged();
    }
    
    private void updateCategoryUI() {
        // Reset all categories
        categoryAll.setBackgroundColor(getResources().getColor(R.color.gray200));
        categoryElectronics.setBackgroundColor(getResources().getColor(R.color.gray200));
        categoryFashion.setBackgroundColor(getResources().getColor(R.color.gray200));
        categoryFootwear.setBackgroundColor(getResources().getColor(R.color.gray200));
        categoryHome.setBackgroundColor(getResources().getColor(R.color.gray200));
        
        categoryAll.setTextColor(getResources().getColor(R.color.textPrimary));
        categoryElectronics.setTextColor(getResources().getColor(R.color.textPrimary));
        categoryFashion.setTextColor(getResources().getColor(R.color.textPrimary));
        categoryFootwear.setTextColor(getResources().getColor(R.color.textPrimary));
        categoryHome.setTextColor(getResources().getColor(R.color.textPrimary));
        
        // Highlight selected category
        switch (selectedCategory) {
            case "All":
                categoryAll.setBackgroundResource(R.drawable.button_background);
                categoryAll.setTextColor(getResources().getColor(R.color.white));
                break;
            case "Electronics":
                categoryElectronics.setBackgroundResource(R.drawable.button_background);
                categoryElectronics.setTextColor(getResources().getColor(R.color.white));
                break;
            case "Fashion":
                categoryFashion.setBackgroundResource(R.drawable.button_background);
                categoryFashion.setTextColor(getResources().getColor(R.color.white));
                break;
            case "Footwear":
                categoryFootwear.setBackgroundResource(R.drawable.button_background);
                categoryFootwear.setTextColor(getResources().getColor(R.color.white));
                break;
            case "Home":
                categoryHome.setBackgroundResource(R.drawable.button_background);
                categoryHome.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }
    
    private void setupLogoutListener() {
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
    
    private void setupNavigationListeners() {
        favoritesButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        });
        
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
        
        // Set cart emoji as background
        cartButton.setBackground(null);
        cartButton.setScaleType(ImageView.ScaleType.CENTER);
        cartButton.setImageDrawable(null);
        cartButton.setBackgroundResource(android.R.color.transparent);
        
        // Create TextView for emoji and add it
        android.widget.TextView cartEmoji = new android.widget.TextView(this);
        cartEmoji.setText("🛒");
        cartEmoji.setTextSize(20);
        cartEmoji.setGravity(android.view.Gravity.CENTER);
        cartEmoji.setLayoutParams(new android.view.ViewGroup.LayoutParams(72, 72));
        
        // Replace ImageView with TextView in parent
        android.view.ViewGroup parent = (android.view.ViewGroup) cartButton.getParent();
        int index = parent.indexOfChild(cartButton);
        parent.removeView(cartButton);
        cartEmoji.setId(R.id.cartButton);
        cartEmoji.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
        parent.addView(cartEmoji, index);
        
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
        
        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrdersActivity.class);
            startActivity(intent);
        });
    }
}