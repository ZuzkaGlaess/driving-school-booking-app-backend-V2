package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.model.*;
import at.technikum.drivingschool.bookingappbackend.dto.response.BookingListResponse;
import at.technikum.drivingschool.bookingappbackend.repository.BookingRepository;
import at.technikum.drivingschool.bookingappbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class BookingsController {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * get logged in user from security context and search for user in database
     * @return user or null
     */
    private User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return getUser(currentPrincipalName);
    }

    /**
     * find user with user id in database or return null
     * @param userId
     * @return user or null
     */
    private User getUser(String userId) {
        Optional<User> user = userRepository.findByUsername(userId);
        return user.orElse(null);
    }

    /**
     * get all bookings in the system if admin otherwise get all bookings of logged in student
     * only admin or student can access
     * throws internal server error in case logged in user not found (could be security violation)
     * @return list of bookings
     */
    @GetMapping("/bookings")
    // TODO: add method protection as soon as app is working
    // TODO: @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBookings() {
        List<Booking> bookings = null;
        User user = getLoggedInUser();
        if (user != null) {
            if (user.getRoles().contains(ERole.ROLE_ADMIN)) {
               bookings = bookingRepository.findAll();
            } else {
                bookings = bookingRepository.findAllBookingsForUser(user.getId());
            }
            return ResponseEntity.ok().body(new BookingListResponse(bookings));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get bookings for logged in user.");

    }

    /**
     * get all bookings for specific user
     * only admin should have access
     * @param userId
     * @return list of bookings of the user or empty
     */
    @GetMapping("/bookings/{userId}")
    // TODO: add method protection as soon as app is working
    // TODO: @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllForUser(@PathVariable("userId") Long userId ) {
        List<Booking> bookings = bookingRepository.findAllBookingsForUser(userId);
        return ResponseEntity.ok().body(new BookingListResponse(bookings));
    }


    /**
     * saves the booking for a specific event for logged in user
     * @param event
     * @return
     */
    @PostMapping("/bookings")
    // TODO: add method protection as soon as app is working
    // TODO: @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> bookEvent(@RequestParam("event") Event event) {
        User user = getLoggedInUser();
        if (user != null) {
            Booking newBooking = new Booking(user, event);
            // saveAndFlush required to instant store record in database
            Booking savedBooking = bookingRepository.saveAndFlush(newBooking);
            return ResponseEntity.ok().body(savedBooking);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to book event for logged in user.");
    }

    /**
     * student cancels their own booking
     * verifies if the booking belongs to this specific student, if not, not allowed to remove
     * @param bookingId
     * @return
     */
    @DeleteMapping("/bookings")
    // TODO: add method protection as soon as app is working
    // TODO: @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> removeMyBooking(@RequestParam("bookingId") Long bookingId) {
        User user = getLoggedInUser();
        if (user != null) {
            Booking booking = bookingRepository.findById(bookingId).orElse(null);
            // check if user equals booking user
            if(booking != null && booking.getUser().getId().equals(user.getId())) {
                bookingRepository.deleteById(bookingId);
                return ResponseEntity.ok().body("Successfully removed booking");
            }
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove booking for logged in user.");
    }
}
