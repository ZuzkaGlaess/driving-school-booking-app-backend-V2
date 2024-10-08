package at.technikum.drivingschool.bookingappbackend.repository;

import at.technikum.drivingschool.bookingappbackend.model.Country;
import at.technikum.drivingschool.bookingappbackend.model.EGender;
import at.technikum.drivingschool.bookingappbackend.model.User;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: finish test class
//@DataJpaTest
public class UserRepositoryTest {
    // @Autowired
    private UserRepository userRepository;

    // TODO: finish test1
    //@Test
    void injectedComponentsAreNotNull(){
        assertThat(userRepository).isNotNull();
    }

    // TODO: finish test2
    //@Test
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
}
