package at.technikum.drivingschool.bookingappbackend.test.api;

import at.technikum.drivingschool.bookingappbackend.controller.AuthController;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {
    @Autowired
    private AuthController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(11)
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    @Order(12)
    void verifyLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\"username\": \"Admin\",\n" +
                                "\"password\": \"Admin!23456789\"\n" +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("email", is("admin@gmail.com")))
                .andExpect(header().exists("Authorization"));
    }

    @Test
    @Order(13)
    void loginFailure() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"username\": \"Admin\",\n" +
                        "\"password\": \"Wrongpassword\"\n" +
                        "}")
        )
                .andExpect(status().isUnauthorized())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("status", is("error")))
                .andExpect(jsonPath("message", is("Bad credentials")));

    }

}
