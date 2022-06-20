package ua.tqs21.deliveryengine.controller;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Date;

import ua.tqs21.deliveryengine.controllers.OrderController;
import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.dto.ClientPostDTO;
import ua.tqs21.deliveryengine.dto.OrderPostDTO;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.models.OrderStatus;
import ua.tqs21.deliveryengine.models.Service;
import ua.tqs21.deliveryengine.services.OrderService;

@SpringBootTest(classes = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableWebMvc
@ActiveProfiles("test")
public class OrderControllerTest {
    @Autowired
    private MockMvc mvc;


    @MockBean
    private OrderService orderService;

    private OrderPostDTO testDto = new OrderPostDTO(1, 1, new ClientPostDTO("TESTE", "TESTE"), new AddressPostDTO());
    private Order expected = new Order(new OrderStatus("teste"), new Date(), new Date(), null, new Service(), 1, null, null);
    @BeforeEach
    void setup() throws Exception {
        System.out.println(asJsonString(expected));
        when(orderService.createOrderFromDTO(testDto)).thenReturn(expected);
        when(orderService.getNearbyOrders(any(Address.class))).thenReturn(new ArrayList<>());
    }

    @Test
    @WithMockUser
    void whenPostDto_persistOrder() throws Exception {
        mvc.perform(
            post("/delivery/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(testDto))
        ).andExpect(status().is(200));
    }

    @Test
    @WithMockUser
    void whenGetNearbyOrders_returnOrders() throws IllegalStateException, Exception {
        mvc.perform(
            post("/delivery/nearby")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(new Address(35, 21)))
        ).andExpect(status().is(200))
        .andExpect(content().string("[]"));
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
