package ua.deliveryengine.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.dto.ServicePostDTO;
import ua.tqs21.deliveryengine.models.*;
import ua.tqs21.deliveryengine.repositories.ServiceOwnerRepository;
import ua.tqs21.deliveryengine.repositories.ServiceRepository;
import ua.tqs21.deliveryengine.repositories.UserRepository;
import ua.tqs21.deliveryengine.services.ServiceService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTests {

    @Mock(lenient = true)
    private ServiceRepository serviceRepository;

    @Mock(lenient = true)
    private ServiceOwnerRepository serviceOwnerRepository;

    @Mock(lenient = true)
    private UserRepository userRepository;

    @InjectMocks
    private ServiceService serviceService;


    private ServiceOwner user = new ServiceOwner(new User("testing", "psw", new UserRole("SO")), null);
    private Address address = new Address();
    private Set<Order> deliveries = new HashSet<Order>();
    private Service service = new Service("test", user, address, deliveries);

    @BeforeEach
    void setup(){
        List<Service> services = new ArrayList<>();
        services.add(service);
        //System.out.println(SecurityContextHolder.getContext());
        //Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user.getUser().getEmail());

        Mockito.when(serviceRepository.save(service)).thenReturn(service);
        Mockito.when(serviceRepository.findById(service.getId())).thenReturn(Optional.of(service));
        Mockito.when(serviceRepository.findAll()).thenReturn(services);

        Mockito.when(userRepository.findByEmail(user.getUser().getEmail())).thenReturn(user.getUser());
        Mockito.when(userRepository.findByEmail("not valid")).thenReturn(null);

    }

    @Test
    void whenCreateService_returnsService(){
        assertThat(serviceService.createService(service)).isEqualTo(service);
    }

    @Test
    void whenValidId_returnsService(){
        assertThat(serviceService.getById(service.getId())).isEqualTo(service);
    }

    @Test
    void whenInvalidId_returnsService(){
        assertThat(serviceService.getById(-1)).isNull();
    }

    @Test
    void whenGetAllServices_returnsAllServices(){
        List<Service> services = new ArrayList<>();
        services.add(service);
        assertThat(serviceService.getAll()).isEqualTo(services);
    }

    @Test
    void whenCreateServiceFromDTOService_returnsServiceObject(){
        AddressPostDTO addressDTO = new AddressPostDTO();
        ServicePostDTO servicePostDTO = new ServicePostDTO("update", addressDTO);
        serviceService.createServiceFromDTO(servicePostDTO);

    }

    @Test
    void whenCreateServiceFromValidDTOService_returnsNotFound(){

    }

    @Test
    void whenUpdateService_returnsUpdatedService(){
        assertThat(serviceService.getById(service.getId()).getName()).isEqualTo("test");
        AddressPostDTO addressDTO = new AddressPostDTO();
        ServicePostDTO servicePostDTO = new ServicePostDTO("update", addressDTO);
        assertThat(serviceService.update(service.getId(),servicePostDTO).getName()).isEqualTo("update");
    }

    @Test
    void whenUpdateInvalidService_returnsNotFound(){
        AddressPostDTO addressDTO = new AddressPostDTO();
        ServicePostDTO servicePostDTO = new ServicePostDTO("update", addressDTO);
        assertThrows(ResponseStatusException.class, () -> this.serviceService.update(-1,servicePostDTO));
    }
}
