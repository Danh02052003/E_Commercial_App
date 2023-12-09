package vlu.mobileproject.data;

public enum FirebaseReferenceKey {
    PRODUCT("Product_2"),
    DISCOUNT("Discount");

    private final String ReferenceKey;

    FirebaseReferenceKey(String ReferenceKey) {
        this.ReferenceKey = ReferenceKey;
    }

    public String getReferenceKey() {
        return ReferenceKey;
    }
}
