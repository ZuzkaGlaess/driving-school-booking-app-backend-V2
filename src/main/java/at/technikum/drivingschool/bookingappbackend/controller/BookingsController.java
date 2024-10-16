package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.response.BookingListResponse;
import at.technikum.drivingschool.bookingappbackend.dto.response.MessageResponse;
import at.technikum.drivingschool.bookingappbackend.exception.BookingNotFoundException;
import at.technikum.drivingschool.bookingappbackend.exception.EventNotFoundException;
import at.technikum.drivingschool.bookingappbackend.exception.UserNotFoundException;
import at.technikum.drivingschool.bookingappbackend.model.*;
import at.technikum.drivingschool.bookingappbackend.service.BookingsService;
import at.technikum.drivingschool.bookingappbackend.service.EventsService;
import at.technikum.drivingschool.bookingappbackend.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api")
public class BookingsController {

    @Autowired
    BookingsService bookingsService;

    @Autowired
    EventsService eventsService;

    @Autowired
    UserService userService;

    private boolean isAdmin(User user) {
        Set<Role> roles = user.getRoles();
        if (!roles.isEmpty()) {
            for (Role role : roles) {
                if (role.getName().equals(ERole.ROLE_ADMIN)) return true;
            }
        }

        return false;
    }

    /**
     * get all bookings in the system if admin otherwise get all bookings of logged in student
     * only admin or student can access
     * throws internal server error in case logged in user not found (could be security violation)
     *
     * @return list of bookings
     */
    @GetMapping("/bookings")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<?> getAllBookings() {
        List<Booking> bookings = null;
        User user = userService.getLoggedInUser().orElseThrow(() -> new UserNotFoundException("Failed to get logged in user from db."));

        if (isAdmin(user)) {
            bookings = bookingsService.getAllBookings();
        } else {
            bookings = bookingsService.getAllBookingsForUser(user.getId());
        }
        return ResponseEntity.ok().body(new BookingListResponse(bookings));
    }

    /**
     * get all bookings for specific user
     * only admin should have access
     *
     * @param userId
     * @return list of bookings of the user or empty
     */
    @GetMapping("/bookings/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllForUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(new BookingListResponse(bookingsService.getAllBookingsForUser(userId)));
    }

    /**
     * saves the booking for a specific event for logged in user
     *
     * @param eventId
     * @return
     */
    @PostMapping("/bookings")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> bookEvent(@RequestParam("eventId") Long eventId) {
        User user = userService.getLoggedInUser().orElseThrow(() -> new UserNotFoundException("Failed to get logged in user from db."));

        Optional<Event> event = eventsService.getEvent(eventId);
        if (event.isPresent()) {
            return ResponseEntity.ok().body(bookingsService.bookEvent(user, event.get()));
        }
        throw new EventNotFoundException("Failed to get event from DB");
    }

    /**
     * student cancels their own booking
     * verifies if the booking belongs to this specific student, if not, not allowed to remove
     *
     * @param bookingId
     * @return
     */
    @DeleteMapping("/bookings/{bookingId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> removeMyBooking(@PathVariable("bookingId") Long bookingId) {
        User user = userService.getLoggedInUser().orElseThrow(() -> new UserNotFoundException("Failed to get logged in user from db."));

        boolean result = bookingsService.deleteBooking(bookingId, user);
        if (result) {
            return ResponseEntity.ok().body(new MessageResponse("Successfully removed booking"));
        } else {
            throw new BookingNotFoundException("Could not find booking with id " + bookingId);
        }
    }
}
