package ua.tqs21.deliveryengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs21.deliveryengine.models.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    
}
