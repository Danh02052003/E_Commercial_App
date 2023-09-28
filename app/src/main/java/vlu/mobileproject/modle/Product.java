package vlu.mobileproject.modle;

public class Product {
    long category_id, product_id;
    int product_price, product_quantity;
    String created_date, product_description, product_name;

    public Product(long category_id, long product_id, int product_price, int product_quantity, String created_date, String product_description, String product_name) {
        this.category_id = category_id;
        this.product_id = product_id;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.created_date = created_date;
        this.product_description = product_description;
        this.product_name = product_name;
    }

    public Product(){}

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
