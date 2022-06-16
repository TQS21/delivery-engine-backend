package ua.tqs21.deliveryengine.enums;

public enum OrdStatus {
    QUEUED("QUEUED"), COLLECTING("COLLECTING"), DELIVERING("DELIVERING"), DELIVERED("DELIVER"), CANCELLED("CANCELLED");

    private final String status;
    private OrdStatus(String s) {
        this.status = s;
    }

    public boolean equalsTo(String s) {
        return this.status.equals(s);
    }
}
