package vlu.mobileproject.data;

public enum PhoneType {
    Iphone("Iphone"),
    Samsung("Samsung");

    private final String phoneTypes;

    PhoneType(String phoneTypes) {
        this.phoneTypes = phoneTypes;
    }

    public String getPhoneType() {
        return phoneTypes;
    }
}
