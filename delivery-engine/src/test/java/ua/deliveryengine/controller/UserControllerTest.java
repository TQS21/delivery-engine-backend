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
import org.springframework.test.web.servlet.MockMvc;
import ua.tqs21.deliveryengine.DeliveryEngineApplication;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.models.UserRole;
import ua.tqs21.deliveryengine.repositories.AdminRepository;
import ua.tqs21.deliveryengine.repositories.UserRepository;
import ua.tqs21.deliveryengine.repositories.UserRoleRepository;

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
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;


    private UserRole admin = new UserRole("ADMIN");
    private UserRole rider = new UserRole("RIDER");
    private User user1 = new User("base1", "psw", admin);
    private User user2 = new User("base2", "psw", rider);
    @BeforeEach
    void setUp(){
        userRoleRepository.saveAndFlush(admin);
        userRoleRepository.saveAndFlush(rider);
        userRepository.saveAndFlush(user1);
        userRepository.saveAndFlush(user2);
    }

    @AfterEach
    void cleanUp(){
        userRepository.deleteAll();
        userRoleRepository.deleteAll();
    }
    @Test
    void whenGetUser_thenGetAllUsers() throws Exception {
        mvc.perform(get("/user/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void whenPostUser_userIsAddedToRepository() throws Exception{
        mvc.perform(get("/user/")
                .contentType(MediaType.APPLICATION_JSON));
        assertThat(userRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void whenFindUserById_findUser() throws Exception{
        mvc.perform(get("/user/"+user1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(user1.getId())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    void whenUpdateUser_updateUser() throws Exception{
        mvc.perform(put("/user/")
                .content(asJsonString(user1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user1.getId())))
                .andExpect(jsonPath("$.email", is(user1.getEmail())));
    }

    @Test
    void whenDeleteUserById_deleteUser() throws Exception{
        mvc.perform(delete("/user/"+user1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$", is(user1.getId())));
        assertThat(userRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void whenPostUserRole_roleIsAddedToRepository() throws Exception{
        UserRole test = new UserRole("TEST");
        mvc.perform(post("/user/roles")
                .content(asJsonString(test))
                .contentType(MediaType.APPLICATION_JSON));
        assertThat(userRoleRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    void whenFindUserRoles_findRoles() throws Exception{
        mvc.perform(get("/user/roles"))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
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
