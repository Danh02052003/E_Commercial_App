package vlu.mobileproject.modle;

public class OrderItem {
    String order_id, orderItemID, product_id, ProductMemoryOption, productName;

    double price_per_unit;

    int quantity;

    public OrderItem(String order_id, String orderItemID, double price_per_unit, String product_id, int quantity) {
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

    public OrderItem(String order_id, String product_id, String ProductMemoryOption, int quantity, String productName) {
        this.order_id = order_id;
        this.orderItemID = orderItemID;
        this.product_id = product_id;
        this.quantity = quantity;
        this.ProductMemoryOption = ProductMemoryOption;
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public double getPrice_per_unit() {
        return price_per_unit;
    }

    public void setPrice_per_unit(double price_per_unit) {
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
