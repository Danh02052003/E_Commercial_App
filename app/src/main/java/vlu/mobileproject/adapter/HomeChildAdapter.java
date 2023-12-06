package vlu.mobileproject.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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

import vlu.mobileproject.ProductDetailsActivity;
import vlu.mobileproject.R;
import vlu.mobileproject.modle.Products;

public class HomeChildAdapter extends RecyclerView.Adapter<HomeChildAdapter.ViewHolder> {
    public List<Products> productsList;
    private Context mContext;

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
        Products product = productsList.get(position);

        holder.tvProductName.setText(product.getProduct_name());

        switch (product.getProduct_categoryId()) {
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
            loadGlideImageWithCheck(mContext, imageURL, holder.ivProductImg);
        }).addOnFailureListener(e -> {
            Log.e(this.toString(), "Error loading image from Firebase: " + e.getMessage());
            e.printStackTrace();
        });

        holder.layoutItem.setOnClickListener(view -> onClickGoToDetail(product, holder));
    }

    private void loadGlideImageWithCheck(Context context, String imageUrl, ImageView imageView) {
        if (isValidContextForGlide(context)) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(imageView);
        }
    }

    private static boolean isValidContextForGlide(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return !(activity.isDestroyed() || activity.isFinishing());
        }
        return true;
    }

    public void onClickGoToDetail(Products product, ViewHolder viewHolder) {
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_product", product);
        intent.putExtras(bundle);

        Pair<View, String> p1 = Pair.create((View) viewHolder.ivProductImg, "product_img");
        Pair<View, String> p2 = Pair.create((View) viewHolder.tvProductName, "product_name");
        Pair<View, String> p3 = Pair.create((View) viewHolder.tvProductPrice, "product_price");
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, p1, p2, p3);
        mContext.startActivity(intent, options.toBundle());
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
