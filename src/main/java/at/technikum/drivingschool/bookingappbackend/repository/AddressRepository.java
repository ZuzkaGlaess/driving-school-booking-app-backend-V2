package at.technikum.drivingschool.bookingappbackend.repository;

import at.technikum.drivingschool.bookingappbackend.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CRUD from {@link Address}
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
}
