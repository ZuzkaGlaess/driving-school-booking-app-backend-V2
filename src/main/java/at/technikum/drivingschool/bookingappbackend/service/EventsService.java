package at.technikum.drivingschool.bookingappbackend.service;

import at.technikum.drivingschool.bookingappbackend.model.EEventStatus;
import at.technikum.drivingschool.bookingappbackend.model.EEventType;
import at.technikum.drivingschool.bookingappbackend.model.Event;
import at.technikum.drivingschool.bookingappbackend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventsService {
    @Autowired
    EventRepository eventRepository;

    /**
     * get all events from database
     *
     * @return
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEvent(Long eventId) {
        return eventRepository.findById(eventId);
    }

    public void createEvent(String title, String eventType, String eventStatus, Long price, Date startDate) {
        eventRepository.save(
                new Event(
                        title,
                        EEventType.valueOf(eventType),
                        EEventStatus.valueOf(eventStatus),
                        price,
                        startDate
                ));
    }

    public void updateEvent(Long id, String title, String eventType, String eventStatus, Long price, Date startDate) {
        eventRepository.save(
                new Event(
                        id,
                        title,
                        EEventType.valueOf(eventType),
                        EEventStatus.valueOf(eventStatus),
                        price,
                        startDate
                ));
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

}
