package vlu.mobileproject.modle;

public class Cart {
    String added_dated;
    int cart_id, product_id,quantity,  user_id;

    public Cart(String added_dated, int cart_id, int product_id, int quantity, int user_id) {
        this.added_dated = added_dated;
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.user_id = user_id;
    }

    public Cart(){}

    public String getAdded_dated() {
        return added_dated;
    }

    public void setAdded_dated(String added_dated) {
        this.added_dated = added_dated;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
