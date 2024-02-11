package at.technikum.drivingschool.bookingappbackend.repository;

import at.technikum.drivingschool.bookingappbackend.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CRUD for {@link Booking}
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
