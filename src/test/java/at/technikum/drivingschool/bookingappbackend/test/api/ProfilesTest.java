package at.technikum.drivingschool.bookingappbackend.test.api;

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
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfilesTest {

    @Autowired
    private MockMvc mockMvc;

    private String getJWTToken() throws Exception {
        String jwtToken = null;
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\"username\": \"Admin\",\n" +
                                "\"password\": \"Admin!23456789\"\n" +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(header().exists("Authorization"))
                .andReturn();

        String jwtHeader = result.getResponse().getHeaders("Authorization").get(0);
        jwtToken = jwtHeader.substring(7);
        return jwtToken;
    }

    @Test
    @Order(31)
    public void getMyProfile() throws Exception {
        String jwtToken = getJWTToken();

        URI uri = new URI("/api/profile");
        mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("email", is("admin@gmail.com")))
                .andExpect(jsonPath("username", is("Admin")));
    }

    @Test
    @Order(32)
    public void getInstructorProfile() throws Exception {
        String jwtToken = getJWTToken();

        URI uri = new URI("/api/profiles/2");
        mockMvc.perform(get(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("email", is("instructor@gmail.com")))
                .andExpect(jsonPath("username", is("Instructor")));
    }

    @Test
    @Order(33)
    public void updateMyProfile() throws Exception {
        String jwtToken = getJWTToken();

        URI uri = new URI("/api/profiles");
        mockMvc.perform(put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
                .content("{" +
                        "\"username\": \"Admin\",\n" +
                        "\"email\": \"admin@gmail.com\",\n" +
                        "\"gender\": \"MALE\",\n" +
                        "\"other\": \"\",\n" +
                        "\"country\": {" +
                              "\"id\": 2,\n" +
                               "\"name\": \"Afghanistan\"\n" +
                        "}\n" +
                        "}")
        ).andExpect(status().isOk());
    }

    @Test
    @Order(34)
    public void deleteStudentProfile() throws Exception {
        String jwtToken = getJWTToken();

        URI uri = new URI("/api/profiles/3");
        mockMvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(status().isOk());
    }
}
