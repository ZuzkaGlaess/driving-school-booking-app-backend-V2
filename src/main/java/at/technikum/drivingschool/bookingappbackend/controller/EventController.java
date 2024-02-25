package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.model.EEventStatus;
import at.technikum.drivingschool.bookingappbackend.model.EEventType;
import at.technikum.drivingschool.bookingappbackend.model.Event;
import at.technikum.drivingschool.bookingappbackend.dto.request.EventRequest;
import at.technikum.drivingschool.bookingappbackend.dto.response.EventListResponse;
import at.technikum.drivingschool.bookingappbackend.repository.EventRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventRepository eventRepository;
    public EventController() {
    }

    /**
     * Retrieve all saved events
     * @return List<Event>
     */
    @GetMapping("/all")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return ResponseEntity.ok().body(new EventListResponse(events));
    }

    /**
     * Create a new Event in the Database
     * @param event
     * @return
     */
    @PutMapping("/createEvent")
    // TODO: add method protection as soon as app is working
    //@PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> createEvent(@Valid @RequestBody EventRequest event) {
        Event savedEvent = eventRepository.save(
                new Event(
                        event.getTitle(),
                        EEventType.valueOf(event.getEventType()),
                        EEventStatus.valueOf(event.getEventStatus()),
                        event.getPrice(),
                        event.getStartDate()
                        ));
        return ResponseEntity.ok().body("Event saved successfully");
    }

    @DeleteMapping("/deleteEvent")
    // TODO: add method protection as soon as app is working
    //@PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<?> deleteEvent(@RequestParam("eventId") Long eventId) {
        eventRepository.deleteById(eventId);
        return ResponseEntity.ok().body("Event successfully deleted");
    }


}
