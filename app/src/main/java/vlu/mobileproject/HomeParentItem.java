package vlu.mobileproject;

import java.util.List;

public class HomeParentItem {
    private String subCategory;
    private List<HomeChildItem> childItemList;

    public HomeParentItem(String subCategory, List<HomeChildItem> childItemList) {
        this.subCategory = subCategory;
        this.childItemList = childItemList;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public List<HomeChildItem> getChildItemList() {
        return childItemList;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setChildItemList(List<HomeChildItem> childItemList) {
        this.childItemList = childItemList;
    }
}
