package ua.tqs21.deliveryengine.config;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.dto.ServicePostDTO;
import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.models.ServiceOwner;
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
            serviceService.createServiceFromDTO(new ServicePostDTO("Howl's Moving Library", new AddressPostDTO("Portugal", "4505-519", "Rua Santo Andre n166")));
        }
    }
}
