package at.technikum.drivingschool.bookingappbackend.models;

import jakarta.persistence.*;

import java.util.Date;

/**
 * Event that can get booked by a user
 */
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EEventType eventType;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EEventStatus eventStatus;
    private Long price;
    private Date startDate;

    public Event() {
    }

    public Event(String title, EEventType eventType, EEventStatus eventStatus, Long price, Date startDate) {
        this.title = title;
        this.eventType = eventType;
        this.eventStatus = eventStatus;
        this.price = price;
        this.startDate = startDate;
    }
}
