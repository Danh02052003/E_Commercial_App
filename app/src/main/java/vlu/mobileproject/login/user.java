package vlu.mobileproject.login;

public class user {
    public static String userName;
    public static String userEmail;
    private String userPassword;
    public String userPhone;
    private String verificationCode;

    public user(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public user(String username, String email, String userPassword, String userPhone, String verificationCode) {
        this.userName = username;
        this.userEmail = email;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.verificationCode = verificationCode;
    }
}
