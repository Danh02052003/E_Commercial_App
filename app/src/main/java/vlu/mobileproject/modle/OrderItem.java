package vlu.mobileproject.modle;

public class OrderItem {
    String order_id, orderItemID, price_per_unit, product_id, ProductMemoryOption;

    int quantity;

    public OrderItem(String order_id, String orderItemID, String price_per_unit, String product_id, int quantity) {
        this.order_id = order_id;
        this.orderItemID = orderItemID;
        this.price_per_unit = price_per_unit;
        this.product_id = product_id;
        this.quantity = quantity;
    }

    public OrderItem(String order_id, String product_id, String ProductMemoryOption, int quantity) {
        this.order_id = order_id;
        this.orderItemID = orderItemID;
        this.product_id = product_id;
        this.quantity = quantity;
        this.ProductMemoryOption = ProductMemoryOption;
    }

    public String getProductMemoryOption() {
        return ProductMemoryOption;
    }

    public void setProductMemoryOption(String productMemoryOption) {
        ProductMemoryOption = productMemoryOption;
    }

    public OrderItem(){}

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(String orderItemID) {
        this.orderItemID = orderItemID;
    }

    public String getPrice_per_unit() {
        return price_per_unit;
    }

    public void setPrice_per_unit(String price_per_unit) {
        this.price_per_unit = price_per_unit;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}