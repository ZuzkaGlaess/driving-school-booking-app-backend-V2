package at.technikum.drivingschool.bookingappbackend.dto.request;

import at.technikum.drivingschool.bookingappbackend.model.Country;
import at.technikum.drivingschool.bookingappbackend.model.EGender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
 
public class SignupRequest {
    @NotBlank(message = "Username not allowed to be empty")
    @Size(min = 5, max = 20, message = "Username has to be 5 till 20 characters")
    private String username;
 
    @NotBlank(message = "Username not allowed to be empty")
    @Size(max = 50, message = "Email max. 50 characters")
    @Email(message = "Must be a well formated email address")
    private String email;

    @NotBlank(message = "Password is not allowed to be empty")
    @Size(min = 8, max = 30, message = "Password has to have min. 8 and max. 30 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,30}$", message = "Password has to contain at least one uppercase, low case, number and special character: @#$%^&+=")
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
