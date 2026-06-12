package com.wotege.hotel.service;

import com.wotege.hotel.dto.booking.BookingRequest;
import com.wotege.hotel.dto.booking.BookingResponse;
import com.wotege.hotel.entity.Booking;
import com.wotege.hotel.entity.Room;
import com.wotege.hotel.exception.BadRequestException;
import com.wotege.hotel.exception.ResourceNotFoundException;
import com.wotege.hotel.repository.BookingRepository;
import com.wotege.hotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        return toResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

        if (request.getCheckOutDate().isBefore(request.getCheckInDate()) ||
                request.getCheckOutDate().isEqual(request.getCheckInDate())) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        Booking booking = Booking.builder()
                .guestName(request.getGuestName())
                .guestEmail(request.getGuestEmail())
                .guestPhone(request.getGuestPhone())
                .room(room)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .numberOfGuests(request.getNumberOfGuests())
                .bookingStatus(Booking.BookingStatus.PENDING)
                .totalAmount(request.getTotalAmount())
                .build();

        booking = bookingRepository.save(booking);

        room.setStatus(Room.RoomStatus.RESERVED);
        roomRepository.save(room);

        return toResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse updateBooking(Long id, BookingRequest request) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

        if (request.getCheckOutDate().isBefore(request.getCheckInDate()) ||
                request.getCheckOutDate().isEqual(request.getCheckInDate())) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }

        booking.setGuestName(request.getGuestName());
        booking.setGuestEmail(request.getGuestEmail());
        booking.setGuestPhone(request.getGuestPhone());
        booking.setRoom(room);
        booking.setCheckInDate(request.getCheckInDate());
        booking.setCheckOutDate(request.getCheckOutDate());
        booking.setNumberOfGuests(request.getNumberOfGuests());
        booking.setTotalAmount(request.getTotalAmount());

        booking = bookingRepository.save(booking);
        return toResponse(booking);
    }

    @Override
    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        Room room = booking.getRoom();
        room.setStatus(Room.RoomStatus.AVAILABLE);
        roomRepository.save(room);

        bookingRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BookingResponse checkIn(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (booking.getBookingStatus() != Booking.BookingStatus.PENDING &&
                booking.getBookingStatus() != Booking.BookingStatus.CONFIRMED) {
            throw new BadRequestException("Booking cannot be checked in from status: " + booking.getBookingStatus());
        }

        booking.setBookingStatus(Booking.BookingStatus.CHECKED_IN);

        Room room = booking.getRoom();
        room.setStatus(Room.RoomStatus.OCCUPIED);
        roomRepository.save(room);

        booking = bookingRepository.save(booking);
        return toResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse checkOut(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (booking.getBookingStatus() != Booking.BookingStatus.CHECKED_IN) {
            throw new BadRequestException("Booking must be checked in before check out");
        }

        booking.setBookingStatus(Booking.BookingStatus.CHECKED_OUT);

        Room room = booking.getRoom();
        room.setStatus(Room.RoomStatus.CLEANING);
        roomRepository.save(room);

        booking = bookingRepository.save(booking);
        return toResponse(booking);
    }

    private BookingResponse toResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setGuestName(booking.getGuestName());
        response.setGuestEmail(booking.getGuestEmail());
        response.setGuestPhone(booking.getGuestPhone());
        response.setRoomId(booking.getRoom().getId());
        response.setRoomNumber(booking.getRoom().getRoomNumber());
        response.setRoomType(booking.getRoom().getRoomType());
        response.setCheckInDate(booking.getCheckInDate());
        response.setCheckOutDate(booking.getCheckOutDate());
        response.setNumberOfGuests(booking.getNumberOfGuests());
        response.setBookingStatus(booking.getBookingStatus().name());
        response.setTotalAmount(booking.getTotalAmount());
        response.setCreatedAt(booking.getCreatedAt());
        return response;
    }
}
