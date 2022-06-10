package ua.tqs21.deliveryengine.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.tqs21.deliveryengine.models.UserRole;
import ua.tqs21.deliveryengine.repositories.UserRoleRepository;

@Service
public class UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRole saveUserRole(UserRole role) {
        return userRoleRepository.save(role);
    }

    public List<UserRole> saveUserRole(List<UserRole> role) {
        return userRoleRepository.saveAll(role);
    }

    public List<UserRole> getUserRoles() {
        return userRoleRepository.findAll();
    }

    public UserRole getUserRoleById(int id) {
        return userRoleRepository.findById((int)id).orElse(null);
    }

    public String deleteUserRole(int id) {
        userRoleRepository.deleteById(id);
        return String.valueOf(id);
    }

    public UserRole updateUser(UserRole role) {
        UserRole existingUserRole = userRoleRepository.findById((int)role.getId()).orElse(null);
        existingUserRole.setRole(role.getRole());
        return userRoleRepository.save(existingUserRole);
    }
}
