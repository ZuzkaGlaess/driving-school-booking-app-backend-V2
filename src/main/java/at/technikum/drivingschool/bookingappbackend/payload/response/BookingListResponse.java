package at.technikum.drivingschool.bookingappbackend.payload.response;

import at.technikum.drivingschool.bookingappbackend.models.Booking;

import java.util.List;

public class BookingListResponse {
    private List<Booking> bookings;

    public BookingListResponse(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
