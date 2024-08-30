package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.request.ProfileRequest;
import at.technikum.drivingschool.bookingappbackend.dto.response.ProfileResponse;
import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class ProfilesController {

    @Autowired
    UserRepository userRepository;
    public ProfilesController() {
    }

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return getUser(currentPrincipalName);
    }

    private User getUser(String userName) {
        Optional<User> user = userRepository.findByUsername(userName);
        return user.orElse(null);
    }

    private User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }

    /**
     * Retrieves the profile of the loggedin user
     */
    @GetMapping("/profiles")
    public ResponseEntity<?> getUserProfile() {
        User user = getLoggedInUser();
        if (user != null) {
            return ResponseEntity.ok().body(new ProfileResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getGender(),
                    user.getCountry()
            ));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get profile for user.");
    }

    /**
     * Retrieves the profile of any user in the DB
     */
    @GetMapping("/profiles/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable("userId") String userId) {

        if (user != null) {
            User user = getUserById(Long.parseLong(userId));
            return ResponseEntity.ok().body(new ProfileResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getGender(),
                    user.getCountry()
            ));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get profile for user.");
    }

    /**
     * Update an existing user profile
     * @param profile
     * @return ok or error
     */
    @PutMapping("/profiles")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody ProfileRequest profile) {
        User user = getLoggedInUser();
        if (user != null) {
            user.setUsername(profile.getUsername());
            user.setEmail(profile.getEmail());
            user.setOther(profile.getOther());
            user.setGender(profile.getGender());
            user.setCountry(profile.getCountry());

            userRepository.save(user);

            return ResponseEntity.ok().body("User updated");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get profile for user.");

    }
}
