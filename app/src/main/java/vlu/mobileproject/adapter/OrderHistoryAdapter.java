package vlu.mobileproject.adapter;

import static com.google.firebase.database.core.RepoManager.clear;
import static java.util.Collections.addAll;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.stream.Collectors;

import vlu.mobileproject.R;
import vlu.mobileproject.activity.view.order.OrderActivity;
import vlu.mobileproject.globalfuction.GlobalData;
import vlu.mobileproject.modle.Discount;
import vlu.mobileproject.modle.OrderHistory;
import vlu.mobileproject.modle.Products;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> implements GlobalData.Callback{

    private Context context;
    private ArrayList<OrderHistory> orderHistoryList;

    List<String> imgList = new ArrayList<>();

    List<Products> productsList = new ArrayList<>();

    int currentIndex = 0;
    private Handler handler = new Handler();
    List<Discount> DiscountList = new ArrayList<>();
    private Runnable updateTextRunnable = new Runnable() {
        @Override
        public void run() {
            if (DiscountList.size() == 0) {
                handler.postDelayed(this, 500);
                return;
            }

            if (currentIndex < DiscountList.size()) {
                animateChange(DiscountList.get(currentIndex).getDiscountUrl(), DiscountList.get(currentIndex).getDiscountDescription());
                currentIndex++;
            } else {
                currentIndex = 0;
            }

            handler.postDelayed(this, 4000);
        }
    };

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    private void animateChange(int animSpeed, OrderHistoryAdapter.ViewHolder holder, String ImgUrl, String productName, String quantity, String pricePerProduct) {
        // Fade-out animation for discountDescription
        performFadeInOutAnimation(animSpeed, holder.productName, productName, null);
        performFadeInOutAnimation(animSpeed, holder.productQuantity, quantity, null);
        performFadeInOutAnimation(animSpeed, holder.orderImg, null, ImgUrl);
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
                    Picasso.get().load(imgUrl).into((ImageView) view);
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
        handler.postDelayed(updateTextRunnable, 0);

        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        OrderHistory orderHistory = orderHistoryList.get(position);

        holder.orderID.setText(orderHistory.getOrder_id());
        String totalPrice = "$ " + (orderHistory.getTotal_amount() * (1 - orderHistory.getDiscount()));
        holder.productTotalPrice.setText(totalPrice.toString());
        holder.productQuantity.setText(String.valueOf(orderHistory.getOrderItemList().size()));
        holder.productStatus.setText(String.valueOf(orderHistory.getStatus().getStatus()));

        holder.orderHisItem.setOnClickListener(v -> {
            onClickGoToDetail(orderHistory, holder);
        });

        imgList = orderHistory.getOrderItemList().stream()
                .map(orderItem -> productsList.stream()
                        .filter(product -> String.valueOf(product.getProduct_id()).equals(orderItem.getOrder_id()))
                        .findFirst()
                        .map(Products::getProduct_img)
                        .orElse(null))
                .collect(Collectors.toList());

        for (String imgUrl : imgList) {
            Picasso.get().load(imgUrl).into(holder.orderImg);
        }
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
        TextView orderID, productTotalPrice, productQuantity, productStatus, productName;

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