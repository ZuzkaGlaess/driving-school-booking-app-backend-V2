package at.technikum.drivingschool.bookingappbackend.models;

import jakarta.persistence.*;

/**
 * Booking record of user booking an event
 */
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    private Event event;

    public Booking() {
    }

    public Booking(User user, Event event) {
        this.user = user;
        this.event = event;
    }
}
