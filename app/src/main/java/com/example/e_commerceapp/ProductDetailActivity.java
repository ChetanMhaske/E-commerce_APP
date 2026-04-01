package com.example.e_commerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    
    private ImageView productImage, backButton;
    private TextView productName, productDescription, productPrice, originalPrice, discountPercent;
    private RatingBar ratingBar;
    private TextView reviewCount;
    private RecyclerView screenshotsRecycler, reviewsRecycler;
    private Button addToCartBtn, buyNowBtn;
    private Product product;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        
        initViews();
        getProductData();
        setupData();
        setupClickListeners();
    }
    
    private void initViews() {
        productImage = findViewById(R.id.productImage);
        backButton = findViewById(R.id.backButton);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        originalPrice = findViewById(R.id.originalPrice);
        discountPercent = findViewById(R.id.discountPercent);
        ratingBar = findViewById(R.id.ratingBar);
        reviewCount = findViewById(R.id.reviewCount);
        screenshotsRecycler = findViewById(R.id.screenshotsRecycler);
        reviewsRecycler = findViewById(R.id.reviewsRecycler);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        buyNowBtn = findViewById(R.id.buyNowBtn);
    }
    
    private void getProductData() {
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        String name = intent.getStringExtra("productName");
        String description = intent.getStringExtra("productDescription");
        double price = intent.getDoubleExtra("productPrice", 0);
        double origPrice = intent.getDoubleExtra("originalPrice", 0);
        String imageUrl = intent.getStringExtra("imageUrl");
        float rating = intent.getFloatExtra("rating", 0);
        int reviews = intent.getIntExtra("reviewCount", 0);
        String category = intent.getStringExtra("category");
        
        product = new Product(productId, name, description, price, origPrice, imageUrl, rating, reviews, category);
    }
    
    private void setupData() {
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productPrice.setText("₹" + String.format("%.0f", product.getPrice()));
        
        if (product.getOriginalPrice() > product.getPrice()) {
            originalPrice.setText("₹" + String.format("%.0f", product.getOriginalPrice()));
            originalPrice.setPaintFlags(originalPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            discountPercent.setText(product.getDiscountPercentage() + "% off");
        }
        
        ratingBar.setRating(product.getRating());
        reviewCount.setText("(" + product.getReviewCount() + " reviews)");
        
        Glide.with(this).load(product.getImageUrl()).into(productImage);
        
        setupScreenshots();
        setupReviews();
    }
    
    private void setupScreenshots() {
        List<String> screenshots = getProductScreenshots();
        ScreenshotAdapter adapter = new ScreenshotAdapter(this, screenshots);
        screenshotsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        screenshotsRecycler.setAdapter(adapter);
    }
    
    private void setupReviews() {
        List<Review> reviews = getProductReviews();
        ReviewAdapter adapter = new ReviewAdapter(this, reviews);
        reviewsRecycler.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecycler.setAdapter(adapter);
    }
    
    private List<String> getProductScreenshots() {
        List<String> screenshots = new ArrayList<>();
        // Add multiple product images based on category
        switch (product.getCategory()) {
            case "Electronics":
                screenshots.add("https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400");
                screenshots.add("https://images.unsplash.com/photo-1592750475338-74b7b21085ab?w=400");
                screenshots.add("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400");
                break;
            case "Fashion":
                screenshots.add("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400");
                screenshots.add("https://images.unsplash.com/photo-1542272604-787c3835535d?w=400");
                screenshots.add("https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=400");
                break;
            case "Footwear":
                screenshots.add("https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400");
                screenshots.add("https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=400");
                screenshots.add("https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=400");
                break;
            default:
                screenshots.add("https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=400");
                screenshots.add("https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400");
                break;
        }
        return screenshots;
    }
    
    private List<Review> getProductReviews() {
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review("John Doe", 5, "Excellent product! Highly recommended.", "2 days ago"));
        reviews.add(new Review("Sarah Smith", 4, "Good quality, fast delivery. Worth the price.", "1 week ago"));
        reviews.add(new Review("Mike Johnson", 5, "Amazing! Exactly as described. Will buy again.", "2 weeks ago"));
        reviews.add(new Review("Emma Wilson", 4, "Great product, minor packaging issues but overall satisfied.", "3 weeks ago"));
        return reviews;
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        addToCartBtn.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(this, product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
        });
        
        buyNowBtn.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(this, "Proceeding to checkout for " + product.getName(), Toast.LENGTH_SHORT).show();
            android.content.Intent intent = new android.content.Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }
}