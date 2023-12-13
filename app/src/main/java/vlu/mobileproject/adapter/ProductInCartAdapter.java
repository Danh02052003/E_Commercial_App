package vlu.mobileproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.ProductInCartItem;
import vlu.mobileproject.R;
import vlu.mobileproject.ShoppingCart;

public class ProductInCartAdapter extends RecyclerView.Adapter<ProductInCartAdapter.ViewHolder> {
    private List<ProductInCartItem> inCartItemList;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private OnCheckedChangeListener onCheckedChangeListener;
    Context context;
    TextView tvCart_totalAdded;

    public ProductInCartAdapter(Context context, List<ProductInCartItem> inCartItemList, TextView tvCart_totalAdded) {
        this.inCartItemList = inCartItemList;
        this.context = context;
        this.tvCart_totalAdded = tvCart_totalAdded;
    }

    public interface OnRemoveCartItem {
        void OnRemovre(String input);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductInCartItem product = inCartItemList.get(position);
        viewBinderHelper.bind(holder.srl, String.valueOf(product.getInCartId()));
        holder.tvCart_productName.setText(product.getProductName());
        holder.tvCart_productPrice.setText("$" + String.valueOf(product.getProductPrice()));
        holder.tvCart_quantityAdded.setText(String.valueOf(product.getProductQuantity()) + "x");
        loadGlideImageWithCheck(context, product.getProductImg(), holder.ivCart_productImg);
        holder.rlRemoveProduct.setOnClickListener(view -> {
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onItemCheckedChanged(holder.getAdapterPosition(), false);
            }
            DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference("Cart");
            cartReference.child(product.getCartItemID()).removeValue();
            inCartItemList.remove(holder.getAdapterPosition());
            notifyItemRemoved(holder.getAdapterPosition());

            tvCart_totalAdded.setText(String.format("%d %s", inCartItemList.size(), context.getString(R.string.goods)));
        });
        holder.cbCartCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onItemCheckedChanged(holder.getAdapterPosition(), b);
                }
            }
        });

        holder.ll_selectProduct.setOnClickListener(view -> {
            if(holder.cbCartCheck.isChecked()){
                holder.cbCartCheck.setChecked(false);
                product.setChecked(false);
            }

            else{
                holder.cbCartCheck.setChecked(true);
                product.setChecked(true);
            }
        });

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
    @Override
    public int getItemCount() {
        return inCartItemList.size();
    }
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.onCheckedChangeListener = listener;
    }
    public interface OnCheckedChangeListener {
        void onItemCheckedChanged(int position, boolean isChecked);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        SwipeRevealLayout srl;
        RelativeLayout rlRemoveProduct;
        TextView tvCart_productName;
        TextView tvCart_productPrice;
        TextView tvCart_quantityAdded;
        ImageView ivCart_productImg;
        CheckBox cbCartCheck;
        LinearLayout ll_selectProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            srl = itemView.findViewById(R.id.srl);
            rlRemoveProduct = itemView.findViewById(R.id.rlRemoveProduct);
            tvCart_productName = itemView.findViewById(R.id.tvCart_productName);
            tvCart_productPrice = itemView.findViewById(R.id.tvCart_productPrice);
            tvCart_quantityAdded = itemView.findViewById(R.id.tvCart_quantityAdded);
            ivCart_productImg = itemView.findViewById(R.id.ivCart_productImg);
            cbCartCheck = itemView.findViewById(R.id.cbCartCheck);
            ll_selectProduct = itemView.findViewById(R.id.ll_selectProduct);
        }
    }

}
