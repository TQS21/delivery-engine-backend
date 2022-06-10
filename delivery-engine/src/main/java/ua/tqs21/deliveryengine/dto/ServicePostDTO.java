package ua.tqs21.deliveryengine.dto;

public class ServicePostDTO {
    private String name;
    private AddressPostDTO address;

    public ServicePostDTO() {}

    public ServicePostDTO(String name, AddressPostDTO address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressPostDTO getAddress() {
        return this.address;
    }

    public void setAddress(AddressPostDTO address) {
        this.address = address;
    }
}
