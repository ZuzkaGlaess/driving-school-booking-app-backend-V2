package at.technikum.drivingschool.bookingappbackend.repository;

import at.technikum.drivingschool.bookingappbackend.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CRUD from {@link Country}
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
