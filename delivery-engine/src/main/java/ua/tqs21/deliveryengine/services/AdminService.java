package ua.tqs21.deliveryengine.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.AdminRepository;
import ua.tqs21.deliveryengine.repositories.UserRepository;

@Service
public class AdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;


    public Admin saveAdmin(User admin) {
        return adminRepository.save(new Admin(admin));
    }

    public Admin saveAdminFromUser(UserDTO user) {
        return adminRepository.save(new Admin(new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), Roles.ADMIN.name())));
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

    public Admin updateAdmin(UserDTO admin) {
        User existingAdmin = userRepository.findByEmail(admin.getEmail());
        
        if (existingAdmin == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Admin adm = adminRepository.findById(existingAdmin.getId()).orElse(null);

        if (adm == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        existingAdmin.setEmail(admin.getEmail());
        existingAdmin.setPassword(admin.getPassword());
        adm.setUser(existingAdmin);
        return adminRepository.save(adm);
    }
}
