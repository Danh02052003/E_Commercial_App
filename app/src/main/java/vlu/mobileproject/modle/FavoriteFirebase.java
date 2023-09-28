package vlu.mobileproject.modle;

public class FavoriteFirebase {
    String user_email;
    long product_id;

    public FavoriteFirebase(String user_email, long product_id) {
        this.user_email = user_email;
        this.product_id = product_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }
}
