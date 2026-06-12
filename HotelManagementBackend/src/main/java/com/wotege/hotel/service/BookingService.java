package com.wotege.hotel.service;

import com.wotege.hotel.dto.booking.BookingRequest;
import com.wotege.hotel.dto.booking.BookingResponse;

import java.util.List;

public interface BookingService {

    List<BookingResponse> getAllBookings();

    BookingResponse getBookingById(Long id);

    BookingResponse createBooking(BookingRequest request);

    BookingResponse updateBooking(Long id, BookingRequest request);

    void deleteBooking(Long id);

    BookingResponse checkIn(Long id);

    BookingResponse checkOut(Long id);
}
