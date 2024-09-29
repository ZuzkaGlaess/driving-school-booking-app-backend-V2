package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.response.BookingListResponse;
import at.technikum.drivingschool.bookingappbackend.model.Booking;
import at.technikum.drivingschool.bookingappbackend.model.ERole;
import at.technikum.drivingschool.bookingappbackend.model.Event;
import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.service.BookingsService;
import at.technikum.drivingschool.bookingappbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class BookingsController {

    @Autowired
    BookingsService bookingsService;

    @Autowired
    UserService userService;

    /**
     * get all bookings in the system if admin otherwise get all bookings of logged in student
     * only admin or student can access
     * throws internal server error in case logged in user not found (could be security violation)
     * @return list of bookings
     */
    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> getAllBookings() {
        List<Booking> bookings = null;
        User user = userService.getLoggedInUser();
        if (user != null) {
            if (user.getRoles().contains(ERole.ROLE_ADMIN)) {
               bookings = bookingsService.getAllBookings();
            } else {
                bookings = bookingsService.getAllBookingsForUser(user.getId());
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllForUser(@PathVariable("userId") Long userId ) {
        return ResponseEntity.ok().body(new BookingListResponse(bookingsService.getAllBookingsForUser(userId)));
    }


    /**
     * saves the booking for a specific event for logged in user
     * @param event
     * @return
     */
    @PostMapping("/bookings")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> bookEvent(@RequestParam("event") Event event) {
        User user = userService.getLoggedInUser();
        if (user != null) {
            return ResponseEntity.ok().body(bookingsService.bookEvent(user, event));
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
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> removeMyBooking(@RequestParam("bookingId") Long bookingId) {
        User user = userService.getLoggedInUser();
        if (user != null) {
            boolean result = bookingsService.deleteBooking(bookingId, user);
            if(result) {
                return ResponseEntity.ok().body("Successfully removed booking");
            }

            return ResponseEntity.ok().body("Booking not found");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to remove booking for logged in user.");
    }
}
