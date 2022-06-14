package ua.deliveryengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ua.tqs21.deliveryengine.DeliveryEngineApplication;
import ua.tqs21.deliveryengine.dto.RiderPostDTO;
import ua.tqs21.deliveryengine.models.*;
import ua.tqs21.deliveryengine.repositories.RiderRepository;
import ua.tqs21.deliveryengine.repositories.ServiceOwnerRepository;
import ua.tqs21.deliveryengine.repositories.ServiceRepository;
import ua.tqs21.deliveryengine.repositories.UserRoleRepository;

import java.util.Date;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DeliveryEngineApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ServiceOwnerRepository serviceOwnerRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    private UserRole owner = new UserRole("OWNER");

    private ServiceOwner serviceOwner1 = new ServiceOwner(new User("email", "psw", owner), null);
    private ServiceOwner serviceOwner2 = new ServiceOwner(new User("email", "psw", owner), null);

    private Service service1 = new Service("service1", serviceOwner1, new Address(), new HashSet<Order>());


    private Service service2 = new Service("service1", serviceOwner2, new Address(), new HashSet<Order>());

    @BeforeEach
    void setUp(){
        userRoleRepository.saveAndFlush(owner);
        serviceOwnerRepository.saveAndFlush(serviceOwner1);
        serviceOwnerRepository.saveAndFlush(serviceOwner2);
        serviceRepository.saveAndFlush(service1);
        serviceRepository.saveAndFlush(service2);
    }

    @AfterEach
    void cleanUp(){
        serviceRepository.deleteAll();
    }

    @Test
    void whenGetRider_thenGetAllRiders() throws Exception {
        mvc.perform(get("/shop/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void whenPostRider_riderIsAddedToRepository() throws Exception{
        Service service3 = new Service("service1", serviceOwner2, new Address(), new HashSet<Order>());
        mvc.perform(post("/shop/")
                        .content(asJsonString(service3))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(service3)));
        assertThat(serviceRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    void whenFindRiderById_findRider() throws Exception{
        mvc.perform(get("/courier/"+service1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.name", is(service1.getName())));
    }

    @Test
    void whenUpdateRider_updateRider() throws Exception{
        mvc.perform(put("/courier/")
                        .content(asJsonString(service1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(service1.getId())))
                .andExpect(jsonPath("$.name", is(service1.getName())));
    }

    @Test
    void whenDeleteRiderById_deleteRider() throws Exception{
        int id = service1.getId();
        mvc.perform(delete("/courier/"+id))
                .andDo(print())
                .andExpect(jsonPath("$", is(id)));
        assertThat(serviceRepository.findAll().size()).isEqualTo(1);
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}