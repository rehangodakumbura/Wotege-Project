package com.example.demo.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByBookingCode(String bookingCode);

    Optional<Reservation> findByBookingId(String bookingId);

    @Query("SELECT MAX(r.bookingId) FROM Reservation r WHERE r.bookingId LIKE 'RES-%'")
    Optional<String> findMaxBookingId();

    List<Reservation> findByStatus(ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE " +
           "LOWER(r.guestName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(r.bookingCode) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(r.bookingId) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Reservation> findBySearch(@Param("search") String search);

    @Query("SELECT r FROM Reservation r WHERE r.checkInDate <= :date AND r.checkOutDate >= :date " +
           "AND r.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Reservation> findActiveOnDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.checkInDate = :today")
    List<Reservation> findCheckInsToday(@Param("today") LocalDate today);

    @Query("SELECT r FROM Reservation r WHERE r.checkOutDate = :today")
    List<Reservation> findCheckOutsToday(@Param("today") LocalDate today);

    @Query("SELECT r FROM Reservation r WHERE r.bookingType = 'EVENT' AND r.checkInDate >= :today " +
           "AND r.status NOT IN ('COMPLETED', 'CANCELLED')")
    List<Reservation> findUpcomingEvents(@Param("today") LocalDate today);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND r.status NOT IN ('COMPLETED', 'CANCELLED') " +
           "AND r.checkInDate < :checkOut AND r.checkOutDate > :checkIn")
    List<Reservation> findConflicting(@Param("roomId") Long roomId,
                                      @Param("checkIn") LocalDate checkIn,
                                      @Param("checkOut") LocalDate checkOut);

    List<Reservation> findAllByOrderByCreatedAtDesc();
}
