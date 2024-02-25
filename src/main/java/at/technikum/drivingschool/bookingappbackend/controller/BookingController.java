package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.model.Booking;
import at.technikum.drivingschool.bookingappbackend.model.Event;
import at.technikum.drivingschool.bookingappbackend.model.User;
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
@RequestMapping("/api/booking")
public class BookingController {

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
     * get all bookings in the system
     * only admin should have access
     * @return list of bookings
     */
    @GetMapping("/all")
    // TODO: add method protection as soon as app is working
    // TODO: @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return ResponseEntity.ok().body(bookings);
    }

    /**
     * get all bookings for specific user
     * only admin should have access
     * @param userId
     * @return list of bookings of the user or empty
     */
    @GetMapping("/allForUser/{userId}")
    // TODO: add method protection as soon as app is working
    // TODO: @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllForUser(@PathVariable("userId") Long userId ) {
        List<Booking> bookings = bookingRepository.findAllBookingsForUser(userId);
        return ResponseEntity.ok().body(new BookingListResponse(bookings));
    }

    /**
     * get all bookings of logged in student
     * only student can access this method
     * throws internal server error in case logged in user not found (could be security violation)
     * @return list of bookings of the logged in user or empty
     */
    @GetMapping("/myBookings")
    // TODO: add method protection as soon as app is working
    // TODO: @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getAllMyBookings() {
        User user = getLoggedInUser();
        if (user != null) {
            List<Booking> bookings = bookingRepository.findAllBookingsForUser(user.getId());
            return ResponseEntity.ok().body(new BookingListResponse(bookings));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get bookings for logged in user.");
    }

    /**
     * saves the booking for a specific event for logged in user
     * @param event
     * @return
     */
    @PostMapping("/bookEvent")
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
    @DeleteMapping("/removeMyBooking")
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
