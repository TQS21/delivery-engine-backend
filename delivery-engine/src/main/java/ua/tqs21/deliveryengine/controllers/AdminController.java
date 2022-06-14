package ua.tqs21.deliveryengine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.services.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/")
    public List<Admin> findAllAdmins() {
        return adminService.getAdmins();
    }

    @PostMapping("/")
    public Admin postAdmin(@RequestBody UserDTO userDTO) {
        return this.adminService.saveAdminFromUser(userDTO);
    }

    @GetMapping("/{id}")
    public Admin findAdminById(@PathVariable("id")  int id) {
        return adminService.getAdminById(id);   
    }

    @PutMapping("/")
    public Admin updateAdmin(@RequestBody User admin) {
        return adminService.updateAdmin(admin);
    }

    @DeleteMapping("/{id}")
    public String deleteAdmin(@PathVariable("id") int id) {
        return adminService.deleteAdmin(id);
    }
}
