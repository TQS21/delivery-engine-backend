package ua.tqs21.deliveryengine.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ua.tqs21.deliveryengine.dto.OrderPostDTO;
import ua.tqs21.deliveryengine.enums.OrdStatus;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.models.OrderStatus;
import ua.tqs21.deliveryengine.repositories.OrderRepository;
import ua.tqs21.deliveryengine.repositories.OrderStatusRepository;
import ua.tqs21.deliveryengine.utils.AddressResolver;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ServiceService servicesService;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public Order createOrderFromDTO(OrderPostDTO orderPostDTO) {
        Order created = new Order();
        ua.tqs21.deliveryengine.models.Service orderOrigin = servicesService.getById(created.getId());

        if (orderOrigin == null) {
            System.out.println("orderOrigin not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        created.setCourier(null);
        created.setShop(orderOrigin);
        created.setTimestamp(new Date());
        created.setDelivery_timestamp(AddressResolver.estimateDeliverTs(AddressResolver.resolveAddress(orderPostDTO.getAddress()), orderOrigin.getAddress()));
        created.setShopOrderRef(orderPostDTO.getShopOrderRef());
        created.setContact(orderPostDTO.getClient());

        OrderStatus status = orderStatusRepository.findByName(OrdStatus.QUEUED.name());

        if (status == null) {
            status = orderStatusRepository.save(new OrderStatus(OrdStatus.QUEUED.name()));
        }

        created.setOrderStatus(status);

        return created;
    }
}
