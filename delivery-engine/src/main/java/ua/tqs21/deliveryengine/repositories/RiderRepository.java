package ua.tqs21.deliveryengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs21.deliveryengine.models.Rider;

public interface RiderRepository extends JpaRepository<Rider, Integer> {
    
}
