package ua.deliveryengine.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.utils.AddressResolver;
import ua.tqs21.deliveryengine.utils.BasicHttpClient;

@ExtendWith(MockitoExtension.class)
public class AddressResolverTest {
    @Mock(lenient = true)
    private BasicHttpClient httpClient;

    @Mock
    private Environment environment;

    @InjectMocks
    private AddressResolver addressResolver;

    @BeforeEach
    void setup() throws IOException {
        String resp = "{\"data\":[{\"latitude\":38.897675,\"longitude\":-77.036547,\"type\":\"address\",\"name\":\"1600 Pennsylvania Avenue NW\",\"number\":\"1600\",\"postal_code\":\"20500\",\"street\":\"Pennsylvania Avenue NW\",\"confidence\":1,\"region\":\"District of Columbia\",\"region_code\":\"DC\",\"county\":\"District of Columbia\",\"locality\":\"Washington\",\"administrative_area\":null,\"neighbourhood\":\"White House Grounds\",\"country\":\"United States\",\"country_code\":\"USA\",\"continent\":\"North America\",\"label\":\"1600 Pennsylvania Avenue NW, Washington, DC, USA\"},{\"latitude\":38.897473,\"longitude\":-77.036548,\"type\":\"address\",\"name\":\"1600 Pennsylvania Avenue Northwest\",\"number\":\"1600\",\"postal_code\":\"20500\",\"street\":\"Pennsylvania Avenue Northwest\",\"confidence\":1,\"region\":\"District of Columbia\",\"region_code\":\"DC\",\"county\":\"District of Columbia\",\"locality\":\"Washington\",\"administrative_area\":null,\"neighbourhood\":\"White House Grounds\",\"country\":\"United States\",\"country_code\":\"USA\",\"continent\":\"North America\",\"label\":\"1600 Pennsylvania Avenue Northwest, Washington, DC, USA\"}]}";
        when(httpClient.doHttpGet("http://api.positionstack.com/v1/forward?access_key=87da8451a602310023f5f71677ff37f2&query=1600+Pennsylvania+Ave+NW%2C+Washington+DC+USA")).thenReturn(resp);

        when(httpClient.doHttpGet("http://api.positionstack.com/v1/forward?access_key=87da8451a602310023f5f71677ff37f2&query=aaaaaaaaaa+bbbbbbbbb")).thenReturn("[]");
        when(this.environment.getProperty("geocoding.key")).thenReturn("87da8451a602310023f5f71677ff37f2");
    }

    @Test
    void whenValidStreet_ReturnRightCoords() throws URISyntaxException, ParseException, IOException {
        AddressPostDTO req = new AddressPostDTO("USA", "1600", "Pennsylvania Ave NW, Washington DC");
        Optional<Address> response = this.addressResolver.resolveAddress(req);

        assertTrue(response.isPresent());
        assertEquals(38.897675, response.get().getLatitude());
        assertEquals(-77.036547, response.get().getLongitude());
    }

    @Test
    void whenInvalidStreet_ReturnEmpty() throws URISyntaxException, ParseException, IOException {
        AddressPostDTO req = new AddressPostDTO("tehqq", "aaaaaaaaaa", "bbbbbbbbb");
        Optional<Address> response = this.addressResolver.resolveAddress(req);

        assertFalse(response.isPresent());
    }
}
