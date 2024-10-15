package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.request.CreateUserRequest;
import at.technikum.drivingschool.bookingappbackend.dto.request.ProfileRequest;
import at.technikum.drivingschool.bookingappbackend.dto.request.UpdateUserRequest;
import at.technikum.drivingschool.bookingappbackend.dto.response.MessageResponse;
import at.technikum.drivingschool.bookingappbackend.dto.response.ProfileResponse;
import at.technikum.drivingschool.bookingappbackend.dto.response.UserListResponse;
import at.technikum.drivingschool.bookingappbackend.exception.UserAlreadyExistsException;
import at.technikum.drivingschool.bookingappbackend.exception.UserNotFoundException;
import at.technikum.drivingschool.bookingappbackend.model.Role;
import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
     * Retrieves all profiles from db, only for Admin
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/profiles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUserProfiles() {
        UserListResponse userList = new UserListResponse();
        userList.initWith(userService.getAllUsers());
        return ResponseEntity.ok().body(userList);
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
        Optional<Role> role = user.getRoles().stream().findFirst();
        return ResponseEntity.ok().body(new ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getCountry(),
                role.map(Role::getName).orElse(null)
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

        return ResponseEntity.ok().body(new MessageResponse("User updated successfully"));
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
    public ResponseEntity<?> updateUserProfile(@PathVariable("userId") Long userId, @Valid @RequestBody UpdateUserRequest profile) {
        User user = userService.getUser(userId).orElseThrow(() -> new UserNotFoundException("Failed to get user from db."));

        user.setUsername(profile.getUsername());
        user.setEmail(profile.getEmail());
        user.setOther(profile.getOther());
        user.setGender(profile.getGender());
        user.setCountry(profile.getCountry());

        userService.updateUser(user, profile.getRole());

        return ResponseEntity.ok().body(new MessageResponse("User updated successfully"));
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

    /**
     * Create a new user, only for Admins
     * *
     * @param createUserRequest
     * @return
     */
    @PostMapping("/profiles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {

        if(userService.userAlreadyExists(createUserRequest.getUsername(), createUserRequest.getEmail())) {
            throw new UserAlreadyExistsException("Username or Email is already in use!");
        }

        userService.createUser(createUserRequest.getUsername(),
                createUserRequest.getEmail(),
                createUserRequest.getPassword(),
                createUserRequest.getGender(),
                createUserRequest.getOther(),
                createUserRequest.getCountry(),
                createUserRequest.getRole(),
                "");

        return ResponseEntity.ok(new MessageResponse("User created successfully!"));
    }

    /**
     * Retrieves the profile of the logged-in user
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or hasRole('STUDENT')")
    public ResponseEntity<?> getMyUserProfile() {
        User user = userService.getLoggedInUser().orElseThrow(() -> new UserNotFoundException("Failed to get logged in user from db."));
        Optional<Role> role = user.getRoles().stream().findFirst();
        return ResponseEntity.ok().body(new ProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getCountry(),
                role.map(Role::getName).orElse(null)
        ));
    }
}
