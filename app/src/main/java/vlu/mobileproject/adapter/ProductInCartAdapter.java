package vlu.mobileproject.adapter;

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

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.ProductInCartItem;
import vlu.mobileproject.R;
import vlu.mobileproject.ShoppingCart;

public class ProductInCartAdapter extends RecyclerView.Adapter<ProductInCartAdapter.ViewHolder> {
    private List<ProductInCartItem> inCartItemList;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private OnCheckedChangeListener onCheckedChangeListener;

    public ProductInCartAdapter(List<ProductInCartItem> inCartItemList) {
        this.inCartItemList = inCartItemList;
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
        holder.ivCart_productImg.setImageResource(product.getProductImg());
        holder.rlRemoveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onItemCheckedChanged(holder.getAdapterPosition(), false);
                }
                ShoppingCart.lstProduct.remove(holder.getAdapterPosition());
//                ShoppingCart.lstQuantity.remove(holder.getAdapterPosition());

                ProductInCartItem x = inCartItemList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());

            }
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
