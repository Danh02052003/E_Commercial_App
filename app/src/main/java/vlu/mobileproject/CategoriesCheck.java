package vlu.mobileproject;

import android.view.ViewGroup;

import vlu.mobileproject.adapter.CategoriesAdapter;

public abstract class CategoriesCheck<T extends CategoriesAdapter.ViewHolder>{
    protected boolean isChecked;
    public abstract T createViewHolder(ViewGroup parent);
    public abstract void bindViewHolder(T holder);

    public CategoriesCheck<T> setChecked (boolean isChecked){
        this.isChecked = isChecked;
        return this;

    }

    public boolean isChecked(){
        return isChecked;
    }

    public boolean isSelectable(){
        return true;
    }

}
