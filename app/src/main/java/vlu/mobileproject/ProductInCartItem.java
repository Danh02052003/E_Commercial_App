package vlu.mobileproject;

public class ProductInCartItem {
    private String productName;
    private double productPrice;
    private int inCartId;
    private int productQuantity;
    private String productImg;

    String CartItemID;

    public String getCartItemID() {
        return CartItemID;
    }

    public void setCartItemID(String cartItemID) {
        CartItemID = cartItemID;
    }

    private boolean isChecked = false;

    public ProductInCartItem(String CartItemID, String productName, double productPrice, int productQuantity, String productImg) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productImg = productImg;
        this.CartItemID = CartItemID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getInCartId() {
        return inCartId;
    }

    public void setInCartId(int inCartId) {
        this.inCartId = inCartId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
