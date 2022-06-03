package ua.tqs21.deliveryengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs21.deliveryengine.models.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    
}
