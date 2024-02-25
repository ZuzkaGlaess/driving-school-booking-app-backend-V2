package at.technikum.drivingschool.bookingappbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import at.technikum.drivingschool.bookingappbackend.model.User;

/**
 * CRUD for {@link User}
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Search for user by UserName
   */
  Optional<User> findByUsername(String username);

  /**
   * Search for user by UserName or EMail
   */
  Optional<User> findByUsernameOrEmail(String username, String email);

  /**
   * Verify if user already exists
   */
  Boolean existsByUsername(String username);

  /**
   * Verify if email address is already in use
   */
  Boolean existsByEmail(String email);
}
