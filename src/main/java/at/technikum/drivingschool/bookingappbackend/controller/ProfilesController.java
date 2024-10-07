package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.request.ProfileRequest;
import at.technikum.drivingschool.bookingappbackend.dto.response.ProfileResponse;
import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api")
public class ProfilesController {

    @Autowired
    UserService userService;
    public ProfilesController() {
    }

    /**
     * Retrieves the profile of the loggedin user
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/profiles")
    public ResponseEntity<?> getUserProfile() {
        User user = userService.getLoggedInUser();
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<?> getUserProfile(@PathVariable("userId") String userId) {
        User user = userService.getUser(Long.parseLong(userId));
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
     * Update my own user profile
     * @param profile
     * @return ok or error
     */
    @PutMapping("/profiles")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> updateUserProfile(@Valid @RequestBody ProfileRequest profile) {
        User user = userService.getLoggedInUser();
        if (user != null) {
            user.setUsername(profile.getUsername());
            user.setEmail(profile.getEmail());
            user.setOther(profile.getOther());
            user.setGender(profile.getGender());
            user.setCountry(profile.getCountry());

            userService.updateUser(user);

            return ResponseEntity.ok().body("User updated");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get profile for user.");

    }

    /**
     * Delete an existing user profile
     * @param profileId
     * @return ok or error
     */
    @DeleteMapping("/profiles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserProfile(@Valid @RequestParam Long profileId) {
        User user = userService.getUser(profileId);
        if (user != null) {
            userService.deleteUser(user);

            return ResponseEntity.ok().body("User deleted");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed delete profile for user.");

    }
}
