package com.example.e_commerceapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;

    public OrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        
        holder.orderIdText.setText(order.getOrderId());
        holder.orderDateText.setText("Ordered on " + order.getOrderDate());
        holder.deliveryDateText.setText("Delivery by " + order.getDeliveryDate());
        holder.statusText.setText(order.getStatus());
        holder.paymentMethodText.setText(order.getPaymentMethod());
        holder.totalAmountText.setText("₹" + String.format("%.0f", order.getTotalAmount()));
        holder.itemCountText.setText(order.getItemCount() + " item(s)");
        
        // Set status color
        int statusColor = order.getStatus().equals("Delivered") ? 
            context.getResources().getColor(R.color.success) : 
            context.getResources().getColor(R.color.primary);
        holder.statusText.setTextColor(statusColor);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, orderDateText, deliveryDateText, statusText, 
                paymentMethodText, totalAmountText, itemCountText;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.orderIdText);
            orderDateText = itemView.findViewById(R.id.orderDateText);
            deliveryDateText = itemView.findViewById(R.id.deliveryDateText);
            statusText = itemView.findViewById(R.id.statusText);
            paymentMethodText = itemView.findViewById(R.id.paymentMethodText);
            totalAmountText = itemView.findViewById(R.id.totalAmountText);
            itemCountText = itemView.findViewById(R.id.itemCountText);
        }
    }
}