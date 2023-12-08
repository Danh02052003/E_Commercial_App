package vlu.mobileproject.data;

public enum DeliveryStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    DELIVERING_TO_YOU("Delivering to you"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    private final String status;

    DeliveryStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

