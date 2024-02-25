package at.technikum.drivingschool.bookingappbackend.repository;

import at.technikum.drivingschool.bookingappbackend.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CRUD for {@link Booking}
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    /**
     * Query returns all bookings of user
     * ?1 is placeholder for first parameter handed to function in this case userId
     * @param userId
     * @return list of bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.user.id == ?1")
    public List<Booking> findAllBookingsForUser(Long userId);
}
