package at.technikum.drivingschool.bookingappbackend.repository;

import at.technikum.drivingschool.bookingappbackend.model.Country;
import at.technikum.drivingschool.bookingappbackend.model.EGender;
import at.technikum.drivingschool.bookingappbackend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(userRepository).isNotNull();
    }

    @Test
    void whenSaved_thenFindsByName() {
        userRepository.save(new User(
                "Zuzka",
                "zuzka@home.at",
                "Zuzka!234567",
                EGender.FEMALE,
                "some other text",
                new Country(Long.valueOf(1),"Austria")));
        assertThat(userRepository.findByUsername("Zuzka")).isNotNull();
    }
}
