package ua.deliveryengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ua.tqs21.deliveryengine.DeliveryEngineApplication;
import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.ServiceOwner;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.ServiceOwnerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = DeliveryEngineApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class OwnerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ServiceOwnerRepository serviceOwnerRepository;

    private ServiceOwner so1 = new ServiceOwner(new User("base1", "psw", Roles.SERVICE_OWNER.name()), null);
    private ServiceOwner so2 =  new ServiceOwner(new User("base2", "psw", Roles.SERVICE_OWNER.name()), null);


    @BeforeEach
    void setUp(){
        serviceOwnerRepository.saveAndFlush(so1);
        serviceOwnerRepository.saveAndFlush(so2);
    }

    @AfterEach
    void cleanUp(){
        serviceOwnerRepository.deleteAll();
    }
    
    @Test
    @WithMockUser
    void whenGetOwner_thenGetAllOwners() throws Exception {
        mvc.perform(get("/owner/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize((2))));
    }

    @Test
    @WithMockUser
    void whenPostOwner_ownerIsAddedToRepository() throws Exception{
        UserDTO owner = new UserDTO("teste", "teste");
        mvc.perform(post("/owner/")
                .content(asJsonString(owner))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        assertThat(serviceOwnerRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @WithMockUser
    void whenFindOwnerById_findOwner() throws Exception{
        mvc.perform(get("/owner/"+so1.getId()))
                .andExpect(jsonPath("$.id", is(so1.getId())))
                .andExpect(jsonPath("$.user.id", is(so1.getUser().getId())));
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
