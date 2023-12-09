package vlu.mobileproject.adapter;

import static com.google.firebase.database.core.RepoManager.clear;
import static java.util.Collections.addAll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.R;
import vlu.mobileproject.activity.view.order.OrderDetailAdapter;
import vlu.mobileproject.modle.OrderHistory;
import vlu.mobileproject.modle.Products;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<OrderHistory> orderHistoryList;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_order_history, parent, false);
        return new OrderHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        OrderHistory orderHistory = orderHistoryList.get(position);

        holder.orderID.setText(orderHistory.getOrder_id());
        String totalPrice = "$ " + (orderHistory.getTotal_amount() * (1 - orderHistory.getDiscount()));
        holder.productTotalPrice.setText(totalPrice.toString());
        holder.productQuantity.setText(String.valueOf(orderHistory.getOrderItemCount()));
        holder.productStatus.setText(String.valueOf(orderHistory.getStatus().getStatus()));

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, productTotalPrice, productQuantity, productStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderID = itemView.findViewById(R.id.orderID);
            productTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
            productQuantity= itemView.findViewById(R.id.quantity);
            productStatus= itemView.findViewById(R.id.txtorderStatus);

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