package at.technikum.drivingschool.bookingappbackend;

import at.technikum.drivingschool.bookingappbackend.controller.AuthController;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegistrationTest {

    @Autowired
    private AuthController controller;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    @Order(2)
    void verifyRegistration() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"username\": \"benjamin\",\n" +
                                "\"email\": \"benjamin@gmail.com\",\n" +
                                "\"role\": [\n" +
                                "\"admin\"\n" +
                                "],\n" +
                                "\"password\": \"Adminadmin#1\",\n" +
                                "\"gender\": \"MALE\",\n" +
                                "\"other\": \"\",\n" +
                                "\"country\": {\n" +
                                "\"id\": 10,\n" +
                                "\"name\": \"Austria\"\n" +
                                "}\n" +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("statusCode" , is(200)));
                .andExpect(jsonPath("message", is("User registered successfully!")));
        assert(userRepository.findByUsername("benjamin").isPresent());
    }

    @Test
    @Order(3)
    void loginTest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\"username\": \"benjamin\",\n" +
                                "\"password\": \"Adminadmin#1\"\n" +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("statusCode" , is(200)));
                .andExpect(jsonPath("email", is("benjamin@gmail.com")))
                .andExpect(cookie().exists("bookingCookie"));
    }
}
