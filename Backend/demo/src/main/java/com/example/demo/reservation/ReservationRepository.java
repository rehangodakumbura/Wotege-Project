package com.example.demo.reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	@Query("SELECT COALESCE(SUM(r.amount), 0) FROM Reservation r WHERE r.status NOT IN ('CANCELLED') AND r.checkInDate BETWEEN :start AND :end")
	BigDecimal totalReservationRevenueBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

	@Query("SELECT FUNCTION('DATE', r.checkInDate) as date, COALESCE(SUM(r.amount), 0) as total FROM Reservation r WHERE r.status NOT IN ('CANCELLED') AND r.checkInDate BETWEEN :start AND :end GROUP BY FUNCTION('DATE', r.checkInDate) ORDER BY FUNCTION('DATE', r.checkInDate)")
	List<Object[]> findDailyReservationRevenue(@Param("start") LocalDate start, @Param("end") LocalDate end);

	long countByStatusAndCheckInDateBetween(ReservationStatus status, LocalDate start, LocalDate end);
}