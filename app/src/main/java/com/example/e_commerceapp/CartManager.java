package com.example.e_commerceapp;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;
    private List<Product> favoriteProducts;

    private CartManager() {
        cartItems = new ArrayList<>();
        favoriteProducts = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Cart methods
    public void addToCart(Product product) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(product, 1));
    }

    public void removeFromCart(String productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public List<CartItem> getCartItems() { return cartItems; }

    public int getCartItemCount() {
        int count = 0;
        for (CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Favorites methods
    public void addToFavorites(Product product) {
        if (!isFavorite(product.getId())) {
            favoriteProducts.add(product);
            product.setFavorite(true);
        }
    }

    public void removeFromFavorites(String productId) {
        favoriteProducts.removeIf(product -> product.getId().equals(productId));
    }

    public boolean isFavorite(String productId) {
        return favoriteProducts.stream().anyMatch(product -> product.getId().equals(productId));
    }

    public List<Product> getFavoriteProducts() { return favoriteProducts; }
}