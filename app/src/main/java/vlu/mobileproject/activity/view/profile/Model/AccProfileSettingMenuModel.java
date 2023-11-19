package vlu.mobileproject.activity.view.profile.Model;

public class AccProfileSettingMenuModel {

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getSettingName() {
        return SettingName;
    }

    public void setSettingName(String settingName) {
        SettingName = settingName;
    }

    int Image;

    String SettingName;
    String SettingCode;

    public String getSettingCode() {
        return SettingCode;
    }

    public void setSettingCode(String settingCode) {
        SettingCode = settingCode;
    }

    public AccProfileSettingMenuModel(int image, String settingName, String SettingCode) {
        Image = image;
        SettingName = settingName;
        this.SettingCode = SettingCode;
    }
    public AccProfileSettingMenuModel() {

    }
}
