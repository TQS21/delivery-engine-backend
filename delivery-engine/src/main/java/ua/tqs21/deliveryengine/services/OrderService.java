package ua.tqs21.deliveryengine.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ua.tqs21.deliveryengine.dto.OrderPostDTO;
import ua.tqs21.deliveryengine.enums.OrdStatus;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.models.OrderStatus;
import ua.tqs21.deliveryengine.models.Rider;
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

    @Autowired
    private AddressResolver addressResolver;

    @Autowired
    private RiderService riderService;

    public Order createOrderFromDTO(OrderPostDTO orderPostDTO) throws URISyntaxException, ParseException, IOException {
        Order created = new Order();
        ua.tqs21.deliveryengine.models.Service orderOrigin = servicesService.getById(orderPostDTO.getShopId());

        if (orderOrigin == null) {
            System.out.println("orderOrigin not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        created.setCourier(null);
        created.setShop(orderOrigin);
        created.setTimestamp(new Date());

        Optional<Address> from = addressResolver.resolveAddress(orderPostDTO.getAddress());

        if (from.isPresent()) {
            created.setDelivery_timestamp(addressResolver.estimateDeliverTs(from.get(), orderOrigin.getAddress()));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not resolve address");
        }

        created.setShopOrderRef(orderPostDTO.getShopOrderRef());
        created.setContact(orderPostDTO.getClient());

        OrderStatus status = orderStatusRepository.findByName(OrdStatus.QUEUED.name());

        if (status == null) {
            status = orderStatusRepository.save(new OrderStatus(OrdStatus.QUEUED.name()));
        }

        created.setOrderStatus(status);

        return orderRepository.save(created);
    }

    public Order accept(int orderid) {
        Optional<Order> fromDb = orderRepository.findById(orderid);

        if (fromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        Order order = fromDb.get();
        OrderStatus orderStatus = orderStatusRepository.findByName(OrdStatus.COLLECTING.name());

        if (orderStatus == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Status");
        }

        if (order.getOrderStatus().getStatus().equals(orderStatusRepository.findByName(OrdStatus.QUEUED.name()).getStatus())) {
            order.setOrderStatus(orderStatus);
            String loggedInEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Rider> rider = riderService.getByEmail(loggedInEmail);

            if (rider.isPresent()) {
                order.setCourier(rider.get());
                return orderRepository.save(order);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Rider");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can Only Accept QUEUED Orders");
    }


    public Order collect(int orderid) {
        Optional<Order> fromDb = orderRepository.findById(orderid);

        if (fromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        Order order = fromDb.get();
        OrderStatus orderStatus = orderStatusRepository.findByName(OrdStatus.DELIVERING.name());

        if (orderStatus == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Status");
        }

        if (order.getOrderStatus().getStatus().equals(orderStatusRepository.findByName(OrdStatus.COLLECTING.name()).getStatus())) {
            order.setOrderStatus(orderStatus);
            String loggedInEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Rider> rider = riderService.getByEmail(loggedInEmail);

            if (rider.isPresent()) {
                return orderRepository.save(order);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Rider");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can Only collect COLLECTING Orders");
    }

    public Order deliver(int orderid) {
        Optional<Order> fromDb = orderRepository.findById(orderid);

        if (fromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        Order order = fromDb.get();
        OrderStatus orderStatus = orderStatusRepository.findByName(OrdStatus.DELIVERED.name());

        if (orderStatus == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Status");
        }

        if (order.getOrderStatus().getStatus().equals(orderStatusRepository.findByName(OrdStatus.DELIVERING.name()).getStatus())) {
            order.setOrderStatus(orderStatus);
            String loggedInEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Rider> rider = riderService.getByEmail(loggedInEmail);

            if (rider.isPresent()) {
                return orderRepository.save(order);
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Rider");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can Only Deliver DELIVERING Orders");
    }


    public Order cancel(int orderid) {
        Optional<Order> fromDb = orderRepository.findById(orderid);

        if (fromDb.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        Order order = fromDb.get();
        OrderStatus orderStatus = orderStatusRepository.findByName(OrdStatus.CANCELLED.name());

        if (orderStatus == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Status");
        }

        if (order.getOrderStatus().getStatus().equals(orderStatusRepository.findByName(OrdStatus.CANCELLED.name()).getStatus()) || order.getOrderStatus().getStatus().equals(orderStatusRepository.findByName(OrdStatus.DELIVERED.name()).getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order Cancelled or Delivered already");
        }

        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }
}
