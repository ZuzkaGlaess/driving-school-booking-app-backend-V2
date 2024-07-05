package at.technikum.drivingschool.bookingappbackend.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class RoleRepositoryTest {

    @Test
    void userRoleIsNotNull() {
        assertThat(RoleRepository.class).isNotNull();
    }

}