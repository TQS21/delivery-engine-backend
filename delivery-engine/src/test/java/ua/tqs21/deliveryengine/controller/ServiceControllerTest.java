package ua.tqs21.deliveryengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ua.tqs21.deliveryengine.DeliveryEngineApplication;
import ua.tqs21.deliveryengine.dto.AddressPostDTO;
import ua.tqs21.deliveryengine.dto.ServicePostDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.*;
import ua.tqs21.deliveryengine.repositories.ServiceOwnerRepository;
import ua.tqs21.deliveryengine.repositories.ServiceRepository;
import ua.tqs21.deliveryengine.services.ServiceService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DeliveryEngineApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ServiceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceService servicesService;

    @Autowired
    private ServiceOwnerRepository serviceOwnerRepository;


    private ServiceOwner serviceOwner1 = new ServiceOwner(new User("email", "psw", Roles.SERVICE_OWNER.name()), null);
    private ServiceOwner serviceOwner2 = new ServiceOwner(new User("email", "psw", Roles.SERVICE_OWNER.name()), null);
    private ServiceOwner dtotest = new ServiceOwner(new User("dto", "test", "role"), null);
    private Service service1 = new Service("service1", serviceOwner1, new Address(), new HashSet<Order>());
    private Service service2 = new Service("service1", serviceOwner2, new Address(), new HashSet<Order>());

    private Service fromDto = new Service("fromdto", null, null, null);

    @BeforeEach
    void setUp() throws URISyntaxException, ParseException, IOException{
        serviceOwnerRepository.saveAndFlush(serviceOwner1);
        serviceOwnerRepository.saveAndFlush(serviceOwner2);
        serviceRepository.saveAndFlush(service1);
        serviceRepository.saveAndFlush(service2);

        when(servicesService.createServiceFromDTO(any())).thenReturn(fromDto);
    }

    @AfterEach
    void cleanUp(){
        serviceRepository.deleteAll();
        serviceOwnerRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void whenGetService_thenGetAllServices() throws Exception {
        mvc.perform(get("/shop/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @WithMockUser
    void whenPostService_serviceIsAddedToRepository() throws Exception{
        ServicePostDTO service3 = new ServicePostDTO("fromdto", new AddressPostDTO("Portugal", "3919","regio u", "rua x"));
        dtotest.setId(5);
        mvc.perform(post("/shop/5")
                        .content(asJsonString(service3))
                        .contentType(MediaType.APPLICATION_JSON));
                //.andExpect(jsonPath("$.name", is(service3.getName())));
    }

    @Test
    @WithMockUser
    void whenFindServiceById_findService() throws Exception{
        mvc.perform(get("/shop/"+service1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.name", is(service1.getName())));
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