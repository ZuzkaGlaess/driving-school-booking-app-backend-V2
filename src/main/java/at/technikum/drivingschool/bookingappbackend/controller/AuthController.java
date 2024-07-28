package at.technikum.drivingschool.bookingappbackend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import at.technikum.drivingschool.bookingappbackend.dto.response.CountryListResponse;
import at.technikum.drivingschool.bookingappbackend.model.Country;
import at.technikum.drivingschool.bookingappbackend.model.ERole;
import at.technikum.drivingschool.bookingappbackend.model.Role;
import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.dto.request.LoginRequest;
import at.technikum.drivingschool.bookingappbackend.dto.request.SignupRequest;
import at.technikum.drivingschool.bookingappbackend.dto.response.MessageResponse;
import at.technikum.drivingschool.bookingappbackend.dto.response.UserInfoResponse;
import at.technikum.drivingschool.bookingappbackend.repository.RoleRepository;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import at.technikum.drivingschool.bookingappbackend.security.jwt.JwtUtils;
import at.technikum.drivingschool.bookingappbackend.security.services.UserDetailsImpl;
import at.technikum.drivingschool.bookingappbackend.service.CountryService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handling login, registration and logout
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  CountryService countryService;

  /**
   * Login method
   * called from the login form
   * extracts from request username and password
   * generates JWT token on successful login
   * @return JWT token as cookie in header and user details in body
   */
  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(),
                                   roles));
  }

  /**
   * User registration method
   * verifies in database if user already exits
   * username and email address must be unique in the system
   * returns error message in case it already exists
   * if ok it creates the user in database
   */
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()),
                         signUpRequest.getGender(),
                         signUpRequest.getOther(),
                         signUpRequest.getCountry());

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    // optional part in case user is allowed to select role, default role student
    // admin assigns the role
    // TODO: update method to only create users with student role
    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "inst":
          Role modRole = roleRepository.findByName(ERole.ROLE_INSTRUCTOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_STUDENT)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @GetMapping("/countries")
  public ResponseEntity<?> getAllCountries() {
    List<Country> countries = countryService.getAllCountries();
    return ResponseEntity.ok(new CountryListResponse(countries));
  }

  /*
   * Logout method
   * sends empty JWT token to the browser
   * browser will update cookie in cache, as cookie doesn't contain any valid data = session terminated
   * additionally logout message is sent
   */
/*  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been logged out!"));
  }*/
}
