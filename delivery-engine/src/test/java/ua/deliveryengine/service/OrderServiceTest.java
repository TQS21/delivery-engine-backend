package ua.deliveryengine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderRepository orderRepository;

    @Mock
    private ServiceService serviceService;

    @Mock
    private AddressResolver  addressResolver;

    @Mock
    private OrderStatusRepository orderStatusRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderPostDTO postDTO1 = new OrderPostDTO(1, 34, new ClientPostDTO("teste", "teste"), new AddressPostDTO("Portugal", "340", "x"));

    @BeforeEach
    void setup() {
        Service dummy = new Service();
        dummy.setId(1);
        Mockito.when(serviceService.getById(anyInt())).thenReturn(dummy);
        Mockito.when(this.orderStatusRepository.findByName(anyString())).thenReturn(new OrderStatus("QUEUED"));
        //Mockito.when(AddressResolver.estimateDeliverTs(any(Address.class), any(Address.class))).thenReturn(new Date());
    }

    @Test
    void whenPostDto_createOrder() {
        Order test = this.orderService.createOrderFromDTO(postDTO1);

        assertEquals(postDTO1.getShopId(), test.getShop().getId());
        assertEquals(postDTO1.getShopOrderRef(), test.getShopOrderRef());
        assertEquals(postDTO1.getClient().getPhoneNumber(), test.getContact().getPhoneNumber());
    }
}
