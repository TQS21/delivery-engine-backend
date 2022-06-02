package ua.deliveryengine.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.models.OrderStatus;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.models.Service;
import ua.tqs21.deliveryengine.models.ServiceOwner;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.models.UserRole;

public class DataSchemaTest {
    
    @Test
    public void user_tests() {
        UserRole admin_role = new UserRole("Admin");
        UserRole rider_role = new UserRole("Rider");
        UserRole test = new UserRole();

        test.setRole("teste");
        test.setId(3);

        assertEquals(test.getRole(), "teste");
        assertEquals(test.getId(), 3);

        User user1 = new User("1", "A", admin_role);
        User user2 = new User("email", "password", rider_role);
        User user3 = new User();

        Admin admin = new Admin(user1);
        admin.setUser(user3);
        assertEquals(admin.getUser(), user3);
        admin.getUser().setId(3);
        assertEquals(admin.getUser().getId(), 3);

        Rider rider = new Rider(user2, new HashSet<Order>());
        ServiceOwner so = new ServiceOwner(user3, new HashSet<>());

        assertEquals(admin.getId(), user1.getId());
        admin.getUser().setPassword("teste");
        assertEquals(admin.getUser().getPassword(), user3.getPassword());

        assertEquals(rider.getDeliveries().size(), 0 );
        assertNull(so.getUser().getUsername());
        so.getUser().setUsername("velho");
        assertEquals(so.getUser().getUsername(), "velho" );
    }
    
    @Test
    public void address_tests() throws Exception {
        Address valid = new Address(15, -2);

        assertEquals(valid.getLatitude(), 15);
        valid.setLongitude(-5);
        assertEquals(valid.getLongitude(), -5);

        assertThrows(IllegalStateException.class, () ->  new Address(-1000, 0));
        assertThrows(IllegalStateException.class, () ->  new Address(1000, 0));
        assertThrows(IllegalStateException.class, () -> new Address(0, 1000));
        assertThrows(IllegalStateException.class, () -> new Address(0, -1000));

        assertThrows(IllegalStateException.class, () ->  new Address(0, 0).setLatitude(10000));
        assertThrows(IllegalStateException.class, () ->  new Address(0, 0).setLatitude(-10000));
        assertThrows(IllegalStateException.class, () ->  new Address(0, 0).setLongitude(10000));
        assertThrows(IllegalStateException.class, () ->  new Address(0, 0).setLongitude(-10000));
    }

    @Test
    public void services_tests() {
        User owner = new User("teste", "omega", new UserRole("teste"));
        Set<Service> services = new HashSet<>();
        ServiceOwner serviceOwner = new ServiceOwner(owner, services);

        Service s1 = new Service("s1", serviceOwner, new Address(), new HashSet<Order>());
        Service s2 = new Service("s2", serviceOwner, new Address(), new HashSet<Order>());
        services.add(s1);
        services.add(s2);


        serviceOwner.setServices(services);
        assertEquals(serviceOwner.getServices().size(), 2);
        serviceOwner.getServices().add(s2);
        assertEquals(serviceOwner.getServices().contains(s1), true);

        s1.setUser(new ServiceOwner());
        s1.setName("s3");
        Address a = new Address();
        s1.setAddress(a);
        assertEquals(s1.getName(), "s3");
        assertEquals(s1.getAddress(), a);
    }

    @Test
    public void order_tests() {
        User r = new User("teste", "teste", new UserRole("rider"));
        Set<Order> orders = new HashSet<>();
        Rider rider = new Rider(r, orders);

        Order o1 = new Order(new OrderStatus(), new Date(), new Date(), rider, new Service());
        Order o2 = new Order();

        orders.add(o1); orders.add(o2);

        rider.setDeliveries(orders);;
        assertEquals(rider.getDeliveries().contains(o1), true);
        assertEquals(rider.getDeliveries().contains(o2), true);
        o1.setCourier(rider);
        assertEquals(o1.getCourier().getId(), r.getId());
        
        OrderStatus status1 = new OrderStatus("a");

        o1.setOrderStatus(status1);
        assertEquals(o1.getOrderStatus(), status1);
        Service s1 = new Service();
        o1.setShop(s1);
        assertEquals(o1.getShop(), s1);

        status1.setId(5);
        assertEquals(o1.getOrderStatus().getId(), 5);
        status1.setStatus("teste");
        assertEquals(status1.getStatus(), "teste");

    }
}
