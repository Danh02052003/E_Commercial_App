package vlu.mobileproject.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import vlu.mobileproject.HomeChildItem;
import vlu.mobileproject.HomeParentItem;
import vlu.mobileproject.R;
import vlu.mobileproject.ViewAllProduct;

public class HomeParentAdapter extends RecyclerView.Adapter<HomeParentAdapter.ViewHolder>{
    public List<HomeParentItem> parentItemList;
    public HomeParentAdapter(List<HomeParentItem> parentItemList) {
        this.parentItemList = parentItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_parent_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeParentItem parentItem = parentItemList.get(position);
        List<HomeChildItem> childItemList = parentItem.getChildItemList();

        HomeChildAdapter childAdapter = new HomeChildAdapter(holder.itemView.getContext(), childItemList);
        holder.rvChildList.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(),LinearLayoutManager.HORIZONTAL,false ));
        holder.rvChildList.setAdapter(childAdapter);
        holder.tvSubCategory_name.setText(parentItem.getSubCategory());
        holder.btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewAllProduct.class);
                intent.putExtra("dataList", (Serializable) childItemList);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return parentItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvSubCategory_name;
        public RecyclerView rvChildList;
        public Button btnExpand;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubCategory_name = itemView.findViewById(R.id.tvSubCategory_name);
            rvChildList = itemView.findViewById(R.id.rvChildList);
            btnExpand = itemView.findViewById(R.id.btnExpand);
        }
    }
}
