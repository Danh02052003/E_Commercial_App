package vlu.mobileproject.activity.view.order;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vlu.mobileproject.ProductDetailsActivity;
import vlu.mobileproject.R;
import vlu.mobileproject.modle.OrderHistory;
import vlu.mobileproject.modle.OrderItem;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    Context context;
    private List<OrderItem> orderItemList;

    public OrderDetailAdapter(Context context, List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
        this.context = context;
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

        holder.orderItemItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGoToDetail(orderItem.getProduct_id());
            }
        });
    }

    public void onClickGoToDetail(String productID) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("productID", productID);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, quantityTextView, color, total;
        ConstraintLayout orderItemItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTextView = itemView.findViewById(R.id.productName);
            quantityTextView = itemView.findViewById(R.id.quantity);
            orderItemItem = itemView.findViewById(R.id.orderItemItem);
            color = itemView.findViewById(R.id.color);
            total = itemView.findViewById(R.id.totalTemp);
        }
    }
}
