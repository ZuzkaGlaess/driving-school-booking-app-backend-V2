package at.technikum.drivingschool.bookingappbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.technikum.drivingschool.bookingappbackend.models.ERole;
import at.technikum.drivingschool.bookingappbackend.models.Role;

/**
 * CRUD for {@link Role}
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  /**
   * Find a role by name
   */
  Optional<Role> findByName(ERole name);
}
