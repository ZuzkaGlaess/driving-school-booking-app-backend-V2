package at.technikum.drivingschool.bookingappbackend.service;

import at.technikum.drivingschool.bookingappbackend.model.Booking;
import at.technikum.drivingschool.bookingappbackend.model.Event;
import at.technikum.drivingschool.bookingappbackend.model.User;
import at.technikum.drivingschool.bookingappbackend.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingsService {
    @Autowired
    BookingRepository bookingRepository;

    /**
     * get all bookings from database
     *
     * @return
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * get all bookings for a specific user
     *
     * @param userId
     * @return
     */
    public List<Booking> getAllBookingsForUser(Long userId) {
        return bookingRepository.findAllBookingsForUser(userId);
    }

    /**
     * save an event booking of a user
     *
     * @param user
     * @param event
     * @return
     */
    public Booking bookEvent(User user, Event event) {
        Booking newBooking = new Booking(user, event);
        // saveAndFlush required to instant store record in database
        return bookingRepository.saveAndFlush(newBooking);
    }

    /**
     * Delete an existing booking
     * Return false if booking not found
     *
     * @param bookingId
     * @param user
     * @return
     */
    public boolean deleteBooking(Long bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        // check if user equals booking user
        if (booking != null && booking.getUser().getId().equals(user.getId())) {
            bookingRepository.deleteById(bookingId);
            return true;
        }

        return false;
    }
}
