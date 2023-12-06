package vlu.mobileproject.data;

import androidx.appcompat.app.AppCompatActivity;

public enum PaymentMethod {
    COD("COD"),
    BANKING("BANKING"),
    CREDIT_CART("CREDIT CART");

    private final String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}

