package com.example.e_commerceapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {
    
    private RecyclerView favoritesRecyclerView;
    private TextView emptyFavoritesText;
    private ImageView backButton;
    private ProductAdapter productAdapter;
    private CartManager cartManager;
    private FavoritesManager favoritesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        
        initViews();
        setupRecyclerView();
        updateFavoritesUI();
    }

    private void initViews() {
        cartManager = CartManager.getInstance();
        favoritesManager = FavoritesManager.getInstance();
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        emptyFavoritesText = findViewById(R.id.emptyFavoritesText);
        backButton = findViewById(R.id.backButton);
        
        backButton.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this, favoritesManager.getFavoriteProducts());
        productAdapter.setOnProductClickListener(this);
        favoritesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        favoritesRecyclerView.setAdapter(productAdapter);
    }

    private void updateFavoritesUI() {
        if (favoritesManager.getFavoriteProducts().isEmpty()) {
            emptyFavoritesText.setVisibility(android.view.View.VISIBLE);
            favoritesRecyclerView.setVisibility(android.view.View.GONE);
        } else {
            emptyFavoritesText.setVisibility(android.view.View.GONE);
            favoritesRecyclerView.setVisibility(android.view.View.VISIBLE);
        }
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onProductClick(Product product) {
        // Navigate to product detail
    }

    @Override
    public void onFavoriteClick(Product product) {
        favoritesManager.removeFromFavorites(product);
        product.setFavorite(false);
        updateFavoritesUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateFavoritesUI();
    }
}