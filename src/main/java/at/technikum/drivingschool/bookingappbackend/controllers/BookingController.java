package at.technikum.drivingschool.bookingappbackend.controllers;

import at.technikum.drivingschool.bookingappbackend.models.Booking;
import at.technikum.drivingschool.bookingappbackend.models.Event;
import at.technikum.drivingschool.bookingappbackend.models.User;
import at.technikum.drivingschool.bookingappbackend.payload.response.BookingListResponse;
import at.technikum.drivingschool.bookingappbackend.repository.BookingRepository;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return getUser(currentPrincipalName);
    }

    private User getUser(String userId) {
        Optional<User> user = userRepository.findByUsername(userId);
        return user.orElse(null);
    }
    @GetMapping("/all")
    // TODO: add method protection as soon as app is working
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity.ok().body(bookings);
    }
    @GetMapping("/allForUser/{userId}")
    // TODO: add method protection as soon as app is working
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllForUser(@PathVariable("userId") Long userId ) {
        List<Booking> bookings = bookingRepository.findAllBookingsForUser(userId);
        return ResponseEntity.ok().body(new BookingListResponse(bookings));
    }

    @GetMapping("/myBookings")
    // TODO: add method protection as soon as app is working
    //@PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getAllMyBookings() {
        User user = getLoggedInUser();
        if (user != null) {
            List<Booking> bookings = bookingRepository.findAllBookingsForUser(user.getId());
            return ResponseEntity.ok().body(new BookingListResponse(bookings));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get bookings for logged in user.");
    }

    @PutMapping("/bookEvent")
    // TODO: add method protection as soon as app is working
    //@PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> bookEvent(@RequestParam("event") Event event) {
        User user = getLoggedInUser();
        if (user != null) {
            Booking newBooking = new Booking(user, event);
            Booking savedBooking = bookingRepository.saveAndFlush(newBooking);
            return ResponseEntity.ok().body(savedBooking);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to book event for logged in user.");
    }

    @DeleteMapping("/removeMyBooking")
    // TODO: add method protection as soon as app is working
    //@PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> removeMyBooking(@RequestParam("bookingId") Long bookingId) {
        User user = getLoggedInUser();
        if (user != null) {
            Booking booking = bookingRepository.findById(bookingId).orElse(null);
            if(booking != null && booking.getUser().getId().equals(user.getId())) {
                bookingRepository.deleteById(bookingId);
                return ResponseEntity.ok().body("Successfully removed booking");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove booking for logged in user.");
    }
}
