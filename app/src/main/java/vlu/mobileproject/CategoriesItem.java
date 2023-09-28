package vlu.mobileproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import vlu.mobileproject.adapter.CategoriesAdapter;

public class CategoriesItem extends CategoriesCheck<CategoriesItem.ViewHolder>{
    private int normalItemTextTint;
    private int selectedItemTextTint;
    private int normalItemBackgroundColor;
    private int selectedItemBackgroundColor;
    private final String categoryName;

    public CategoriesItem(String categoryName) {
        this.categoryName = categoryName;

    }

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_categories_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {
        holder.tvCategory_name.setText(categoryName);
        holder.tvCategory_name.setTextColor(isChecked ? selectedItemTextTint : normalItemTextTint);
        holder.cvCategory_bg.setCardBackgroundColor(isChecked ? selectedItemBackgroundColor : normalItemBackgroundColor);
    }

    public CategoriesItem withSelectedTextTint(int selectedItemTextTint){
        this.selectedItemTextTint = selectedItemTextTint;
        return this;
    }

    public CategoriesItem withTextTint(int normalItemTextTint){
        this.normalItemTextTint = normalItemTextTint;
        return this;
    }

    public CategoriesItem withSelectedBackgroundColor(int selectedItemBackgroundColor) {
        this.selectedItemBackgroundColor = selectedItemBackgroundColor;
        return this;
    }

    public CategoriesItem withBackgroundColor(int normalItemBackgroundColor) {
        this.normalItemBackgroundColor = normalItemBackgroundColor;
        return this;
    }

    static class ViewHolder extends CategoriesAdapter.ViewHolder{
        private final TextView tvCategory_name;
        private final CardView cvCategory_bg;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);
            tvCategory_name = itemView.findViewById(R.id.tvCategory_name);
            cvCategory_bg = itemView.findViewById(R.id.cvCategory_bg);
        }
    }
}
