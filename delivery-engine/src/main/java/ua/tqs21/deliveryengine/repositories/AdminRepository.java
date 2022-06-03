package ua.tqs21.deliveryengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs21.deliveryengine.models.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
}
