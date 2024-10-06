package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.request.LoginRequest;
import at.technikum.drivingschool.bookingappbackend.dto.request.SignupRequest;
import at.technikum.drivingschool.bookingappbackend.dto.response.CountryListResponse;
import at.technikum.drivingschool.bookingappbackend.dto.response.MessageResponse;
import at.technikum.drivingschool.bookingappbackend.dto.response.UserInfoResponse;
import at.technikum.drivingschool.bookingappbackend.model.Country;
import at.technikum.drivingschool.bookingappbackend.security.jwt.JwtUtils;
import at.technikum.drivingschool.bookingappbackend.security.services.UserDetailsImpl;
import at.technikum.drivingschool.bookingappbackend.service.CountryService;
import at.technikum.drivingschool.bookingappbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
  UserService userService;

  @Autowired
  CountryService countryService;

  @Autowired
  JwtUtils jwtUtils;

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

    String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok()
            .headers(httpHeaders -> httpHeaders.add(HttpHeaders.SET_COOKIE, jwtCookie.toString()))
            .headers(httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, jwt))
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

    if(userService.userAlreadyExists(signUpRequest.getUsername(), signUpRequest.getEmail())) {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Username or Email is already in use!"));
    }

    userService.registerUser(signUpRequest.getUsername(),signUpRequest.getEmail(),signUpRequest.getPassword(),signUpRequest.getGender(),signUpRequest.getOther(),signUpRequest.getCountry(),"");

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
