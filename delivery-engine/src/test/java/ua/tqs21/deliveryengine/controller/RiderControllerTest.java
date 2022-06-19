package ua.tqs21.deliveryengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ua.tqs21.deliveryengine.dto.RiderPostDTO;
import ua.tqs21.deliveryengine.enums.Roles;
import ua.tqs21.deliveryengine.models.Order;
import ua.tqs21.deliveryengine.models.Rider;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.repositories.RiderRepository;

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
@ActiveProfiles("test")
public class RiderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RiderRepository riderRepository;


    private User base1 = new User("base1", "psw", Roles.RIDER.name());
    private User base2 = new User("base2", "psw", Roles.RIDER.name());
    private Rider rider1 = new Rider(base1, new HashSet<Order>());
    private Rider rider2 = new Rider(base2, new HashSet<Order>());


    @BeforeEach
    void setUp(){
        riderRepository.saveAndFlush(rider1);
        riderRepository.saveAndFlush(rider2);
    }

    @AfterEach
    void cleanUp(){
        riderRepository.deleteAll();
    }

    @Test
    @WithMockUser
    void whenGetRider_thenGetAllRiders() throws Exception {
        mvc.perform(get("/courier/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @WithMockUser
    void whenPostRider_riderIsAddedToRepository() throws Exception{
        RiderPostDTO rider3 = new RiderPostDTO("base3", "http://teste.com/a.jpg", new Date(), "psw3");
        mvc.perform(post("/courier/")
                .content(asJsonString(rider3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.email", is(rider3.getEmail())));
        assertThat(riderRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @WithMockUser
    void whenFindRiderById_findRider() throws Exception{
        mvc.perform(get("/courier/"+rider1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(rider1.getId())))
                .andExpect(jsonPath("$.user.id", is(rider1.getUser().getId())));
    }

    @Test
    @WithMockUser
    void whenDeleteRiderById_deleteRider() throws Exception{
        int id = rider1.getId();
        mvc.perform(delete("/courier/"+id))
                .andDo(print())
                .andExpect(jsonPath("$", is(id)));
        assertThat(riderRepository.findAll().size()).isEqualTo(1);
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
