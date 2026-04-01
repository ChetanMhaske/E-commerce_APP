package com.example.e_commerceapp;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {
    private static FavoritesManager instance;
    private List<Product> favoriteProducts;
    private static final String PREFS_NAME = "FavoritesData";
    private static final String FAVORITES_KEY = "favorites";
    private Context context;

    private FavoritesManager() {
        favoriteProducts = new ArrayList<>();
    }

    public static FavoritesManager getInstance() {
        if (instance == null) {
            instance = new FavoritesManager();
        }
        return instance;
    }
    
    public void init(Context context) {
        this.context = context;
        loadFavorites();
    }

    public void addToFavorites(Product product) {
        if (!isFavorite(product)) {
            favoriteProducts.add(product);
            saveFavorites();
        }
    }

    public void removeFromFavorites(Product product) {
        favoriteProducts.removeIf(p -> p.getId() == product.getId());
        saveFavorites();
    }

    public boolean isFavorite(Product product) {
        return favoriteProducts.stream().anyMatch(p -> p.getId() == product.getId());
    }

    public List<Product> getFavoriteProducts() {
        return favoriteProducts;
    }
    
    private void saveFavorites() {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String favoritesJson = gson.toJson(favoriteProducts);
            prefs.edit().putString(FAVORITES_KEY, favoritesJson).apply();
        }
    }
    
    private void loadFavorites() {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String favoritesJson = prefs.getString(FAVORITES_KEY, null);
            if (favoritesJson != null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Product>>(){}.getType();
                List<Product> savedFavorites = gson.fromJson(favoritesJson, listType);
                if (savedFavorites != null) {
                    favoriteProducts = savedFavorites;
                }
            }
        }
    }
}