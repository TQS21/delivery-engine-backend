package ua.tqs21.deliveryengine.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    
    @GetMapping("/")
    public List<User> findAllUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id")  int id) {
        return userService.getUserById(id);   
    }

    @PutMapping("/")
    public User updateUser(@RequestBody UserDTO user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        return userService.deleteUser(id);
    }
}
