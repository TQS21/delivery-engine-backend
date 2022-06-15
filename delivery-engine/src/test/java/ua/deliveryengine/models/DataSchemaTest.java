package ua.deliveryengine.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.models.OrderStatus;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.models.Service;
import ua.tqs21.deliveryengine.models.ServiceOwner;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.models.UserRole;

class DataSchemaTest {
    
    @Test
    void user_tests() {
        UserRole test = new UserRole();

        test.setRole("teste");
        test.setId(3);

        assertEquals("teste", test.getRole());
        assertEquals(3, test.getId());

        User user1 = new User("1", "A", Roles.ADMIN.name());
        User user2 = new User("email", "password", Roles.RIDER.name());
        User user3 = new User();

        Admin admin = new Admin();
        Admin adminwithUser = new Admin(new User());
        adminwithUser.getUser().setId(56);
        assertEquals(56, adminwithUser.getUser().getId());
        admin.setUser(user3);
        assertEquals(user3, admin.getUser());
        admin.getUser().setId(3);
        assertEquals(3, admin.getUser().getId());

        Rider rider = new Rider(user2, new HashSet<Order>());
        rider.setId(0);
        assertEquals(0, rider.getId());

        rider.setUser(user3);
        assertEquals(user3, rider.getUser());
        ServiceOwner so = new ServiceOwner(user3, new HashSet<>());

        assertEquals(admin.getId(), user1.getId());
        admin.getUser().setPassword("teste");
        assertEquals(admin.getUser().getPassword(), user3.getPassword());

        assertEquals(0, rider.getDeliveries().size());
        assertNull(so.getUser().getEmail());
        so.getUser().setEmail("velho");
        assertEquals(so.getUser().getEmail(), "velho" );
    }
    
    @Test
    void address_tests() throws Exception {
        Address valid = new Address(15, -2);

        assertEquals(15, valid.getLatitude());
        valid.setLongitude(-5);
        assertEquals(-5, valid.getLongitude());

        assertThrows(IllegalStateException.class, () ->  { 
            new Address(-1000, 0);
            new Address(1000, 0);
            new Address(0, 1000);
            new Address(0, -1000);
            new Address(0, 0).setLatitude(10000);
            new Address(0, 0).setLatitude(-10000);
            new Address(0, 0).setLongitude(10000);
            new Address(0, 0).setLongitude(-10000);
        });
    }

    @Test
    void services_tests() {
        User owner = new User("teste", "omega", "teste");
        Set<Service> services = new HashSet<>();
        ServiceOwner serviceOwner = new ServiceOwner(owner, services);

        Service s1 = new Service("s1", serviceOwner, new Address(), new HashSet<Order>());
        Service s2 = new Service("s2", serviceOwner, new Address(), new HashSet<Order>());
        services.add(s1);
        services.add(s2);


        serviceOwner.setServices(services);
        assertEquals(2, serviceOwner.getServices().size());
        serviceOwner.getServices().add(s2);
        assertEquals(true, serviceOwner.getServices().contains(s1));

        s1.setUser(new ServiceOwner());
        s1.setName("s3");
        Address a = new Address();
        s1.setAddress(a);
        assertEquals("s3", s1.getName());
        assertEquals(a, s1.getAddress());
    }

    @Test
    void order_tests() {
        User r = new User("teste", "teste", "rider");
        Set<Order> orders = new HashSet<>();
        Rider rider = new Rider(r, orders);

        Order o1 = new Order(new OrderStatus(), new Date(), new Date(), rider, new Service());
        Order o2 = new Order();

        orders.add(o1); orders.add(o2);

        rider.setDeliveries(orders);;
        assertEquals(true, rider.getDeliveries().contains(o1));
        assertEquals(true, rider.getDeliveries().contains(o2));
        o1.setCourier(rider);
        assertEquals(o1.getCourier().getId(), r.getId());
        
        OrderStatus status1 = new OrderStatus("a");

        o1.setOrderStatus(status1);
        assertEquals(status1, o1.getOrderStatus());
        Service s1 = new Service();
        o1.setShop(s1);
        assertEquals(s1, o1.getShop());

        status1.setId(5);
        assertEquals(5, o1.getOrderStatus().getId());
        status1.setStatus("teste");
        assertEquals("teste", status1.getStatus());

    }
}
