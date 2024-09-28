package at.technikum.drivingschool.bookingappbackend;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
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

                .andExpect(cookie().exists("bookingCookie"))
                .andReturn();

        jwtToken = result.getResponse().getHeaders("Authorization").get(0);
        return jwtToken;
    }

    @Test
    @Order(31)
    public void getMyProfile() throws Exception {
        String jwtToken = getJWTToken();

        URI uri = new URI("/api/profiles");
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
                        "\"email\": \"admin2@gmail.com\",\n" +
                        "\"gender\": \"MALE\",\n" +
                        "\"other\": \"\",\n" +
                        "\"country\": {" +
                              "\"id\": 1,\n" +
                               "\"name\": \"Austria\"\n" +
                        "}\n" +
                        "}")
        ).andExpect(status().isOk());
    }

    @Test
    @Order(34)
    public void deleteStudentProfile() throws Exception {
        String jwtToken = getJWTToken();

        URI uri = new URI("/api/profiles?profileId=3");
        mockMvc.perform(delete(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtToken)
        ).andExpect(status().isOk());
    }
}
