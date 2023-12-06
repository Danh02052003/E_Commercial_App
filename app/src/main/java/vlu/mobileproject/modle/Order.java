package vlu.mobileproject.modle;

public class Order {
    String buyer_id, order_id;
    double total_amount;
    String order_date, status;

    public Order(String buyer_id, String order_id, double total_amount, String order_date, String status) {
        this.buyer_id = buyer_id;
        this.order_id = order_id;
        this.total_amount = total_amount;
        this.order_date = order_date;
        this.status = status;
    }

    public Order(){}

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
