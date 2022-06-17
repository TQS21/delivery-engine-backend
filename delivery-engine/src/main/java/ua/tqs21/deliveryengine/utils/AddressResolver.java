package ua.tqs21.deliveryengine.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.env.Environment;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.models.Address;

@Component
public class AddressResolver {
    private final String baseUri = "http://api.positionstack.com/v1/forward";

    @Autowired
    private Environment environment;

    @Autowired
    private BasicHttpClient httpClient;

    public Optional<Address> resolveAddress(AddressPostDTO postAddress) throws URISyntaxException, ParseException, IOException {
        String apiKey = environment.getProperty("geocoding.key");
        URIBuilder uriBuilder = new URIBuilder(this.baseUri);
        uriBuilder.addParameter("access_key", apiKey);
        uriBuilder.addParameter("query", String.format("%s %s %s", postAddress.getZipCode(), postAddress.getAddress(), postAddress.getCountry()));

        String apiResponse = this.httpClient.doHttpGet(uriBuilder.build().toString());
        // get parts from response till reaching the address

        try {
            JSONObject obj = (JSONObject) new JSONParser().parse(apiResponse);
            JSONObject results = (JSONObject) ((JSONArray) obj.get("data")).get(0);
            System.out.println(results);
            Double lat = (Double) results.get("latitude");
            Double lon = (Double) results.get("longitude");
            return Optional.of(new Address(lat, lon));

        } catch(Exception e) {
            return Optional.empty();
        }

    }

    public Date estimateDeliverTs(Address from, Address to) {
        return new Date();
    }

    public double distance(Address from, Address to) {
        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(to.getLatitude() - from.getLatitude());
        double lonDistance = Math.toRadians(to.getLongitude() - from.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(from.getLatitude())) * Math.cos(Math.toRadians(to.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

    
        distance = Math.pow(distance, 2);
    
        return Math.sqrt(distance) / 1000;
    }
}