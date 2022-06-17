package ua.tqs21.deliveryengine.models;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class ClientOrderInfo {
    private String name;
    private String phoneNumber;
    private String address;
    private String zipCode;

    public ClientOrderInfo() {}

    public ClientOrderInfo(String name, String phone, String address, String zipCode) {
        this.name = name;
        this.phoneNumber = phone;
        this.address = address;
        this.zipCode = zipCode;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("phone_number")
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    @JsonProperty("zip_code")
    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
