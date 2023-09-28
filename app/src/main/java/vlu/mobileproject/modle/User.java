package vlu.mobileproject.modle;
public class User {
    private String id_user;
    private String email_user;
    private String name_user;
    private String password_user;
    private String phone_user;

    public User(String id_user, String email_user, String name_user, String password_user, String phone_user) {
        this.id_user = id_user;
        this.email_user = email_user;
        this.name_user = name_user;
        this.password_user = password_user;
        this.phone_user = phone_user;
    }

    // Empty constructor
    public User() {
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getPassword_user() {
        return password_user;
    }

    public void setPassword_user(String password_user) {
        this.password_user = password_user;
    }

    public String getPhone_user() {
        return phone_user;
    }

    public void setPhone_user(String phone_user) {
        this.phone_user = phone_user;
    }
}
