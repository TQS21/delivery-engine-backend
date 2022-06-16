package ua.tqs21.deliveryengine.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.models.Address;

@Component
public class AddressResolver {
    public static Address resolveAddress(AddressPostDTO postAddress) {
        // TODO: Pegando nos campos do AddressPostDTO (country, zip code e morada) transformar em Address (longitude, latitude)
        return new Address();
    }

    public static Date estimateDeliverTs(Address from, Address to) {
        return new Date();
    }
}