package vlu.mobileproject.translate;

import android.app.Application;

public class language extends Application {
    private static language instance;
    private static String presentLang;

    public language() {
        presentLang = "en";
    }

    public static synchronized language getInstance() {
        if (instance == null) {
            instance = new language();
        }
        return instance;
    }

    public static String getPresentLang() {
        return presentLang;
    }

    public static void setPresentLang(String lang) {
        presentLang = lang;
    }
}
