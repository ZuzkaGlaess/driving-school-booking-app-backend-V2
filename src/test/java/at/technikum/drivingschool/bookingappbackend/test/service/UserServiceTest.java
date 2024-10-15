package at.technikum.drivingschool.bookingappbackend.test.service;

import at.technikum.drivingschool.bookingappbackend.model.Country;
import at.technikum.drivingschool.bookingappbackend.model.EGender;
import at.technikum.drivingschool.bookingappbackend.model.ERole;
import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.service.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestPropertySource("classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    void createUser() throws Exception {
        User user = userService.createUser("sebastian", "sebastian@gmail.com", "Sebastian!23456789", EGender.MALE, "", new Country(Long.valueOf("1"), "Austria"), ERole.ROLE_STUDENT, "");
        assertThat(user).isNotNull();
    }

    @Test
    @Order(2)
    void updateUser() throws Exception {

    }

    @Test
    @Order(3)
    void deleteUser() throws Exception {

    }

}
