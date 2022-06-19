package ua.deliveryengine.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.utils.AddressResolver;
import ua.tqs21.deliveryengine.utils.BasicHttpClient;

@ExtendWith(MockitoExtension.class)
public class AddressResolverTest {
    @Mock(lenient = true)
    private BasicHttpClient httpClient;

    @Mock(lenient = true)
    private Environment environment;

    @InjectMocks
    private AddressResolver addressResolver;

    @Test
    void whenValidStreet_ReturnRightCoords() throws URISyntaxException, ParseException, IOException {
        String resp = "{\"data\":[{\"latitude\":38.897675,\"longitude\":-77.036547,\"type\":\"address\",\"name\":\"1600 Pennsylvania Avenue NW\",\"number\":\"1600\",\"postal_code\":\"20500\",\"street\":\"Pennsylvania Avenue NW\",\"confidence\":1,\"region\":\"District of Columbia\",\"region_code\":\"DC\",\"county\":\"District of Columbia\",\"locality\":\"Washington\",\"administrative_area\":null,\"neighbourhood\":\"White House Grounds\",\"country\":\"United States\",\"country_code\":\"USA\",\"continent\":\"North America\",\"label\":\"1600 Pennsylvania Avenue NW, Washington, DC, USA\"},{\"latitude\":38.897473,\"longitude\":-77.036548,\"type\":\"address\",\"name\":\"1600 Pennsylvania Avenue Northwest\",\"number\":\"1600\",\"postal_code\":\"20500\",\"street\":\"Pennsylvania Avenue Northwest\",\"confidence\":1,\"region\":\"District of Columbia\",\"region_code\":\"DC\",\"county\":\"District of Columbia\",\"locality\":\"Washington\",\"administrative_area\":null,\"neighbourhood\":\"White House Grounds\",\"country\":\"United States\",\"country_code\":\"USA\",\"continent\":\"North America\",\"label\":\"1600 Pennsylvania Avenue Northwest, Washington, DC, USA\"}]}";
        when(httpClient.doHttpGet(anyString())).thenReturn(resp);
        AddressPostDTO req = new AddressPostDTO("USA", "1600", "New York", "Pennsylvania Ave NW, Washington DC");
        Optional<Address> response = this.addressResolver.resolveAddress(req);

        assertTrue(response.isPresent());
        assertEquals(38.897675, response.get().getLatitude());
        assertEquals(-77.036547, response.get().getLongitude());
    }

    @Test
    void whenInvalidStreet_ReturnEmpty() throws URISyntaxException, ParseException, IOException {
        when(httpClient.doHttpGet(anyString())).thenReturn("[]");
        AddressPostDTO req = new AddressPostDTO("tehqq", "aaaaaaaaaa", "teste", "bbbbbbbbb");
        Optional<Address> response = this.addressResolver.resolveAddress(req);

        assertFalse(response.isPresent());
    }

    @Test
    void testDistance() {
        Address from = new Address(10, 20);
        Address to = new Address(-10, 110);
        
        assertEquals(10200, this.addressResolver.distance(from, to), 10);
    }
}
