package ua.tqs21.deliveryengine.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import ua.tqs21.deliveryengine.dto.ServicePostDTO;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.models.Service;
import ua.tqs21.deliveryengine.models.ServiceOwner;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.ServiceOwnerRepository;
import ua.tqs21.deliveryengine.repositories.ServiceRepository;
import ua.tqs21.deliveryengine.repositories.UserRepository;
import ua.tqs21.deliveryengine.utils.AddressResolver;

@org.springframework.stereotype.Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceOwnerRepository serviceOwnerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressResolver addressResolver;

    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    public Service createServiceFromDTO(ServicePostDTO servicePostDTO) throws URISyntaxException, ParseException, IOException {
        Service created = new Service();
        String ownerEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User owner = userRepository.findByEmail(ownerEmail);
        System.out.println("OWNER: " + owner);
        if (owner != null) {
            Optional<ServiceOwner> so = serviceOwnerRepository.findById(owner.getId());
            if (so.isPresent()) {
                System.out.println("SO: " + so.get());
                created.setUser(so.get());
                created.setName(servicePostDTO.getName());
                created.setDeliveries(new HashSet<>());

                Optional<Address> address = addressResolver.resolveAddress(servicePostDTO.getAddress());

                if (address.isPresent()) {
                    created.setAddress(address.get());
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address could not be resolved.");
                }
                return serviceRepository.save(created);
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);

    }

    public List<Service> getAll() {
        return this.serviceRepository.findAll();
    }

    public Service getById(int id) {
        return this.serviceRepository.findById(id).orElse(null);
    }

    public Service update(int id, ServicePostDTO servicePostDTO) {
        Optional<Service> fromDb = serviceRepository.findById(id);

        if (fromDb.isPresent()) {
            Service found = fromDb.get();
            found.setName(servicePostDTO.getName());
            serviceRepository.save(found);
            return found;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shop not found.");
    }
}
