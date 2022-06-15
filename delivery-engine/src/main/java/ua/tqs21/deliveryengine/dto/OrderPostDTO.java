package ua.tqs21.deliveryengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderPostDTO {
    private int shopId;
    private ClientPostDTO client;
    private AddressPostDTO address;

    public OrderPostDTO() {}

    public OrderPostDTO(int id, ClientPostDTO cpd, AddressPostDTO apd) {
        this.shopId = id;
        this.client = cpd;
        this.address = apd;
    }

    @JsonProperty("shop_id")
    public int getShopId() {
        return this.shopId;
    }

    public void setShopId(int id) {
        this.shopId = id;
    }

    public ClientPostDTO getClient() {
        return this.client;
    }

    public void setClient(ClientPostDTO cpd) {
        this.client = cpd;
    }

    public AddressPostDTO getAddress() {
        return this.address;
    }

    public void setAddress(AddressPostDTO apd) {
        this.address = apd;
    }

}
