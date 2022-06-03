package ua.tqs21.deliveryengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs21.deliveryengine.models.ServiceOwner;

public interface ServiceOwnerRepository extends JpaRepository<ServiceOwner, Integer> {
    
}