package ua.tqs21.deliveryengine.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import ua.tqs21.deliveryengine.dto.RiderPostDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.models.ServiceOwner;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.AdminRepository;
import ua.tqs21.deliveryengine.repositories.RiderRepository;
import ua.tqs21.deliveryengine.repositories.ServiceOwnerRepository;
import ua.tqs21.deliveryengine.repositories.UserRepository;
import ua.tqs21.deliveryengine.services.AdminService;
import ua.tqs21.deliveryengine.services.RiderService;
import ua.tqs21.deliveryengine.services.ServiceOwnerService;
import ua.tqs21.deliveryengine.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServicesTest {
    @Mock(lenient = true)
    private PasswordEncoder passwordEncoder;

    @Mock(lenient = true)
    private UserRepository userRepository;

    @Mock(lenient = true)
    private AdminRepository adminRepository;

    @Mock(lenient = true)
    private RiderRepository riderRepository;

    @Mock(lenient = true)
    private ServiceOwnerRepository serviceOwnerRepository;


    @InjectMocks
    private UserService userService;

    @InjectMocks
    private AdminService adminService;

    @InjectMocks
    private RiderService riderService;

    @InjectMocks
    private ServiceOwnerService serviceOwnerService;

    private List<User> users;

    @BeforeEach
    void setup() {
        User base = new User("base", "psw", null);
        Rider rider1 = new Rider(new User("vasco", "vasco", Roles.RIDER.name()), null);
        Rider rider2 = new Rider(new User("email", "psw", Roles.RIDER.name()), null);
        Admin a1 = new Admin(base);
        ServiceOwner so1 = new ServiceOwner(new User("testing", "psw", Roles.SERVICE_OWNER.name()), null);

        List<Rider> riders = new ArrayList<>();
        riders.add(rider1); riders.add(rider2);
        List<Admin> admins = new ArrayList<>();
        admins.add(a1);
        List<ServiceOwner> serviceOwners = new ArrayList<>();
        serviceOwners.add(so1);

        List<User> users = new ArrayList<>();
        users.add(base); users.add(rider1.getUser()); users.add(rider2.getUser()); users.add(so1.getUser());

        this.users = users;

        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(riderRepository.findById(rider1.getUser().getId())).thenReturn(Optional.of(rider1));
        Mockito.when(userRepository.findByEmail("not valid email")).thenReturn(null);
        Mockito.when(userRepository.findByEmail("vasco")).thenReturn(rider1.getUser());
        Mockito.when(adminRepository.findAll()).thenReturn(admins);
        Mockito.when(serviceOwnerRepository.findAll()).thenReturn(serviceOwners);
        Mockito.when(riderRepository.findAll()).thenReturn(riders);
        Mockito.when(passwordEncoder.encode("teste")).thenReturn("hash");
        Mockito.when(riderRepository.save(any(Rider.class))).thenReturn(new Rider(new User("vasco", "teste", "RIDER"), new HashSet<>()));
    }

    @Test
    void whenGetUsers_ReturnUsers() {
        List<User> users = this.userService.getUsers();

        assertThat(users).hasSize(4).extracting((u) -> u.getEmail()).containsOnly(
            "base",
            "vasco",
            "email",
            "testing"
        );

        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findAll();
    }

    @Test
    void whenUserExists_findExistingEmail() {
        User vasco = this.userService.getUserByEmail("vasco");

        assertThat(this.riderService.getRiderById(vasco.getId()).getUser().getPassword()).isEqualTo("vasco");
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findByEmail(("vasco"));
    }

    @Test
    void whenUserNotFound_return404() {
        assertThrows(ResponseStatusException.class, () -> this.userService.getUserByEmail("not valid email"));
    }

    @Test
    void whenGetTypeOfUser_returnOnlyThatType() {
        List<Rider> riders = this.riderService.getRiders();
        List<Admin> admins = this.adminService.getAdmins();
        List<ServiceOwner> sos = this.serviceOwnerService.getServiceOwners();

        assertThat(riders).hasSize(2).extracting((r) -> r.getUser().getRole()).containsOnly("RIDER");
        assertThat(sos).hasSize(1).extracting((s) -> s.getUser().getRole()).containsOnly("SERVICE_OWNER");
    }


    @Test
    void whenCreatingFromDTO_returnRiderObject() {
        RiderPostDTO riderPostDTO = new RiderPostDTO("vasco", "http://teste.com/a.jpg", new Date(), "teste");
        Rider entity = this.riderService.saveRiderFromDto(riderPostDTO);

        assertThat(entity.getUser().getEmail()).isEqualTo(riderPostDTO.getEmail());
        assertThat(entity.getUser().getPassword()).isEqualTo("teste");
        assertThat(entity.getUser().getRole()).isEqualTo("RIDER");
        assertThat(entity.getDeliveries()).isEmpty();
    }
}
