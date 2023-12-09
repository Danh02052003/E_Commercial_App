package vlu.mobileproject.data;

public enum FirebaseReferenceKey {
    PRODUCT("Products_2"),
    DISCOUNT("Discount"),
    ORDER("Order"),
    ORDER_ITEM("OrderItem"),
    CART("Cart");

    private final String ReferenceKey;

    FirebaseReferenceKey(String ReferenceKey) {
        this.ReferenceKey = ReferenceKey;
    }

    public String getReferenceKey() {
        return ReferenceKey;
    }
}
