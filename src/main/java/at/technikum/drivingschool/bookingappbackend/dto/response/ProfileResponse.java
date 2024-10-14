package at.technikum.drivingschool.bookingappbackend.dto.response;

import at.technikum.drivingschool.bookingappbackend.model.Country;
import at.technikum.drivingschool.bookingappbackend.model.EGender;
import at.technikum.drivingschool.bookingappbackend.model.ERole;


public class ProfileResponse {
	private Long id;
	private String username;
	private String email;
	private EGender gender;
	private Country country;
	private ERole role;

	public ProfileResponse(Long id, String username, String email, EGender gender, Country country, ERole role) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.gender = gender;
		this.country = country;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public EGender getGender() {
		return gender;
	}

	public void setGender(EGender gender) {
		this.gender = gender;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public ERole getRole() {
		return role;
	}

	public void setRole(ERole role) {
		this.role = role;
	}
}
