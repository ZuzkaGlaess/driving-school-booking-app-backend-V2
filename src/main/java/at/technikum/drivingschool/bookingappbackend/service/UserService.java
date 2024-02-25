package at.technikum.drivingschool.bookingappbackend.service;

import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository
                .findByUsername(username);
    }
}
