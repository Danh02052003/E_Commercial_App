package vlu.mobileproject.modle;

public class Delivery {
    String delivery_address, delivery_date, status;
    int delivery_id, order_id;

    public Delivery(String delivery_address, String delivery_date, String status, int delivery_id, int order_id) {
        this.delivery_address = delivery_address;
        this.delivery_date = delivery_date;
        this.status = status;
        this.delivery_id = delivery_id;
        this.order_id = order_id;
    }

    public Delivery(){}

    public String getDelivery_address() {
        return delivery_address;
    }

    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(int delivery_id) {
        this.delivery_id = delivery_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
