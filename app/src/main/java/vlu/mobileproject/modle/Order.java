package vlu.mobileproject.modle;

public class Order {
    int buyer_id, order_id, total_amount;
    String order_date, status;

    public Order(int buyer_id, int order_id, int total_amount, String order_date, String status) {
        this.buyer_id = buyer_id;
        this.order_id = order_id;
        this.total_amount = total_amount;
        this.order_date = order_date;
        this.status = status;
    }

    public Order(){}

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
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
