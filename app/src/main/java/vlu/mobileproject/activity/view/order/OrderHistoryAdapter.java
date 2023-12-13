package vlu.mobileproject.activity.view.order;

import static com.google.firebase.database.core.RepoManager.clear;
import static java.util.Collections.addAll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vlu.mobileproject.R;
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.globalfuction.GlobalData;
import vlu.mobileproject.modle.OrderHistory;
import vlu.mobileproject.modle.OrderItem;
import vlu.mobileproject.modle.Products;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> implements GlobalData.Callback{

    private Context context;
    private ArrayList<OrderHistory> orderHistoryList;

    List<OrderItem> OrderItemList = new ArrayList<>();

    List<Products> productsList;

    private Handler handler = new Handler();

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    private void animateChange(int animSpeed, OrderHistoryAdapter.ViewHolder holder, OrderItem orderItem) {
        performFadeInOutAnimation(animSpeed, holder.productName, orderItem.getProductName() + " " + orderItem.getProductOptName(), null);
        performFadeInOutAnimation(animSpeed, holder.productQuantity, "x " + orderItem.getQuantity(), null);
        performFadeInOutAnimation(animSpeed, holder.orderImg, null, orderItem.getImgUrl());
        performFadeInOutAnimation(animSpeed, holder.txtPricePerUnit, "$ " + orderItem.getPrice_per_unit(), null);
    }

    private void performFadeInOutAnimation(int animSpeed, View view, String textToShow, String imgUrl) {
        ObjectAnimator fadeOutAnimation = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOutAnimation.setDuration(animSpeed);
        fadeOutAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (view instanceof TextView) {
                    ((TextView) view).setText(textToShow);
                } else if (view instanceof ImageView) {
                    String imgLink = imgUrl.isEmpty() ? "https://robohash.org/" + Math.random() : imgUrl;
                    Picasso.get().load(imgLink).into((ImageView) view);
                }

                ObjectAnimator fadeInAnimation = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeInAnimation.setDuration(animSpeed);
                fadeInAnimation.start();
            }
        });
        fadeOutAnimation.start();
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_order_history, parent, false);
        GlobalData.initData(context, this);

        return new OrderHistoryAdapter.ViewHolder(view);
    }

    void GetOrderItemList(OrderHistory orderHistory) {
        for (Products product : productsList) {
            for (OrderItem orderItem : orderHistory.getOrderItemList()) {
                if (orderItem.getProduct_id().equals(product.getProductID())) {
                    orderItem.setImgUrl(product.getProduct_img());
                    orderItem.setProductName(product.getProduct_name());
                    orderItem.setPrice_per_unit(product.getProductOptPackage(orderItem.getProductMemoryOptKey()).getProduct_price());
                    orderItem.setProductOptName(product.getProductOptPackage(orderItem.getProductMemoryOptKey()).getMemory());
                    OrderItemList.add(orderItem);
                    break;
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        OrderHistory orderHistory = orderHistoryList.get(position);

        GetOrderItemList(orderHistory);

        Runnable updateTextRunnable = new Runnable() {
            @Override
            public void run() {
                if (OrderItemList.size() == 0) {
                    GetOrderItemList(orderHistory);
                    handler.postDelayed(this, 500);
                    return;
                }

                if (orderHistory.currentCount < OrderItemList.size()) {
                    animateChange(500, holder, OrderItemList.get(orderHistory.currentCount));
                    orderHistory.currentCount++;
                } else {
                    orderHistory.currentCount = 0;
                }

                if (OrderItemList.size() > 1) {
                    handler.postDelayed(this, 4000);
                }
            }
        };
        handler.postDelayed(orderHistory.setUpdateTextRunnable(updateTextRunnable), 0);

        holder.orderID.setText(orderHistory.getOrder_id());
        String totalPrice = "$ " + (orderHistory.getTotal_amount() * (1 - orderHistory.getDiscount()));
        holder.productTotalPrice.setText(totalPrice.toString());
        holder.productQuantity.setText(String.valueOf(orderHistory.getOrderItemList().size()));
        holder.productStatus.setText(String.valueOf(orderHistory.getStatus().getStatus()));
        holder.txtDiscount.setText(String.valueOf(orderHistory.getDiscount() + " %"));

        int statusColor;
        if (orderHistory.getStatus().equals(DeliveryStatus.CANCELED)) {
            statusColor = context.getColor(R.color.red);
        } else {
            statusColor = context.getColor(R.color.greenVLUS);
        }
        holder.productStatus.setTextColor(statusColor);
        holder.orderHisItem.setOnClickListener(v -> {
            onClickGoToDetail(orderHistory, holder);
        });
    }

    public void onClickGoToDetail(OrderHistory orderHistory, OrderHistoryAdapter.ViewHolder viewHolder) {
        Intent intent = new Intent(context, OrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("OrderID", orderHistory.getOrder_id());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    @Override
    public void onCompleted(List<Products> products) {
        productsList = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, productTotalPrice, productQuantity, productStatus, productName, txtPricePerUnit, txtDiscount;

        ImageView orderImg;

        ConstraintLayout orderHisItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderID = itemView.findViewById(R.id.orderID);
            productName = itemView.findViewById(R.id.productName);
            productTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            productQuantity= itemView.findViewById(R.id.quantity);
            productStatus= itemView.findViewById(R.id.txtorderStatus);
            orderHisItem= itemView.findViewById(R.id.orderHisItem);
            orderImg= itemView.findViewById(R.id.orderImg);
            txtPricePerUnit= itemView.findViewById(R.id.txtPricePerUnit);
            txtDiscount= itemView.findViewById(R.id.txtDiscount);

        }
    }

    // Add this method to update the adapter data
    @SuppressLint("RestrictedApi")
    public void updateData(List<OrderHistory> filteredData) {
        clear();
        addAll(filteredData); // Add the filtered data to the adapter
        notifyDataSetChanged(); // Notify the ListView that the data has changed
    }
}