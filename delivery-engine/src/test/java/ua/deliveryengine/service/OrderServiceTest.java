package ua.deliveryengine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.dto.ClientPostDTO;
import ua.tqs21.deliveryengine.dto.OrderPostDTO;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.models.OrderStatus;
import ua.tqs21.deliveryengine.models.Service;
import ua.tqs21.deliveryengine.repositories.OrderRepository;
import ua.tqs21.deliveryengine.repositories.OrderStatusRepository;
import ua.tqs21.deliveryengine.services.OrderService;
import ua.tqs21.deliveryengine.services.ServiceService;
import ua.tqs21.deliveryengine.utils.AddressResolver;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ServiceService serviceService;

    @Mock(lenient = true)
    private AddressResolver  addressResolver;

    @Mock
    private OrderStatusRepository orderStatusRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderPostDTO postDTO1 = new OrderPostDTO(1, 34, new ClientPostDTO("teste", "teste"), new AddressPostDTO("Portugal", "340", "x"));

    @BeforeEach
    void setup() throws URISyntaxException, ParseException, IOException {
        Service dummy = new Service();
        dummy.setId(1);
        Mockito.when(serviceService.getById(anyInt())).thenReturn(dummy);
        Mockito.when(this.orderStatusRepository.findByName(anyString())).thenReturn(new OrderStatus("QUEUED"));
        Service origin = new Service("a", null, null, null);
        origin.setId(1);
        Mockito.when(this.orderRepository.save(any(Order.class))).thenReturn(new Order(null, null, null, null, origin, 34, new ClientPostDTO("teste", "teste")));
        Mockito.when(addressResolver.resolveAddress(any(AddressPostDTO.class))).thenReturn(Optional.of(new Address()));
        Mockito.when(addressResolver.estimateDeliverTs(any(Address.class), any(Address.class))).thenReturn(new Date());
    }

    @Test
    void whenPostDto_createOrder() throws URISyntaxException, ParseException, IOException {
        Order test = this.orderService.createOrderFromDTO(postDTO1);

        assertEquals(postDTO1.getShopId(), test.getShop().getId());
        assertEquals(postDTO1.getShopOrderRef(), test.getShopOrderRef());
        assertEquals(postDTO1.getClient().getPhoneNumber(), test.getContact().getPhoneNumber());
    }
}
