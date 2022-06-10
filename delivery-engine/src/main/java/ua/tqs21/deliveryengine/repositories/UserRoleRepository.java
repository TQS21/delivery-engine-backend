package ua.tqs21.deliveryengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs21.deliveryengine.models.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    UserRole findByRole(String role);
}
