package com.wotege.hotel.repository;

import com.wotege.hotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByBookingStatus(Booking.BookingStatus status);

    List<Booking> findByGuestEmail(String guestEmail);

    List<Booking> findByGuestNameContainingIgnoreCase(String guestName);

    @Query("SELECT b FROM Booking b WHERE b.checkInDate = :date")
    List<Booking> findByCheckInDate(@Param("date") LocalDate date);

    @Query("SELECT b FROM Booking b WHERE b.checkOutDate = :date")
    List<Booking> findByCheckOutDate(@Param("date") LocalDate date);

    long countByBookingStatus(Booking.BookingStatus status);

    long countByCheckInDate(LocalDate date);

    long countByCheckOutDate(LocalDate date);
}
