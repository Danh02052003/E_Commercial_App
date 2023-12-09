package vlu.mobileproject.modle;

import java.io.Serializable;
import java.util.List;

import vlu.mobileproject.activity.view.order.OrderHistoryActivity;
import vlu.mobileproject.data.DeliveryMethod;
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.data.PaymentMethod;

public class OrderHistory extends Order implements Serializable {

    List<OrderItem> orderItemList;

    public OrderHistory() {

    }

    public OrderHistory(String buyer_id, String order_id, double total_amount, double discount, double otherFees, String order_date, DeliveryStatus status, PaymentMethod paymentMethod, DeliveryMethod deliveryMethod, String shippingAddress, String phoneNumber) {
        super(buyer_id, order_id, total_amount, discount, otherFees, order_date, status, paymentMethod, deliveryMethod, shippingAddress, phoneNumber);
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}