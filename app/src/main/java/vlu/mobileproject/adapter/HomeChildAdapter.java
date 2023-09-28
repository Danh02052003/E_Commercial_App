package vlu.mobileproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vlu.mobileproject.Favorite;
import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.ProductDetailsActivity;
import vlu.mobileproject.R;

public class HomeChildAdapter extends RecyclerView.Adapter<HomeChildAdapter.ViewHolder>{
    public List<HomeChildItem> childItemList;
    private Context mContext;
    public HomeChildAdapter(Context context, List<HomeChildItem> childItemList) {
        this.mContext = context;
        this.childItemList = childItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_child_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeChildItem childItem = childItemList.get(position);

        holder.tvProductName.setText(childItem.getProductName());
        holder.tvProductCategory.setText(childItem.getProductCategory());
        holder.tvProductPrice.setText("$" + String.valueOf(childItem.getProductPrice()));
        holder.ivProductImg.setImageResource(childItem.getProductImg());
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickGoToDetail(childItem);
            }
        });

    }
    public void onClickGoToDetail(HomeChildItem childItem){
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_product",childItem);
        intent.putExtras(bundle);

        mContext.startActivity(intent);
    }

    public void setItems(List<HomeChildItem> items) {
        int removedPosition = -1;
        for (int i = 0; i < childItemList.size(); i++) {
            if (!items.contains(childItemList.get(i))) {
                removedPosition = i;
                break;
            }
        }
        this.childItemList = items;
        notifyItemRemoved(removedPosition);
    }

    @Override
    public int getItemCount() {
        return childItemList.size();
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
