package ua.tqs21.deliveryengine.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ua.tqs21.deliveryengine.dto.RiderPostDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.RiderRepository;
import ua.tqs21.deliveryengine.repositories.UserRepository;

@Service
public class RiderService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private UserRepository userRepository;

    public Rider saveRider(Rider rider) {
        return riderRepository.save(rider);
    }

    public List<Rider> saveRider(List<Rider> rider) {
        return riderRepository.saveAll(rider);
    }

    public Rider saveRiderFromDto(RiderPostDTO riderPostDTO) {
        Rider created = new Rider();
        created.setUser(new User(riderPostDTO.getEmail(), passwordEncoder.encode(riderPostDTO.getPassword()), Roles.RIDER.name()));
        created.setDeliveries(new HashSet<>());
        return riderRepository.save(created);
    }

    public List<Rider> getRiders() {
        return riderRepository.findAll();
    }

    public Rider getRiderById(int id) {
        return riderRepository.findById((int)id).orElse(null);
    }


    public String deleteRider(int id) {
        riderRepository.deleteById(id);
        return String.valueOf(id);
    }

    public Rider updateRider(RiderPostDTO rider) {
        User u = userRepository.findByEmail(rider.getEmail());

        if (u == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Rider existingRider = riderRepository.findById(u.getId()).orElse(null);

        if (existingRider == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rider not found");
        }

        User updated = new User();
        updated.setEmail(u.getEmail());
        updated.setPassword(u.getPassword());
        existingRider.setUser(updated);
        return riderRepository.save(existingRider);
    }

    public Optional<Rider> getByEmail(String email) {
        User u = userRepository.findByEmail(email);
        
        if (u == null) {
            return Optional.ofNullable(null);
        }

        return riderRepository.findById(u.getId());
    }
}
