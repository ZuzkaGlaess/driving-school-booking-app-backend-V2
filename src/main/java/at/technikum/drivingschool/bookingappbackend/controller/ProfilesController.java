package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.request.ProfileRequest;
import at.technikum.drivingschool.bookingappbackend.dto.response.MessageResponse;
import at.technikum.drivingschool.bookingappbackend.dto.response.ProfileResponse;
import at.technikum.drivingschool.bookingappbackend.dto.response.UserListResponse;
import at.technikum.drivingschool.bookingappbackend.exception.UserNotFoundException;
import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> getMyUserProfile() {
        User user = userService.getLoggedInUser().orElseThrow(() -> new UserNotFoundException("Failed to get logged in user from db."));

        return ResponseEntity.ok().body(new ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getCountry()
        ));
    }

    /**
     * Retrieves all profiles from db, only for Admin
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/profiles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUserProfiles() {
        return ResponseEntity.ok().body(new UserListResponse(userService.getAllUsers()));
    }

    /**
     * Retrieves the profile of any user in the DB
     *
     * @param userId
     * @return
     */
    @GetMapping("/profiles/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserProfile(@PathVariable("userId") Long userId) {
        User user = userService.getUser(userId).orElseThrow(() -> new UserNotFoundException("Failed to get logged in user from db."));

        return ResponseEntity.ok().body(new ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getCountry()
        ));
    }

    /**
     * Update my own user profile
     *
     * @param profile
     * @return ok or error
     */
    @PutMapping("/profiles")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> updateMyUserProfile(@Valid @RequestBody ProfileRequest profile) {
        User user = userService.getLoggedInUser().orElseThrow(() -> new UserNotFoundException("Failed to get logged in user from db."));

        user.setUsername(profile.getUsername());
        user.setEmail(profile.getEmail());
        user.setOther(profile.getOther());
        user.setGender(profile.getGender());
        user.setCountry(profile.getCountry());

        userService.updateUser(user);

        return ResponseEntity.ok().body("User updated");
    }

    /**
     * Update any user profile, only for Admins
     *
     * @param userId
     * @param profile
     * @return
     */
    @PutMapping("/profiles/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserProfile(@PathVariable("userId") Long userId, @Valid @RequestBody ProfileRequest profile) {
        User user = userService.getUser(userId).orElseThrow(() -> new UserNotFoundException("Failed to get user from db."));

        user.setUsername(profile.getUsername());
        user.setEmail(profile.getEmail());
        user.setOther(profile.getOther());
        user.setGender(profile.getGender());
        user.setCountry(profile.getCountry());

        userService.updateUser(user);

        return ResponseEntity.ok().body("User updated");
    }

    /**
     * Delete an existing user profile, only for Admins
     *
     * @param userId
     * @return ok or error
     */
    @DeleteMapping("/profiles/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserProfile(@PathVariable("userId") Long userId) {
        User user = userService.getUser(userId).orElseThrow(() -> new UserNotFoundException("Failed to get user from db."));

        userService.deleteUser(user);

        return ResponseEntity.ok().body(new MessageResponse("User deleted"));
    }
}
