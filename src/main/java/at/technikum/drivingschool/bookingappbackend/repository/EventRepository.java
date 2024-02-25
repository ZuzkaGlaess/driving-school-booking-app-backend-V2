package at.technikum.drivingschool.bookingappbackend.repository;

import at.technikum.drivingschool.bookingappbackend.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CRUD for {@link Event}
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
