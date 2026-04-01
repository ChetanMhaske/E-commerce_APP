package com.example.e_commerceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private Runnable updateCallback;

    public CartAdapter(Context context, List<CartItem> cartItems, Runnable updateCallback) {
        this.context = context;
        this.cartItems = cartItems;
        this.updateCallback = updateCallback;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        Product product = item.getProduct();
        
        holder.productName.setText(product.getName());
        holder.productPrice.setText("₹" + String.format("%.0f", product.getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.totalPrice.setText("₹" + String.format("%.0f", item.getTotalPrice()));
        
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);
        
        holder.increaseBtn.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            updateCallback.run();
        });
        
        holder.decreaseBtn.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
            } else {
                CartManager.getInstance().removeFromCart(product.getId());
            }
            updateCallback.run();
        });
        
        holder.removeBtn.setOnClickListener(v -> {
            CartManager.getInstance().removeFromCart(product.getId());
            updateCallback.run();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantity, totalPrice;
        Button increaseBtn, decreaseBtn, removeBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            increaseBtn = itemView.findViewById(R.id.increaseBtn);
            decreaseBtn = itemView.findViewById(R.id.decreaseBtn);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }
}