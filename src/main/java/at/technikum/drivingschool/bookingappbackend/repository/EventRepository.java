package at.technikum.drivingschool.bookingappbackend.repository;

import at.technikum.drivingschool.bookingappbackend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CRUD for {@link Event}
 */
public interface EventRepository extends JpaRepository<Event, Long> {

}
