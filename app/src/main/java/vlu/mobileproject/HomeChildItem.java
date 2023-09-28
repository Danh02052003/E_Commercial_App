package vlu.mobileproject;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class HomeChildItem implements Serializable {
    private String productName;
    private String productCategory;
    private double productPrice;
    private int productImg;


    private int categoryId;
    private int quantity;
    private long productId;
    private String createdDate;
    private String productDescription;
    private String[] capacities;
    private boolean isChecked = false;



    public HomeChildItem(String productName, double productPrice, int productImg,
                        long productId, int categoryId, int quantity, String createdDate, String productDescription, String[] capacities) {
        this.productName = productName;
        switch (categoryId){
            case 1:
                productCategory = "Samsung";
                break;
            case 2:
                productCategory = "iPhone";
                break;
        }
        this.productPrice = productPrice;
        this.productImg = productImg;

        this.productId= productId;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.productDescription = productDescription;
        this.capacities = capacities;

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductImg() {
        return productImg;
    }

    public void setProductImg(int productImg) {
        this.productImg = productImg;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String[] getCapacities() {
        return capacities;
    }

    public void setCapacities(String[] capacities) {
        this.capacities = capacities;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HomeChildItem that = (HomeChildItem) obj;
        return productId == that.productId;
    }
}
