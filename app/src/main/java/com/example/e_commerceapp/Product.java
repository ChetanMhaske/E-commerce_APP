package com.example.e_commerceapp;

public class Product {
    private String id;
    private String name;
    private String description;
    private double price;
    private double originalPrice;
    private String imageUrl;
    private float rating;
    private int reviewCount;
    private String category;
    private boolean isFavorite;

    public Product() {}

    public Product(String id, String name, String description, double price, double originalPrice, 
                   String imageUrl, float rating, int reviewCount, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.originalPrice = originalPrice;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.category = category;
        this.isFavorite = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public int getDiscountPercentage() {
        if (originalPrice > 0) {
            return (int) (((originalPrice - price) / originalPrice) * 100);
        }
        return 0;
    }
}