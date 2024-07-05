package at.technikum.drivingschool.bookingappbackend.controller;

import at.technikum.drivingschool.bookingappbackend.dto.request.EventRequest;
import at.technikum.drivingschool.bookingappbackend.model.Booking;
import at.technikum.drivingschool.bookingappbackend.model.EEventStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EventsControllerTest {
    @Autowired
    private EventsController eventsController;
    @Test
    public void EventsController_EventrequestTest() {

        //Arrange
        EventRequest request = new EventRequest();
        String title = request.toString();
        Booking event = new Booking();

        //Act


        //Assert


    }

}