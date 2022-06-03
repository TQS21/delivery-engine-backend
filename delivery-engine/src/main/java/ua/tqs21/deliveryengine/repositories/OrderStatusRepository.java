package ua.tqs21.deliveryengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs21.deliveryengine.models.OrderStatus;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    
}
