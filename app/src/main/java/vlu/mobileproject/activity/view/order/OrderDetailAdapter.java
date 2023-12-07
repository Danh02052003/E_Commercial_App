package vlu.mobileproject.activity.view.order;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vlu.mobileproject.R;
import vlu.mobileproject.modle.OrderItem;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private List<OrderItem> orderItemList;

    public OrderDetailAdapter(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);

        // Bind your data to the views in the item layout
        holder.itemNameTextView.setText(orderItem.getProductName());
        holder.quantityTextView.setText(String.valueOf("X " + orderItem.getQuantity()));
        //holder.color.setText(String.valueOf("X " + orderItem.getQuantity()));
        holder.total.setText(String.valueOf("$ " + (orderItem.getPrice_per_unit() * orderItem.getQuantity())));
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, quantityTextView, color, total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTextView = itemView.findViewById(R.id.productName);
            quantityTextView = itemView.findViewById(R.id.quantity);
            color = itemView.findViewById(R.id.color);
            total = itemView.findViewById(R.id.totalAmount);
        }
    }
}
