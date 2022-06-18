package ua.tqs21.deliveryengine.dto;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
public class OrderPostDTO {
    private int shopId;
    private int shopOrderRef;
    private ClientPostDTO client;
    private AddressPostDTO address;

    public OrderPostDTO() {}

    public OrderPostDTO(int id, int shopOrderRef, ClientPostDTO cpd, AddressPostDTO apd) {
        this.shopId = id;
        this.shopOrderRef = shopOrderRef;
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

    @JsonProperty("shop_order_ref")
    public int getShopOrderRef() {
        return this.shopOrderRef;
    }

    public void setShopOrderRef(int id) {
        this.shopOrderRef = id;
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