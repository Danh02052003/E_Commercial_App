package vlu.mobileproject.modle;

import vlu.mobileproject.data.DeliveryMethod;
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.data.PaymentMethod;

public class Order {
    protected String buyer_id, order_id, shippingAddress;
    protected double total_amount, discount, otherFees;
    protected String order_date;
    protected DeliveryStatus status;
    protected PaymentMethod paymentMethod;
    protected DeliveryMethod deliveryMethod;
    protected String phoneNumber;
    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getOtherFees() {
        return otherFees;
    }

    public void setOtherFees(double otherFees) {
        this.otherFees = otherFees;
    }

    public Order(String buyer_id, String order_id, double total_amount, double discount, double otherFees, String order_date, DeliveryStatus status, PaymentMethod paymentMethod, DeliveryMethod deliveryMethod, String shippingAddress, String phoneNumber) {
        this.buyer_id = buyer_id;
        this.order_id = order_id;
        this.total_amount = total_amount;
        this.order_date = order_date;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.discount = discount;
        this.otherFees = otherFees;
        this.deliveryMethod = deliveryMethod;
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
