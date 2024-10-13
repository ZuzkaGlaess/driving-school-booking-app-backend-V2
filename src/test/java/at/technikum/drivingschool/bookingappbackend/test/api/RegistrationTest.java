package at.technikum.drivingschool.bookingappbackend.test.api;

import at.technikum.drivingschool.bookingappbackend.controller.AuthController;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource("classpath:application-test.properties")
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
    @Order(21)
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
        assertThat(userRepository).isNotNull();
    }

    @Test
    @Order(22)
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
                .andExpect(jsonPath("message", is("User registered successfully!")));
        assert(userRepository.findByUsername("benjamin").isPresent());
    }

    @Test
    @Order(23)
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
                .andExpect(jsonPath("email", is("benjamin@gmail.com")))
                .andExpect(header().exists("Authorization"));
    }

    @Test
    @Order(24)
    void loginTestFailure() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\"username\": \"benni\",\n" +
                                "\"password\": \"Adminadmin#1\"\n" +
                                "}")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("error", is("Unauthorized")))
                .andExpect(jsonPath("message", is("Bad credentials")));
    }
}
