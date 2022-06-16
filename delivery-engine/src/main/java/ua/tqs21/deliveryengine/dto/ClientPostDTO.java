package ua.tqs21.deliveryengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientPostDTO {
    private String name;
    private String phoneNumber;

    public ClientPostDTO() {}

    public ClientPostDTO(String name, String phone) {
        this.name = name;
        this.phoneNumber = phone;
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
}
