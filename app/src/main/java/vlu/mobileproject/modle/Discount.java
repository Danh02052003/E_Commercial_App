package vlu.mobileproject.modle;

public class Discount {
    String discountName;
    String discountDescription;
    double discountValue;

    public Discount() {

    }

    public Discount(String discountName, String discountDescription, double discountValue) {
        this.discountName = discountName;
        this.discountDescription = discountDescription;
        this.discountValue = discountValue;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getDiscountDescription() {
        return discountDescription;
    }

    public void setDiscountDescription(String discountDescription) {
        this.discountDescription = discountDescription;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
}
