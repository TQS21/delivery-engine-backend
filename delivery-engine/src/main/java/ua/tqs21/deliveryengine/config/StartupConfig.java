package ua.tqs21.deliveryengine.config;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.models.Address;
import ua.tqs21.deliveryengine.models.Service;
import ua.tqs21.deliveryengine.services.ServiceOwnerService;
import ua.tqs21.deliveryengine.services.ServiceService;

@Component
@Profile("!test")
@Transactional
public class StartupConfig implements CommandLineRunner {

    @Autowired
    private ServiceOwnerService serviceOwnerService;
    
    @Autowired
    private ServiceService serviceService;

    @Override
    public
    void run(String ...args) throws Exception {
        System.out.println("running command line runner...");
        if (serviceOwnerService.getServiceOwners().size() == 0) {
            serviceOwnerService.saveOwnerFromUser(new UserDTO("hmlowner@gmail.com", "hmlowner"));
            serviceService.createService(new Service("HML - Howl's Moving Library", this.serviceOwnerService.getServiceOwnerById(1), new Address(6.75, -73.35), new HashSet<>()));
        }
    }
}
