package vlu.mobileproject;

import java.util.List;

import vlu.mobileproject.modle.Products;

public class HomeParentItem {
    private String subCategory;
    private List<Products> products;

    public HomeParentItem(String subCategory, List<Products> products) {
        this.subCategory = subCategory;
        this.products = products;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public List<Products> getChildItemList() {
        return products;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setChildItemList(List<Products> products) {
        this.products = products;
    }
}
