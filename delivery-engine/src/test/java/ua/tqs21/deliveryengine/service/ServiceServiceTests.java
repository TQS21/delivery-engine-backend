package ua.tqs21.deliveryengine.service;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;
import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.dto.ServicePostDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.*;
import ua.tqs21.deliveryengine.repositories.ServiceOwnerRepository;
import ua.tqs21.deliveryengine.repositories.ServiceRepository;
import ua.tqs21.deliveryengine.repositories.UserRepository;
import ua.tqs21.deliveryengine.services.ServiceService;
import ua.tqs21.deliveryengine.utils.AddressResolver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTests {

    @Mock(lenient = true)
    private ServiceRepository serviceRepository;

    @Mock(lenient = true)
    private ServiceOwnerRepository serviceOwnerRepository;

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private AddressResolver addressResolver;

    @Mock(lenient = true)
    private Authentication auth;

    @Mock(lenient = true)
    private SecurityContext securityContext;

    @InjectMocks
    private ServiceService serviceService;


    private ServiceOwner user = new ServiceOwner(new User("testing", "psw", "SO"), null);
    private Address address = new Address();
    private Set<Order> deliveries = new HashSet<Order>();
    private Service service = new Service("test", user, address, deliveries);

    @BeforeEach
    void setup() throws URISyntaxException, ParseException, IOException{
        List<Service> services = new ArrayList<>();
        services.add(service);
        //System.out.println(SecurityContextHolder.getContext());
        //Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user.getUser().getEmail());

        Mockito.when(serviceRepository.save(service)).thenReturn(service);
        Mockito.when(serviceRepository.findById(service.getId())).thenReturn(Optional.of(service));
        Mockito.when(serviceRepository.findAll()).thenReturn(services);

        Mockito.when(userRepository.findByEmail(user.getUser().getEmail())).thenReturn(user.getUser());
        Mockito.when(userRepository.findByEmail("not valid")).thenReturn(null);

        User so = new User("teste", "teste", Roles.SERVICE_OWNER.name());
        Mockito.when(userRepository.findByEmail("teste")).thenReturn(so);
        Mockito.when(serviceOwnerRepository.findById(so.getId())).thenReturn(Optional.of(new ServiceOwner(so, new HashSet<>())));
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        Mockito.when(auth.getPrincipal()).thenReturn("teste");
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(addressResolver.resolveAddress(any(AddressPostDTO.class))).thenReturn(Optional.of(new Address()));
        Mockito.when(addressResolver.estimateDeliverTs(any(Address.class), any(Address.class))).thenReturn(new Date());

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
    @WithMockUser
    void whenCreateServiceFromDTOService_returnsServiceObject() throws URISyntaxException, ParseException, IOException{
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
