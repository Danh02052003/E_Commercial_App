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

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.R;
import vlu.mobileproject.modle.OrderHistory;
import vlu.mobileproject.modle.Products;

public class OrderHistoryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OrderHistory> orderHistoryList;

    public OrderHistoryAdapter(Context context, ArrayList<OrderHistory> orderHistoryList) {
        this.context = context;
        this.orderHistoryList = orderHistoryList;
    }

    @Override
    public int getCount() {
        return orderHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_layout_order_history, parent, false);
        }
        OrderHistory orderHistory = orderHistoryList.get(position);

        TextView orderID = convertView.findViewById(R.id.orderID);
        TextView productTotalPrice = convertView.findViewById(R.id.txtTotalPrice);
        TextView productQuantity= convertView.findViewById(R.id.quantity);
        TextView productStatus= convertView.findViewById(R.id.txtorderStatus);

        orderID.setText(orderHistory.getOrder_id());
        String totalPrice = "$ " + (orderHistory.getTotal_amount() * (1 - orderHistory.getDiscount()));
        productTotalPrice.setText(totalPrice.toString());
        productQuantity.setText(String.valueOf(orderHistory.getOrderItemCount()));
        productStatus.setText(orderHistory.getStatus().getStatus());

        return convertView;
    }

    // Add this method to update the adapter data
    @SuppressLint("RestrictedApi")
    public void updateData(List<OrderHistory> filteredData) {
        clear();
        addAll(filteredData); // Add the filtered data to the adapter
        notifyDataSetChanged(); // Notify the ListView that the data has changed
    }
}