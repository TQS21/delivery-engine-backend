package ua.tqs21.deliveryengine.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs21.deliveryengine.dto.OrderPostDTO;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.services.OrderService;

@RestController
@RequestMapping("/delivery")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping("/")
    public Order postOrderFromDto(@RequestBody OrderPostDTO order) throws URISyntaxException, ParseException, IOException {
        return orderService.createOrderFromDTO(order);
    }

    @GetMapping("/")
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable int id) {
        return this.orderService.getOrderById(id);
    }

    @GetMapping("/active")
    public List<Order> getActiveOrders() {
        return this.orderService.getActiveOrders();
    }

    @PostMapping("/nearby")
    public List<Order> getNearbyOrders(@RequestBody Address source) throws Exception {
        return orderService.getNearbyOrders(source);
    }

    @PostMapping("/{id}/accept")
    public Order acceptOrder(@PathVariable("id")  int id) {
        return this.orderService.accept(id);
    }

    @PostMapping("/{id}/collect")
    public Order collectOrder(@PathVariable("id")  int id) {
        return this.orderService.collect(id);
    }

    @PostMapping("/{id}/deliver")
    public Order deliverOrder(@PathVariable("id")  int id) {
        return this.orderService.deliver(id);
    }

    @PostMapping("/{id}/cancel")
    public Order cancelOrder(@PathVariable("id")  int id) {
        return this.orderService.cancel(id);
    }
}
