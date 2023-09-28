package vlu.mobileproject.modle;

public class Phone {
    private int imagePhone;
    private String namePhone;

    public int getImagePhone() {
        return imagePhone;
    }

    public void setImagePhone(int imagePhone) {
        this.imagePhone = imagePhone;
    }

    public String getNamePhone() {
        return namePhone;
    }

    public void setNamePhone(String namePhone) {
        this.namePhone = namePhone;
    }

    public Phone(int imagePhone, String namePhone) {
        this.imagePhone = imagePhone;
        this.namePhone = namePhone;
    }
    public Phone(){}
}
