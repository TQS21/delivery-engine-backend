package ua.tqs21.deliveryengine.models;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private double latitude;
    private double longitude;

    public Address() {}

    public Address(double latitude, double longitude) throws IllegalStateException {
        if (latitude < -90.0 || latitude > 90 || longitude < -180.0 || longitude > 180.0) {
            throw new IllegalStateException("Invalid address coordinates");
        }

        this.latitude = latitude;
        this.longitude = longitude;

    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalStateException("Invalid address coordinates");
        }
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalStateException("Invalid address coordinates");
        }
        this.longitude = longitude;
    }
}
