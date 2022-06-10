package ua.tqs21.deliveryengine.enums;

public enum Roles {
    ADMIN("ADMIN"), RIDER("RIDER"), SERVICE_OWNER("SERVICE_OWNER");

    private final String role;
    private Roles(String s) {
        this.role = s;
    }

    public boolean equalsTo(String r) {
        return this.role.equals(r);
    }
}
