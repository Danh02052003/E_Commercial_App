package vlu.mobileproject.data;

public enum DeliveryProvider {
    ("Pending"),
    IN_PROGRESS("In Progress"),
    DELIVERING_TO_YOU("Delivering to you"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    private final String deliveryProvider;

    DeliveryProvider(String deliveryProvider) {
        this.deliveryProvider = deliveryProvider;
    }

    public String getdeliveryProvider() {
        return deliveryProvider;
    }
}