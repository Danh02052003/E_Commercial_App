package vlu.mobileproject;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.modle.Products;

public class ShoppingCart {
    public static List<ShoppingCart> lstProduct = new ArrayList<>();

    public Products item;

    String productID;
    public int quantity;

    public String getMemoryOptName() {
        return MemoryOptName;
    }

    public void setMemoryOptName(String selectedMemoryOption) {
        this.MemoryOptName = selectedMemoryOption;
    }

    String MemoryOptName;

    String UserID;
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public ShoppingCart() {

    }

    public ShoppingCart(String productID, int quantity, String MemoryOptName) {
        this.quantity = quantity;
        this.productID = productID;
        this.MemoryOptName = MemoryOptName;
    }
    public ShoppingCart(String productID, int quantity) {
        this.quantity = quantity;
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static List<ShoppingCart> getLstProduct() {
        return lstProduct;
    }

    public static void setLstProduct(List<ShoppingCart> lstProduct) {
        ShoppingCart.lstProduct = lstProduct;
    }

    public Products getItem() {
        return item;
    }

    public void setItem(Products item) {
        this.item = item;
    }
}
