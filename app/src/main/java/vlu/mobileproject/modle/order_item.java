package vlu.mobileproject.modle;

public class order_item {
    int order_id, order_id_item, price_per_unit, product_id, quantity;

    public order_item(int order_id, int order_id_item, int price_per_unit, int product_id, int quantity) {
        this.order_id = order_id;
        this.order_id_item = order_id_item;
        this.price_per_unit = price_per_unit;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public order_item(){}

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_id_item() {
        return order_id_item;
    }

    public void setOrder_id_item(int order_id_item) {
        this.order_id_item = order_id_item;
    }

    public int getPrice_per_unit() {
        return price_per_unit;
    }

    public void setPrice_per_unit(int price_per_unit) {
        this.price_per_unit = price_per_unit;
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
}
