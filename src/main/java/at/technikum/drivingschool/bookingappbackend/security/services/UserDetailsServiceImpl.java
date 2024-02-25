package at.technikum.drivingschool.bookingappbackend.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;

/**
 * Implemenation for {@link UserDetailsService} of Spring Security
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  /**
   * Read user from MariaDB over {@link UserRepository}
   * or throw Exception if not found
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    /*
      Search for user by username or email
      Hand over username as email attribute to run query
    User user = userRepository.findByUsernameOrEmail(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    */

    return UserDetailsImpl.build(user);
  }

}
