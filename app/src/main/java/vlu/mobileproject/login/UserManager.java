package vlu.mobileproject.login;

public class UserManager {
    private static UserManager instance;
    private String userEmail;

    private UserManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
