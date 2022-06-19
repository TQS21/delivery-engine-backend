package ua.tqs21.deliveryengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressPostDTO {
    private String country;
    private String zipCode;
    private String region;
    private String address;

    public AddressPostDTO() {}

    public AddressPostDTO(String country, String zipCode, String region, String address) {
        this.country = country;
        this.zipCode = zipCode;
        this.region = region;
        this.address = address;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("zip_code")
    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipcode) {
        this.zipCode = zipcode;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
