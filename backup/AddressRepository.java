package at.technikum.drivingschool.bookingappbackend.repository;

import at.technikum.drivingschool.bookingappbackend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CRUD from {@link Address}
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
