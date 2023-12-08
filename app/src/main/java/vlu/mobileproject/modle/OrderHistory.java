package vlu.mobileproject.modle;

import vlu.mobileproject.activity.view.order.OrderHistoryActivity;
import vlu.mobileproject.data.DeliveryMethod;
import vlu.mobileproject.data.DeliveryStatus;
import vlu.mobileproject.data.PaymentMethod;

public class OrderHistory extends Order {

    int orderItemCount;

    public OrderHistory() {

    }

    public OrderHistory(String buyer_id, String order_id, double total_amount, double discount, double otherFees, String order_date, DeliveryStatus status, PaymentMethod paymentMethod, DeliveryMethod deliveryMethod, String shippingAddress, String phoneNumber, int orderItemCount) {
        super(buyer_id, order_id, total_amount, discount, otherFees, order_date, status, paymentMethod, deliveryMethod, shippingAddress, phoneNumber);
        this.orderItemCount = orderItemCount;
    }

    public int getOrderItemCount() {
        return orderItemCount;
    }

    public void setOrderItemCount(int orderItemCount) {
        this.orderItemCount = orderItemCount;
    }
}