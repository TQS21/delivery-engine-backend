package ua.tqs21.deliveryengine.services;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.tqs21.deliveryengine.dto.RiderPostDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.RiderRepository;

@Service
public class RiderService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RiderRepository riderRepository;

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

    public Rider updateRider(Rider rider) {
        Rider existingRider = riderRepository.findById((int)rider.getId()).orElse(null);
        existingRider.setUser(rider.getUser());
        existingRider.setDeliveries(rider.getDeliveries());
        return riderRepository.save(existingRider);
    }
}
