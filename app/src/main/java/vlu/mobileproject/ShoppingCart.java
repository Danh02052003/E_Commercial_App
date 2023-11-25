package vlu.mobileproject;

import java.util.ArrayList;
import java.util.List;

import vlu.mobileproject.modle.Products;

public class ShoppingCart {
    public static List<ShoppingCart> lstProduct = new ArrayList<>();

    public Products item;
    public int quantity;
    public double price;

    public ShoppingCart(Products item, int quantity, double price) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        lstProduct.add(this);

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
