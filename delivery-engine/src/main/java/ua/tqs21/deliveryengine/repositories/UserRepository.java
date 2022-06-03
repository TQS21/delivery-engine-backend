package ua.tqs21.deliveryengine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tqs21.deliveryengine.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
