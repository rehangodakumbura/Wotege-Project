package com.example.demo.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("SELECT FUNCTION('DATE', o.orderedAt) as date, SUM(o.totalAmount) as total FROM Order o WHERE o.status NOT IN ('CANCELLED', 'REFUNDED', 'DRAFT') AND o.orderedAt BETWEEN :start AND :end GROUP BY FUNCTION('DATE', o.orderedAt) ORDER BY FUNCTION('DATE', o.orderedAt)")
	List<Object[]> findDailyRevenue(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	@Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status NOT IN ('CANCELLED', 'REFUNDED', 'DRAFT') AND o.orderedAt BETWEEN :start AND :end")
	BigDecimal totalRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	long countByStatusAndOrderedAtBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);
}
