package vlu.mobileproject.data;

public enum PaymentMethod {
    COD("Cash on Delivery"),
    BANKING("BANKING"),
    CREDIT_CARD("CREDIT CARD");

    private final String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}

