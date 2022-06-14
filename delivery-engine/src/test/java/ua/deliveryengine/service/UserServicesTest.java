package ua.deliveryengine.service;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import ua.tqs21.deliveryengine.dto.RiderPostDTO;
import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.models.ServiceOwner;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.models.UserRole;
import ua.tqs21.deliveryengine.repositories.AdminRepository;
import ua.tqs21.deliveryengine.repositories.RiderRepository;
import ua.tqs21.deliveryengine.repositories.ServiceOwnerRepository;
import ua.tqs21.deliveryengine.repositories.UserRepository;
import ua.tqs21.deliveryengine.repositories.UserRoleRepository;
import ua.tqs21.deliveryengine.services.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

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

    @Mock(lenient = true)
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private AdminService adminService;

    @InjectMocks
    private RiderService riderService;

    @InjectMocks
    private ServiceOwnerService serviceOwnerService;

    @InjectMocks
    private UserRoleService userRoleService;
    private List<User> users;

    private User base = new User("base", "psw", null);
    private UserRole ur1 = new UserRole("RIDER");
    private UserRole ur2 = new UserRole("SO");
    private UserRole ur3 = new UserRole("ADMIN");
    private Rider rider1 = new Rider(new User("vasco", "vasco", ur1), null);
    private Rider rider2 = new Rider(new User("email", "psw", ur1), null);
    private Rider rider3 = new Rider();
    private Admin a1 = new Admin(base);
    private ServiceOwner so1 = new ServiceOwner(new User("testing", "psw", ur2), null);

    @BeforeEach
    void setup() {
        List<Rider> riders = new ArrayList<>();
        riders.add(rider1); riders.add(rider2);
        List<Admin> admins = new ArrayList<>();
        admins.add(a1);
        List<ServiceOwner> serviceOwners = new ArrayList<>();
        serviceOwners.add(so1);
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(ur1);
        userRoles.add(ur2);

        List<User> users = new ArrayList<>();
        users.add(base); users.add(rider1.getUser()); users.add(rider2.getUser()); users.add(so1.getUser());

        this.users = users;

        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(userRepository.findById(rider1.getUser().getId())).thenReturn(Optional.of(rider1.getUser()));
        Mockito.when(userRepository.findByEmail("not valid email")).thenReturn(null);
        Mockito.when(userRepository.findByEmail("vasco")).thenReturn(rider1.getUser());
        Mockito.when(userRepository.save(base)).thenReturn(base);
        Mockito.when(userRepository.saveAll(users)).thenReturn(users);

        Mockito.when(riderRepository.findAll()).thenReturn(riders);
        Mockito.when(riderRepository.findById(rider1.getUser().getId())).thenReturn(Optional.of(rider1));
        Mockito.when(riderRepository.save(rider1)).thenReturn(rider1);
        Mockito.when(riderRepository.saveAll(riders)).thenReturn(riders);

        Mockito.when(adminRepository.findAll()).thenReturn(admins);
        Mockito.when(adminRepository.findById(a1.getUser().getId())).thenReturn(Optional.of(a1));
        Mockito.when(adminRepository.save(a1)).thenReturn(a1);
        Mockito.when(adminRepository.saveAll(admins)).thenReturn(admins);

        Mockito.when(serviceOwnerRepository.findAll()).thenReturn(serviceOwners);
        Mockito.when(serviceOwnerRepository.findById(so1.getUser().getId())).thenReturn(Optional.of(so1));
        Mockito.when(serviceOwnerRepository.save(so1)).thenReturn(so1);
        Mockito.when(serviceOwnerRepository.saveAll(serviceOwners)).thenReturn(serviceOwners);

        Mockito.when(userRoleRepository.findAll()).thenReturn(userRoles);
        Mockito.when(userRoleRepository.findById(rider1.getUser().getId())).thenReturn(Optional.of(rider1.getUser().getRole()));
        Mockito.when(userRoleRepository.findByRole(Roles.SERVICE_OWNER.name())).thenReturn(ur1);
        Mockito.when(userRoleRepository.findByRole(Roles.RIDER.name())).thenReturn(ur2);
        Mockito.when(userRoleRepository.findByRole(Roles.ADMIN.name())).thenReturn(ur3);
        Mockito.when(userRoleRepository.save(ur1)).thenReturn(ur1);
        Mockito.when(userRoleRepository.saveAll(userRoles)).thenReturn(userRoles);

        Mockito.when(passwordEncoder.encode("teste")).thenReturn("hash");

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
    void whenUserExists_findExistingId() {
        User vasco = this.userService.getUserById(rider1.getUser().getId());
        assertThat(this.riderService.getRiderById(vasco.getId()).getUser().getPassword()).isEqualTo("vasco");
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findById((rider1.getUser().getId()));
    }

    @Test
    void whenUserNotFoundById_return404() {
        assertThrows(ResponseStatusException.class, () -> this.userService.getUserById(-1));
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
    void whenSaveEntity_returnEntity(){
        assertThat(userService.saveUser(base)).isEqualTo(base);
        assertThat(riderService.saveRider(rider1)).isEqualTo(rider1);
        assertThat(adminService.saveAdmin(a1)).isEqualTo(a1);
        assertThat(serviceOwnerService.saveServiceOwner(so1)).isEqualTo(so1);
        assertThat(userRoleService.saveUserRole(ur1)).isEqualTo(ur1);
    }

    @Test
    void whenSaveListOfEntity_returnListOfEntities(){
        List<Rider> riders = new ArrayList<>();
        riders.add(rider1); riders.add(rider2);
        List<Admin> admins = new ArrayList<>();
        admins.add(a1);
        List<ServiceOwner> serviceOwners = new ArrayList<>();
        serviceOwners.add(so1);
        List<UserRole> userRoles = new ArrayList<>();
        userRoles.add(ur1);
        userRoles.add(ur2);

        List<User> users = new ArrayList<>();
        users.add(base); users.add(rider1.getUser()); users.add(rider2.getUser()); users.add(so1.getUser());

        assertThat(userService.saveUser(users)).isEqualTo(users);
        assertThat(riderService.saveRider(riders)).isEqualTo(riders);
        assertThat(adminService.saveAdmins(admins)).isEqualTo(admins);
        assertThat(serviceOwnerService.saveServiceOwner(serviceOwners)).isEqualTo(serviceOwners);
        assertThat(userRoleService.saveUserRole(userRoles)).isEqualTo(userRoles);
    }
    @Test
    void whenGetTypeOfUser_returnOnlyThatType() {
        List<Rider> riders = this.riderService.getRiders();
        List<Admin> admins = this.adminService.getAdmins();
        List<ServiceOwner> sos = this.serviceOwnerService.getServiceOwners();
        List<UserRole> userRoles = this.userRoleService.getUserRoles();

        assertThat(riders).hasSize(2).extracting((r) -> r.getUser().getRole().getRole()).containsOnly("RIDER");
        assertThat(admins).hasSize(1).extracting((a) -> a.getClass().toString()).containsOnly(Admin.class.toString());
        assertThat(sos).hasSize(1).extracting((s) -> s.getClass().toString()).containsOnly(ServiceOwner.class.toString());
        assertThat(userRoles).hasSize(2);
    }

    @Test
    void whenGetTypeOfUserById_returnUser() {
        Rider riders = this.riderService.getRiderById(rider1.getUser().getId());
        Admin admins = this.adminService.getAdminById(a1.getUser().getId());
        ServiceOwner sos = this.serviceOwnerService.getServiceOwnerById(so1.getUser().getId());
        UserRole userRole = this.userRoleService.getUserRoleById(rider1.getUser().getId());

        assertThat(riders).isEqualTo(rider1);
        assertThat(admins).isEqualTo(a1);
        assertThat(sos).isEqualTo(so1);
        assertThat(userRole).isEqualTo(rider1.getUser().getRole());
    }

    @Test
    void whenGetTypeOfUserByUnknownId_returnNull() {
        Rider riders = this.riderService.getRiderById(-1);
        Admin admins = this.adminService.getAdminById(-1);
        ServiceOwner sos = this.serviceOwnerService.getServiceOwnerById(-1);
        UserRole userRole = this.userRoleService.getUserRoleById(-1);

        assertThat(riders).isNull();
        assertThat(admins).isNull();
        assertThat(sos).isNull();
        assertThat(userRole).isNull();
    }

    @Test
    void whenDeleteTypeOfUser_returnOneLessUser() {
        List<Rider> riders = this.riderService.getRiders();
        List<Admin> admins = this.adminService.getAdmins();
        List<ServiceOwner> sos = this.serviceOwnerService.getServiceOwners();
        List<UserRole> userRoles = this.userRoleService.getUserRoles();

        assertThat(riders).hasSize(2);
        assertThat(admins).hasSize(1);
        assertThat(sos).hasSize(1);
        assertThat(userRoles).hasSize(2);
    }

//    @Test
//    void whenCreatingFromDTORider_returnRiderObject() {
//        RiderPostDTO riderPostDTO = new RiderPostDTO("vasco", "http://teste.com/a.jpg", new Date(), "teste");
//
//        Rider entity = this.riderService.saveRiderFromDto(riderPostDTO);
//
//        Mockito.verify(riderRepository, VerificationModeFactory.times(1)).findById((rider1.getId()));
//
//        assertThat(entity.getUser().getEmail()).isEqualTo(riderPostDTO.getEmail());
//        assertThat(entity.getUser().getPassword()).isEqualTo("hash");
//        assertThat(entity.getUser().getRole().getRole()).isEqualTo("RIDER");
//    }

    @Test
    void whenCreatingFromDTOUser_returnAdminObject() {
        UserDTO userDTO = new UserDTO("vasco", "teste");

        Admin entity = this.adminService.saveAdminFromUser(userDTO);

        assertThat(entity.getUser().getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(entity.getUser().getPassword()).isEqualTo("hash");
        assertThat(entity.getUser().getRole().getRole()).isEqualTo("ADMIN");
    }

    @Test
    void whenCreatingFromDTOService_returnServiceObject() {
        UserDTO userDTO = new UserDTO("vasco", "teste");

        ServiceOwner entity = this.serviceOwnerService.saveAdminFromUser(userDTO);

        assertThat(entity.getUser().getEmail()).isEqualTo(userDTO.getEmail());
        assertThat(entity.getUser().getPassword()).isEqualTo("hash");
        assertThat(entity.getUser().getRole().getRole()).isEqualTo("ADMIN");
    }

    @Test
    void whenDeletingUser_userIsDeleted(){
        this.userService.deleteUser(rider1.getUser().getId());
        this.adminService.deleteAdmin(a1.getId());
        this.riderService.deleteRider(rider1.getId());
        this.serviceOwnerService.deleteServiceOwner(so1.getId());
        this.userRoleService.deleteUserRole(ur1.getId());

        Mockito.verify(userRepository, VerificationModeFactory.times(1)).deleteById((rider1.getUser().getId()));
        Mockito.verify(adminRepository, VerificationModeFactory.times(1)).deleteById((a1.getId()));
        Mockito.verify(riderRepository, VerificationModeFactory.times(1)).deleteById((rider1.getId()));
        Mockito.verify(serviceOwnerRepository, VerificationModeFactory.times(1)).deleteById((so1.getId()));
        Mockito.verify(userRoleRepository, VerificationModeFactory.times(1)).deleteById((ur1.getId()));
    }

    @Test
    void whenUpdatingUser_updateUser() {
        this.userService.updateUser(base);
        this.adminService.updateAdmin(a1);
        this.riderService.updateRider(rider1);
        this.serviceOwnerService.updateServiceOwner(so1);
        this.userRoleService.updateUser(ur1);

        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findById((base.getId()));
        Mockito.verify(adminRepository, VerificationModeFactory.times(1)).findById((a1.getId()));
        Mockito.verify(riderRepository, VerificationModeFactory.times(1)).findById((rider1.getId()));
        Mockito.verify(serviceOwnerRepository, VerificationModeFactory.times(1)).findById((so1.getId()));
        Mockito.verify(userRoleRepository, VerificationModeFactory.times(1)).findById((ur1.getId()));
    }
}
