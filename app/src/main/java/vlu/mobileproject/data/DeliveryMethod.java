package vlu.mobileproject.data;

public enum DeliveryMethod {
    NORMAL("Normal speed"),
    LOW_COST("Low cost"),
    HIGH_SPEED("High speed");

    private final String deliveryMethod;

    DeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getdeliveryMethod() {
        return deliveryMethod;
    }
}