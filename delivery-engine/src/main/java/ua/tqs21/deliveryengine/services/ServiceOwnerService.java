package ua.tqs21.deliveryengine.services;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.ServiceOwner;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.ServiceOwnerRepository;

@Service
public class ServiceOwnerService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ServiceOwnerRepository soRepository;

    public ServiceOwner saveServiceOwner(ServiceOwner so) {
        return soRepository.save(so);
    }

    public List<ServiceOwner> saveServiceOwner(List<ServiceOwner> so) {
        return soRepository.saveAll(so);
    }

    public ServiceOwner saveAdminFromUser(UserDTO user) {
        return new ServiceOwner(new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), Roles.SERVICE_OWNER.name()), new HashSet<>());
    }

    public List<ServiceOwner> getServiceOwners() {
        return soRepository.findAll();
    }

    public ServiceOwner getServiceOwnerById(int id) {
        return soRepository.findById((int)id).orElse(null);
    }


    public String deleteServiceOwner(int id) {
        soRepository.deleteById(id);
        return String.valueOf(id);
    }

    public ServiceOwner updateServiceOwner(ServiceOwner so) {
        System.out.println(so);
        ServiceOwner existingServiceOwner = soRepository.findById((int)so.getId()).orElse(null);
        existingServiceOwner.setUser(so.getUser());
        existingServiceOwner.setServices(so.getServices());
        return soRepository.save(existingServiceOwner);
    }
}
