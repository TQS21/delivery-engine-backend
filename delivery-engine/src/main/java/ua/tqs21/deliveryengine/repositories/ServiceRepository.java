package ua.tqs21.deliveryengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ua.tqs21.deliveryengine.models.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    
}
