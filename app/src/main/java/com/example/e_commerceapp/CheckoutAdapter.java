package com.example.e_commerceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {
    private Context context;
    private List<CartItem> cartItems;

    public CheckoutAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        Product product = item.getProduct();
        
        holder.productName.setText(product.getName());
        holder.productPrice.setText("₹" + String.format("%.0f", product.getPrice()));
        holder.quantity.setText("Qty: " + item.getQuantity());
        holder.totalPrice.setText("₹" + String.format("%.0f", item.getTotalPrice()));
        
        Glide.with(context).load(product.getImageUrl()).into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CheckoutViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice, quantity, totalPrice;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }
    }
}