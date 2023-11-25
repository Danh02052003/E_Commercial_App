package vlu.mobileproject.adapter;

import static com.google.firebase.database.core.RepoManager.clear;

import static java.util.Collections.addAll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.AllFrag;
import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.R;
import vlu.mobileproject.modle.Products;

public class ProductAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Products> productList;

    public ProductAdapter(Context context, ArrayList<Products> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_layout_phone_search, parent, false);
        }

        // Get the current product item
        Products product = productList.get(position);

        ImageView imgProductId = convertView.findViewById(R.id.imgPhoneSearch);
        TextView textProductName = convertView.findViewById(R.id.txtNamePhone);

        // Set the product_id as the image resource
//        imgProductId.setImageResource(product.getProduct_img());

        // Set the product_name to the TextView
        textProductName.setText(product.getProduct_name());

        return convertView;
    }

    // Add this method to update the adapter data
    @SuppressLint("RestrictedApi")
    public void updateData(List<Products> filteredData) {
        clear();
        addAll(filteredData); // Add the filtered data to the adapter
        notifyDataSetChanged(); // Notify the ListView that the data has changed
    }
}