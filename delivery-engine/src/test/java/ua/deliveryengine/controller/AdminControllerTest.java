package ua.deliveryengine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;
import ua.tqs21.deliveryengine.DeliveryEngineApplication;
import ua.tqs21.deliveryengine.dto.UserDTO;
import ua.tqs21.deliveryengine.models.Admin;
import ua.tqs21.deliveryengine.models.User;
import ua.tqs21.deliveryengine.models.UserRole;
import ua.tqs21.deliveryengine.repositories.AdminRepository;
import ua.tqs21.deliveryengine.repositories.UserRoleRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = DeliveryEngineApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;


    private UserRole admin = new UserRole("ADMIN");
    private Admin admin1 = new Admin(new User("base1", "psw", admin));
    private Admin admin2 = new Admin(new User("base2", "psw", admin));
    @BeforeEach
    void setUp(){
        userRoleRepository.saveAndFlush(admin);
        adminRepository.saveAndFlush(admin1);
        adminRepository.saveAndFlush(admin2);
    }

    @AfterEach
    void cleanUp(){
        adminRepository.deleteAll();
    }
    @Test
    void whenGetAdmin_thenGetAllAdmins() throws Exception {
        mvc.perform(get("/admin/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void whenPostAdmin_adminIsAddedToRepository() throws Exception{
        Admin admin3 = new Admin(new User("base3", "psw", null));
        mvc.perform(post("/admin/")
                .content(asJsonString(admin3))
                .contentType(MediaType.APPLICATION_JSON));
        assertThat(adminRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    void whenFindAdminById_findAdmin() throws Exception{
        mvc.perform(get("/admin/"+admin1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(admin1.getId())))
                .andExpect(jsonPath("$.user.id", is(admin1.getUser().getId())));
    }

    @Test
    void whenUpdateAdmin_updateAdmin() throws Exception{
        mvc.perform(put("/admin/"))
                .andDo(print())
                .andExpect(jsonPath("$", is(admin1)));
    }

    @Test
    void whenDeleteAdminById_deleteAdmin() throws Exception{
        mvc.perform(delete("/admin/"+admin1.getId()))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(admin1.getId())));
        assertThat(adminRepository.findAll().size()).isEqualTo(1);
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
