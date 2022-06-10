package ua.tqs21.deliveryengine.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.AdminRepository;
import ua.tqs21.deliveryengine.repositories.UserRoleRepository;

@Service
public class AdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Admin> saveAdmins(List<Admin> admin) {
        return adminRepository.saveAll(admin);
    }

    public Admin saveAdminFromUser(UserDTO user) {
        return new Admin(new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), userRoleRepository.findByRole(Roles.ADMIN.name())));
    }

    public List<Admin> getAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(int id) {
        return adminRepository.findById((int)id).orElse(null);
    }


    public String deleteAdmin(int id) {
        adminRepository.deleteById(id);
        return String.valueOf(id);
    }

    public Admin updateAdmin(Admin admin) {
        System.out.println(admin);
        Admin existingAdmin = adminRepository.findById((int)admin.getId()).orElse(null);
        existingAdmin.setUser(admin.getUser());
        return adminRepository.save(existingAdmin);
    }
}
