package at.technikum.drivingschool.bookingappbackend.payload.request;

import at.technikum.drivingschool.bookingappbackend.models.EEventStatus;
import at.technikum.drivingschool.bookingappbackend.models.EEventType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class EventRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String eventType;
    @NotBlank
    private String eventStatus;
    @NotBlank
    private Long price;
    @NotBlank
    private Date startDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
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
