package at.technikum.drivingschool.bookingappbackend.controllers;

import at.technikum.drivingschool.bookingappbackend.models.User;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    UserRepository userRepository;
    public ProfileController() {
    }

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return getUser(currentPrincipalName);
    }

    private User getUser(String userId) {
        Optional<User> user = userRepository.findByUsername(userId);
        return user.orElse(null);
    }

    /**
     * Retrieves the profile of the loggedin user
     */
    @GetMapping("/userProfile")
    public ResponseEntity<?> getUserProfile() {
        User user = getLoggedInUser();
        if (user != null) {
            return ResponseEntity.ok().body(user);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get profile for user.");
    }

    /**
     * Retrieves the profile of any user in the DB
     */
    @GetMapping("/userProfile/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable("userId") String userId) {
        User user = getUser(userId);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get profile for user.");
    }

    /**
     * Update an existing user profile
     * @param user
     * @return
     */
    @PostMapping("/userProfile")
    public ResponseEntity<?> updateUserProfile(@RequestParam("user") User user) {
        userRepository.save(user);
        return ResponseEntity.ok().body("User updated");
    }
}
