package ua.tqs21.deliveryengine.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ua.tqs21.deliveryengine.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findAllByStatusName(String statusName);
}
