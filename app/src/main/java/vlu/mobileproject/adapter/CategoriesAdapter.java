package vlu.mobileproject.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vlu.mobileproject.CategoriesCheck;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private List<CategoriesCheck> items;
    private Map<Class<? extends CategoriesCheck>, Integer> viewTypes;
    private SparseArray<CategoriesCheck> holderFactories;

    private OnItemSelectedListener listener;

    public CategoriesAdapter(List<CategoriesCheck> items){
        this.items = items;
        this.viewTypes = new HashMap<>();
        this.holderFactories = new SparseArray<>();
        processViewTypes();
    }
    private void processViewTypes(){
        int type = 0;
        for(CategoriesCheck item : items){
            if(!viewTypes.containsKey(item.getClass())){
                viewTypes.put(item.getClass(),type);
                holderFactories.put(type,item);
                type++;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = holderFactories.get(viewType).createViewHolder(parent);
        holder.categoriesAdapter = this;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        items.get(position).bindViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public int getItemViewType(int position) {
        return viewTypes.get(items.get(position).getClass());
    }

    public void setSelected(int position){
        CategoriesCheck newChecked = items.get(position);
        if(!newChecked.isSelectable()){
            return;
        }

        for (int i=0; i<items.size(); i++){
            CategoriesCheck item = items.get(i);
            if(item.isChecked()){
                item.setChecked(false);
                notifyItemChanged(i);
                break;
            }
        }

        newChecked.setChecked(true);
        notifyItemChanged(position);

        if (listener != null){
            listener.onItemSelected(position);
        }
    }

    public void setListener(OnItemSelectedListener listener){
        this.listener = listener;
    }

    public interface OnItemSelectedListener{
        void onItemSelected(int position);
    }

    public static abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CategoriesAdapter categoriesAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {categoriesAdapter.setSelected(getAdapterPosition());}
    }
}
