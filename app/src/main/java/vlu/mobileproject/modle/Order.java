package vlu.mobileproject.modle;

import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.data.PaymentMethod;

public class Order {
    String buyer_id, order_id;
    double total_amount;
    String order_date;
    DeliveryStatus status;
    PaymentMethod paymentMethod;

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Order(String buyer_id, String order_id, double total_amount, String order_date, DeliveryStatus status, PaymentMethod paymentMethod) {
        this.buyer_id = buyer_id;
        this.order_id = order_id;
        this.total_amount = total_amount;
        this.order_date = order_date;
        this.status = status;
        this.paymentMethod = paymentMethod;
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

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
