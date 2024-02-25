package at.technikum.drivingschool.bookingappbackend.model;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EEventType getEventType() {
        return eventType;
    }

    public void setEventType(EEventType eventType) {
        this.eventType = eventType;
    }

    public EEventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EEventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
