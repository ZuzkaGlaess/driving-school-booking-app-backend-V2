package at.technikum.drivingschool.bookingappbackend.payload.response;

import at.technikum.drivingschool.bookingappbackend.models.Event;

import java.util.List;

public class EventListResponse {
    private List<Event> events;

    public EventListResponse(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
