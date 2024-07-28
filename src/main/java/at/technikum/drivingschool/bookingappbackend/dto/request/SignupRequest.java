package at.technikum.drivingschool.bookingappbackend.dto.request;

import java.util.Set;

import at.technikum.drivingschool.bookingappbackend.model.Country;
import at.technikum.drivingschool.bookingappbackend.model.EGender;
import jakarta.validation.constraints.*;
 
public class SignupRequest {
    @NotBlank(message = "Username not allowed to be empty")
    @Size(min = 3, max = 20, message = "Username has to be 3 till 20 characters")
    private String username;
 
    @NotBlank(message = "Username not allowed to be empty")
    @Size(max = 50, message = "Email max. 50 characters")
    @Email(message = "Must be a well formated email address")
    private String email;
    
    private Set<String> role;
    
    @NotBlank(message = "Password is not allowed to be empty")
    @Size(min = 12, max = 30, message = "Password has to have min. 12 and max. 30 characters")
    private String password;

    private EGender gender;
    private String other;

    private Country country;
  
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
    public Set<String> getRole() {
      return this.role;
    }
    
    public void setRole(Set<String> role) {
      this.role = role;
    }

    public EGender getGender() {
        return gender;
    }

    public void setGender(EGender gender) {
        this.gender = gender;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
