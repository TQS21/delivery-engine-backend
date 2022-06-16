package ua.tqs21.deliveryengine.controllers;

import java.util.List;

import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs21.deliveryengine.dto.OrderPostDTO;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.services.OrderService;

@RestController
@RequestMapping("/delivery")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping("/")
    public Order postOrderFromDto(@RequestBody OrderPostDTO order) {
        return orderService.createOrderFromDTO(order);
    }

    @GetMapping("/")
    public List<Order> getOrders() {
        System.out.println("entrou");
        throw new NotYetImplementedException();
    }
}
