package com.example.e_commerceapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private Context context;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onFavoriteClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText("₹" + String.format("%.0f", product.getPrice()));
        
        if (product.getOriginalPrice() > product.getPrice()) {
            holder.originalPrice.setText("₹" + String.format("%.0f", product.getOriginalPrice()));
            holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.originalPrice.setVisibility(View.VISIBLE);
            holder.discountPercent.setText(product.getDiscountPercentage() + "% off");
            holder.discountPercent.setVisibility(View.VISIBLE);
        } else {
            holder.originalPrice.setVisibility(View.GONE);
            holder.discountPercent.setVisibility(View.GONE);
        }
        
        holder.ratingBar.setRating(product.getRating());
        holder.reviewCount.setText("(" + product.getReviewCount() + ")");
        
        // Load image from internet using Glide
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .transform(new RoundedCorners(8)))
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
        
        boolean isFavorite = FavoritesManager.getInstance().isFavorite(product);
        holder.favoriteIcon.setImageResource(isFavorite ? 
            android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        product.setFavorite(isFavorite);
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onProductClick(product);
        });
        
        holder.favoriteIcon.setOnClickListener(v -> {
            FavoritesManager favoritesManager = FavoritesManager.getInstance();
            if (favoritesManager.isFavorite(product)) {
                favoritesManager.removeFromFavorites(product);
                product.setFavorite(false);
            } else {
                favoritesManager.addToFavorites(product);
                product.setFavorite(true);
            }
            if (listener != null) listener.onFavoriteClick(product);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage, favoriteIcon;
        TextView productName, productDescription, productPrice, originalPrice, discountPercent, reviewCount;
        RatingBar ratingBar;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            favoriteIcon = itemView.findViewById(R.id.favoriteIcon);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            originalPrice = itemView.findViewById(R.id.originalPrice);
            discountPercent = itemView.findViewById(R.id.discountPercent);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            reviewCount = itemView.findViewById(R.id.reviewCount);
        }
    }
}