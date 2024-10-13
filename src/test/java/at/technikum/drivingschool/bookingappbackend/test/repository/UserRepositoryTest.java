package at.technikum.drivingschool.bookingappbackend.test.repository;

import at.technikum.drivingschool.bookingappbackend.model.Country;
import at.technikum.drivingschool.bookingappbackend.model.EGender;
import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: finish test class
@DataJpaTest()
@TestPropertySource("classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void injectedComponentsAreNotNull() {
        assertThat(userRepository).isNotNull();
    }

    @Test
    @Order(2)
    void whenSaved_thenFindsByName() {
        userRepository.save(new User(
                "Zuzka",
                "zuzka@home.at",
                "Zuzka!234567",
                EGender.FEMALE,
                "some other text",
                new Country(Long.valueOf(1),"Austria"),
                        "")
                );
        assertThat(userRepository.findByUsername("Zuzka")).isNotNull();
    }

    // TODO: user update test order3
    // TODO: user delete test order4

}
