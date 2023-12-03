package vlu.mobileproject;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.modle.Products;

public class ShoppingCart {
    public static List<ShoppingCart> lstProduct = new ArrayList<>();

    public Products item;

    String productID;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int quantity;
    public double price;

    String UserID;

    public ShoppingCart(Products item, int quantity, double price) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        //lstProduct.add(this);
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
