package vlu.mobileproject.activity.view.order;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.ProductDetailsActivity;
import vlu.mobileproject.R;
import vlu.mobileproject.globalfuction.GlobalData;
import vlu.mobileproject.modle.OrderHistory;
import vlu.mobileproject.modle.OrderItem;
import vlu.mobileproject.modle.Products;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> implements GlobalData.Callback {
    Context context;
    private List<OrderItem> orderItemList;
    List<Products> productList;

    public OrderDetailAdapter(Context context, List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order_detail_item, parent, false);
        GlobalData.initData(context, this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem = orderItemList.get(position);

        List<OrderItem> OrderItemList = new ArrayList<>();

        for (Products product : productList) {
            if (orderItem.getProduct_id().equals(product.getProductID())) {
                orderItem.setImgUrl(product.getProduct_img());
                orderItem.setProductName(product.getProduct_name());
                orderItem.setPrice_per_unit(product.getProductOptPackage(orderItem.getProductMemoryOptKey()).getProduct_price());
                orderItem.setProductOptName(product.getProductOptPackage(orderItem.getProductMemoryOptKey()).getMemory());
                OrderItemList.add(orderItem);
            }
        }

        holder.itemNameTextView.setText(orderItem.getProductName());
        Picasso.get().load(orderItem.getImgUrl()).into(holder.itemImg);
        holder.quantityTextView.setText(String.valueOf("X " + orderItem.getQuantity()));
        //holder.color.setText(String.valueOf("X " + orderItem.getQuantity()));
        holder.memoOpt.setText(orderItem.getProductOptName());
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

    @Override
    public void onCompleted(List<Products> products) {
        productList = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView, quantityTextView, total, memoOpt;
        ConstraintLayout orderItemItem;

        ImageView itemImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTextView = itemView.findViewById(R.id.productName);
            quantityTextView = itemView.findViewById(R.id.quantity);
            orderItemItem = itemView.findViewById(R.id.orderItemItem);
            itemImg = itemView.findViewById(R.id.itemImg);
            total = itemView.findViewById(R.id.totalTemp);
            memoOpt = itemView.findViewById(R.id.memoOpt);
        }
    }
}
