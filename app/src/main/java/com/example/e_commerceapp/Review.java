package com.example.e_commerceapp;

public class Review {
    private String userName;
    private int rating;
    private String comment;
    private String timeAgo;

    public Review(String userName, int rating, String comment, String timeAgo) {
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.timeAgo = timeAgo;
    }

    public String getUserName() { return userName; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public String getTimeAgo() { return timeAgo; }
}