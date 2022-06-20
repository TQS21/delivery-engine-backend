package ua.tqs21.deliveryengine.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.models.OrderStatus;
import ua.tqs21.deliveryengine.models.User;

@DataJpaTest
@AutoConfigureTestDatabase
public class UserDefinedFunctionsRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @BeforeEach
    void populate() {
        testEntityManager.persist(new User("teste", "teste", "teste"));
        testEntityManager.persist(new User("admin@ua.pt", "l2", "teste2"));
        testEntityManager.persist(new User("aaaa", "aaaa", "aaaa"));

        testEntityManager.flush();

        testEntityManager.persist(new OrderStatus("STATUS1"));
        testEntityManager.persist(new OrderStatus("STATUS2"));

        testEntityManager.flush();
        

        OrderStatus active = new OrderStatus("ACTIVE");
        OrderStatus pending = new OrderStatus("PENDING");

        testEntityManager.persist(active);
        testEntityManager.persist(pending);

        Order o1 = new Order();
        o1.setOrderStatus(active);
        Order o2 = new Order();
        o2.setOrderStatus(pending);
        Order o3 = new Order();
        o3.setOrderStatus(active);
        testEntityManager.persist(o1);
        testEntityManager.persist(o2);
        testEntityManager.persist(o3);

        testEntityManager.flush();

    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
        orderStatusRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    void whenFindUserByEmail_returnCorrectUser() {
        User found = userRepository.findByEmail("teste");

        assertEquals("teste", found.getPassword());
        assertEquals("teste", found.getRole());

        User notfound = userRepository.findByEmail("uhrfquiwehr");

        assertNull(notfound);
    }

    @Test
    void whenFindOrderStatusByName_returnCorrectStatus() {
        OrderStatus found = orderStatusRepository.findByName("STATUS1");

        assertEquals("STATUS1", found.getStatus());

        OrderStatus notfound = orderStatusRepository.findByName("iwrhqwihe");

        assertNull(notfound);
    }

    @Test
    void whenFindOrderByStatus_returnListOfOrders() {
        List<Order> activeOrders = orderRepository.findAllByStatusName("ACTIVE");

        assertThat(activeOrders).hasSize(2).extracting((o) -> o.getOrderStatus().getStatus()).containsOnly("ACTIVE");

        List<Order> pending = orderRepository.findAllByStatusName("PENDING");

        assertThat(pending).hasSize(1).extracting((o) -> o.getOrderStatus().getStatus()).containsOnly("PENDING");

        List<Order> invalid = orderRepository.findAllByStatusName("null");

        assertThat(invalid).hasSize(0);
    }
}
