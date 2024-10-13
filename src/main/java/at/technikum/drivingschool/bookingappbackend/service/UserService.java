package at.technikum.drivingschool.bookingappbackend.service;

import at.technikum.drivingschool.bookingappbackend.model.*;
import at.technikum.drivingschool.bookingappbackend.repository.RoleRepository;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository
                .findByUsername(username);
    }

    /**
     * get logged in user from security context and search for user in database
     * @return user or null
     */
    public Optional<User> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return getUser(currentPrincipalName);
    }

    /**
     * find user with user id in database or return null
     * @param userId
     * @return user or null
     */
    public Optional<User> getUser(String userId) {
        return userRepository.findByUsername(userId);
    }

    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Verify if a user is already registered
     * Check by User name or email
     * Both are unique
     *
     * @param name
     * @param email
     * @return
     */
    public boolean userAlreadyExists(String name, String email) {
        if (userRepository.existsByUsername(name)) {
            return true;
        } else if(userRepository.existsByEmail(email)) {
            return true;
        }

        return false;
    }

    /**
     * register new User automatically as student
     *
     * @param username
     * @param email
     * @param password
     * @param gender
     * @param other
     * @param country
     * @return
     */
    public User registerUser(String username, String email, String password, EGender gender, String other, Country country, String profilePictureRef) {
        User user = new User(username,
                email,
                encoder.encode(password),
                gender,
                other,
                country,
                profilePictureRef);

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        return userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
