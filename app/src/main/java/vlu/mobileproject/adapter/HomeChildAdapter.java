package vlu.mobileproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import vlu.mobileproject.Favorite;
import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.ProductDetailsActivity;
import vlu.mobileproject.R;
import vlu.mobileproject.modle.Products;

public class HomeChildAdapter extends RecyclerView.Adapter<HomeChildAdapter.ViewHolder>{
//    public List<HomeChildItem> childItemList;
    public List<Products> productsList;
    private Context mContext;
//    public HomeChildAdapter(Context context, List<HomeChildItem> childItemList) {
//        this.mContext = context;
//        this.childItemList = childItemList;
//    }
    public HomeChildAdapter(Context context, List<Products> productsList) {
        this.mContext = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_child_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        HomeChildItem childItem = childItemList.get(position);
//
//        holder.tvProductName.setText(childItem.getProductName());
//        holder.tvProductCategory.setText(childItem.getProductCategory());
//        holder.tvProductPrice.setText("$" + String.valueOf(childItem.getProductPrice()));
//        holder.ivProductImg.setImageResource(childItem.getProductImg());
//        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                onClickGoToDetail(childItem);
//            }
//        });
        Products product = productsList.get(position);

        holder.tvProductName.setText(product.getProduct_name());

        switch (product.getProduct_categoryId()){
            case 1:
                holder.tvProductCategory.setText("Samsung");
                break;
            case 2:
                holder.tvProductCategory.setText("Apple");
                break;
        }

        holder.tvProductPrice.setText(String.valueOf(product.getPriceForMemory()));

        StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(product.getProduct_img());
        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            product.setProduct_img(uri.toString());
            String imageURL = product.getProduct_img();
            Glide.with(mContext).load(imageURL).into(holder.ivProductImg);
        }).addOnFailureListener(e -> {
            Log.e(this.toString(), "Error loading image from Firebase: " + e.getMessage());
            e.printStackTrace();
        });
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickGoToDetail(product);
            }
        });

    }
    public void onClickGoToDetail(Products product){
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_product", product);
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }

    public void setItems(List<Products> items) {
        int removedPosition = -1;
        for (int i = 0; i < productsList.size(); i++) {
            if (!items.contains(productsList.get(i))) {
                removedPosition = i;
                break;
            }
        }
        this.productsList = items;
        notifyItemRemoved(removedPosition);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvProductName;
        public TextView tvProductCategory;
        public TextView tvProductPrice;
        public ImageView ivProductImg;
        private LinearLayout layoutItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImg = itemView.findViewById(R.id.ivProductImg);
            layoutItem = itemView.findViewById(R.id.layoutItem);
        }
    }


}
