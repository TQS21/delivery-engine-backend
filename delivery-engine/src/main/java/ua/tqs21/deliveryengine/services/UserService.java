package ua.tqs21.deliveryengine.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> saveUser(List<User> user) {
        return userRepository.saveAll(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id) {
        User u = userRepository.findById((int)id).orElse(null);

        if (u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }

        return u;
    }

    public User getUserByEmail(String email) {
        User u = userRepository.findByEmail(email);

        if (u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }

        return u;
    }

    public String deleteUser(int id) {
        userRepository.deleteById(id);
        return String.valueOf(id);
    }

    public User updateUser(UserDTO user) {
        System.out.println(user);
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        return userRepository.save(existingUser);
    }
}
