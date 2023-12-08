package vlu.mobileproject.data;

public enum DeliveryProvider {
    NORMAL(""),
    GIAO_HANG_TIET_KIEM("In Progress"),
    HOA_TOC("Delivering to you");

    private final String deliveryProvider;

    DeliveryProvider(String deliveryProvider) {
        this.deliveryProvider = deliveryProvider;
    }

    public String getdeliveryProvider() {
        return deliveryProvider;
    }
}