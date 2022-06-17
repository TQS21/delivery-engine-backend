package ua.deliveryengine.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.models.OrderStatus;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.OrderRepository;
import ua.tqs21.deliveryengine.repositories.OrderStatusRepository;
import ua.tqs21.deliveryengine.repositories.UserRepository;
import ua.tqs21.deliveryengine.services.OrderService;
import ua.tqs21.deliveryengine.services.RiderService;

@ExtendWith(MockitoExtension.class)
public class OrderStatusTest {

    @Mock(lenient = true)
    private OrderRepository orderRepository;

    @Mock(lenient = true)
    private OrderStatusRepository orderStatusRepository;

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private RiderService riderService;

    @Mock(lenient = true)
    private Authentication auth;

    @Mock(lenient = true)
    private SecurityContext securityContext;

    @InjectMocks
    private OrderService orderService;

    private Order queuedOrder;
    private Order collectingOrder;
    private Order deliveringOrder;
    private Order deliveredOrder;
    private Order cancelledOrder;

    @BeforeEach
    void setup() {
        Mockito.when(orderStatusRepository.findByName("QUEUED")).thenReturn(new OrderStatus("QUEUED"));
        Mockito.when(orderStatusRepository.findByName("COLLECTING")).thenReturn(new OrderStatus("COLLECTING"));
        Mockito.when(orderStatusRepository.findByName("DELIVERING")).thenReturn(new OrderStatus("DELIVERING"));
        Mockito.when(orderStatusRepository.findByName("DELIVERED")).thenReturn(new OrderStatus("DELIVERED"));
        Mockito.when(orderStatusRepository.findByName("CANCELLED")).thenReturn(new OrderStatus("CANCELLED"));
        Mockito.when(orderStatusRepository.findByName("invalid")).thenReturn(null);

        queuedOrder = new Order(new OrderStatus("QUEUED"), null, null, null, null, 3, null);
        queuedOrder.setId(1);
        collectingOrder = new Order(new OrderStatus("COLLECTING"), null, null, null, null, 3, null);
        collectingOrder.setId(2);
        deliveringOrder = new Order(new OrderStatus("DELIVERING"), null, null, null, null, 3, null);
        deliveringOrder.setId(3);
        deliveredOrder = new Order(new OrderStatus("DELIVERED"), null, null, null, null, 3, null);
        deliveredOrder.setId(4);
        cancelledOrder = new Order(new OrderStatus("CANCELLED"), null, null, null, null, 3, null);
        cancelledOrder.setId(5);

        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(queuedOrder));
        Mockito.when(orderRepository.findById(2)).thenReturn(Optional.of(collectingOrder));
        Mockito.when(orderRepository.findById(3)).thenReturn(Optional.of(deliveringOrder));
        Mockito.when(orderRepository.findById(4)).thenReturn(Optional.of(deliveredOrder));
        Mockito.when(orderRepository.findById(5)).thenReturn(Optional.of(cancelledOrder));
        Mockito.when(orderRepository.findById(6)).thenReturn(Optional.ofNullable(null));

        Mockito.when(orderRepository.save(queuedOrder)).thenReturn(collectingOrder);
        Mockito.when(orderRepository.save(collectingOrder)).thenReturn(deliveringOrder);
        Mockito.when(orderRepository.save(deliveringOrder)).thenReturn(deliveredOrder);

        User so = new User("teste", "teste", Roles.SERVICE_OWNER.name());
        Mockito.when(userRepository.findByEmail("teste")).thenReturn(so);
        Mockito.when(riderService.getByEmail(so.getEmail())).thenReturn(Optional.of(new Rider(so, new HashSet<>())));
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        Mockito.when(auth.getPrincipal()).thenReturn("teste");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void whenAcceptOrder_associateLoggedRider() {
        orderService.accept(queuedOrder.getId());
        assertEquals(queuedOrder.getCourier().getUser().getEmail(), "teste");
    }

    @Test
    void whenAcceptedOrder_handleException() {
        Order test = orderService.accept(queuedOrder.getId());

        assertEquals("COLLECTING", test.getOrderStatus().getStatus());
        assertThrows(ResponseStatusException.class, ( () -> orderService.accept(cancelledOrder.getId())));
        assertThrows(ResponseStatusException.class, ( () -> orderService.accept(collectingOrder.getId())));
        assertThrows(ResponseStatusException.class, ( () -> orderService.accept(deliveringOrder.getId()) ));
        assertThrows(ResponseStatusException.class, ( () -> orderService.accept(deliveredOrder.getId())));
        assertThrows(ResponseStatusException.class, ( () -> orderService.accept(6)));
    }

    @Test
    void whenCollectingOrder_handleException() {
        Order test = orderService.collect(collectingOrder.getId());
        assertEquals("DELIVERING", test.getOrderStatus().getStatus());
        assertThrows(ResponseStatusException.class, ( () -> orderService.collect(cancelledOrder.getId())));
        assertThrows(ResponseStatusException.class, ( () -> orderService.collect(queuedOrder.getId()) ));
        assertThrows(ResponseStatusException.class, ( () -> orderService.collect(deliveringOrder.getId()) ));
        assertThrows(ResponseStatusException.class, ( () -> orderService.collect(deliveredOrder.getId())));
        assertThrows(ResponseStatusException.class, ( () -> orderService.collect(6)));
    }

    @Test
    void whenDeliveringOrder_handleException() {
        Order test = orderService.deliver(deliveringOrder.getId());
        assertEquals("DELIVERED", test.getOrderStatus().getStatus());
        assertThrows(ResponseStatusException.class, ( () -> orderService.deliver(cancelledOrder.getId())));
        assertThrows(ResponseStatusException.class, ( () -> orderService.deliver(queuedOrder.getId()) ));
        assertThrows(ResponseStatusException.class, ( () -> orderService.deliver(collectingOrder.getId()) ));
        assertThrows(ResponseStatusException.class, ( () -> orderService.deliver(deliveredOrder.getId())));
        assertThrows(ResponseStatusException.class, ( () -> orderService.deliver(6)));
    }


    @Test
    void whenCancellingOrder_handleException() {
        assertThrows(ResponseStatusException.class, ( () -> orderService.cancel(cancelledOrder.getId())));
        assertThrows(ResponseStatusException.class, ( () -> orderService.cancel(deliveredOrder.getId()) ));
        assertThrows(ResponseStatusException.class, ( () -> orderService.cancel(6)));

        assertDoesNotThrow(() -> orderService.cancel(queuedOrder.getId()));
        assertDoesNotThrow(() -> orderService.cancel(deliveringOrder.getId()));
        assertDoesNotThrow(() -> orderService.cancel(collectingOrder.getId()));
    }

}
