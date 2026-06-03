package com.example.demo.dashboard;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.audit.AuditLog;
import com.example.demo.audit.AuditLogRepository;
import com.example.demo.customer.CustomerRepository;
import com.example.demo.dto.ActivityItem;
import com.example.demo.dto.DashboardSummary;
import com.example.demo.dto.RevenueChartDataPoint;
import com.example.demo.order.OrderRepository;
import com.example.demo.order.OrderStatus;
import com.example.demo.reservation.ReservationRepository;
import com.example.demo.reservation.ReservationStatus;
import com.example.demo.room.RoomRepository;
import com.example.demo.room.RoomStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboardService {

	private final OrderRepository orderRepository;
	private final ReservationRepository reservationRepository;
	private final RoomRepository roomRepository;
	private final CustomerRepository customerRepository;
	private final AuditLogRepository auditLogRepository;

	public DashboardService(OrderRepository orderRepository, ReservationRepository reservationRepository,
			RoomRepository roomRepository, CustomerRepository customerRepository,
			AuditLogRepository auditLogRepository) {
		this.orderRepository = orderRepository;
		this.reservationRepository = reservationRepository;
		this.roomRepository = roomRepository;
		this.customerRepository = customerRepository;
		this.auditLogRepository = auditLogRepository;
	}

	public DashboardSummary getSummary() {
		LocalDate today = LocalDate.now();
		LocalDateTime startOfToday = today.atStartOfDay();
		LocalDateTime endOfToday = today.plusDays(1).atStartOfDay();
		LocalDateTime startOfYesterday = today.minusDays(1).atStartOfDay();

		LocalDate startOfMonth = today.withDayOfMonth(1);
		LocalDate startOfPrevMonth = startOfMonth.minusMonths(1);

		BigDecimal restaurantRevenue = orderRepository.totalRevenueBetween(startOfToday, endOfToday);
		BigDecimal prevRestaurantRevenue = orderRepository.totalRevenueBetween(startOfYesterday, today.atStartOfDay());

		BigDecimal hotelRevenue = reservationRepository.totalReservationRevenueBetween(today, today.plusDays(1));
		BigDecimal prevHotelRevenue = reservationRepository.totalReservationRevenueBetween(today.minusDays(1), today);

		BigDecimal totalGrossRevenue = hotelRevenue.add(restaurantRevenue);
		BigDecimal prevTotalRevenue = prevHotelRevenue.add(prevRestaurantRevenue);

		BigDecimal revenueChangePercent = calculateChangePercent(totalGrossRevenue, prevTotalRevenue);

		long totalRooms = roomRepository.count();
		long occupiedRooms = roomRepository.countByStatus(RoomStatus.OCCUPIED);
		BigDecimal roomOccupancyPercent = totalRooms > 0
				? BigDecimal.valueOf(occupiedRooms).multiply(BigDecimal.valueOf(100))
						.divide(BigDecimal.valueOf(totalRooms), 2, RoundingMode.HALF_UP)
				: BigDecimal.ZERO;

		long upcomingReservations = reservationRepository
				.countByStatusAndCheckInDateBetween(ReservationStatus.CONFIRMED, today, today.plusDays(1));
		long prevUpcoming = reservationRepository
				.countByStatusAndCheckInDateBetween(ReservationStatus.CONFIRMED, today.minusDays(1), today);
		BigDecimal occupancyChangePercent = calculateChangePercent(
				BigDecimal.valueOf(upcomingReservations), BigDecimal.valueOf(prevUpcoming));

		BigDecimal restaurantLoadPercent = BigDecimal.valueOf(65);
		int restaurantLoadSeats = 42;
		int restaurantTotalSeats = 50;
		BigDecimal restaurantAvgWaitMinutes = BigDecimal.valueOf(12);

		long activeGuestsCount = roomRepository.countByStatus(RoomStatus.OCCUPIED);
		long prevActiveGuests = customerRepository.countByCreatedAtBetween(startOfYesterday, startOfToday);
		BigDecimal activeGuestChangePercent = calculateChangePercent(
				BigDecimal.valueOf(activeGuestsCount), BigDecimal.valueOf(prevActiveGuests));

		BigDecimal dailyRestaurantSales = restaurantRevenue;
		BigDecimal dailyRestaurantSalesChangePercent = calculateChangePercent(restaurantRevenue, prevRestaurantRevenue);

		DashboardSummary summary = new DashboardSummary();
		summary.setTotalGrossRevenue(totalGrossRevenue);
		summary.setRevenueChangePercent(revenueChangePercent);
		summary.setRoomOccupancyPercent(roomOccupancyPercent);
		summary.setOccupancyChangePercent(occupancyChangePercent);
		summary.setRestaurantLoadPercent(restaurantLoadPercent);
		summary.setRestaurantLoadSeats(restaurantLoadSeats);
		summary.setRestaurantTotalSeats(restaurantTotalSeats);
		summary.setRestaurantAvgWaitMinutes(restaurantAvgWaitMinutes);
		summary.setActiveGuestsCount((int) activeGuestsCount);
		summary.setActiveGuestChangePercent(activeGuestChangePercent);
		summary.setDailyRestaurantSales(dailyRestaurantSales);
		summary.setDailyRestaurantSalesChangePercent(dailyRestaurantSalesChangePercent);
		return summary;
	}

	public List<RevenueChartDataPoint> getRevenueChart(String period) {
		LocalDate today = LocalDate.now();
		LocalDateTime start;
		LocalDateTime end = today.plusDays(1).atStartOfDay();
		DateTimeFormatter labelFormatter;

		if ("monthly".equalsIgnoreCase(period)) {
			start = today.minusMonths(12).atStartOfDay();
			labelFormatter = DateTimeFormatter.ofPattern("MMM yy");
		} else {
			start = today.minusDays(6).atStartOfDay();
			labelFormatter = DateTimeFormatter.ofPattern("EEE");
		}

		List<Object[]> restaurantData = orderRepository.findDailyRevenue(start, end);
		List<Object[]> hotelData = reservationRepository.findDailyReservationRevenue(start.toLocalDate(), end.toLocalDate());

		LocalDate cursor = start.toLocalDate();
		List<RevenueChartDataPoint> points = new ArrayList<>();
		while (!cursor.isAfter(today)) {
			LocalDate day = cursor;
			RevenueChartDataPoint point = new RevenueChartDataPoint();
			point.setLabel(day.format(labelFormatter));

			BigDecimal hotelRev = hotelData.stream()
					.filter(row -> row[0] != null && row[0].toString().equals(day.toString()))
					.map(row -> (BigDecimal) row[1])
					.findFirst().orElse(BigDecimal.ZERO);
			point.setHotelRevenue(hotelRev);

			BigDecimal restRev = restaurantData.stream()
					.filter(row -> row[0] != null && row[0].toString().equals(day.toString()))
					.map(row -> (BigDecimal) row[1])
					.findFirst().orElse(BigDecimal.ZERO);
			point.setRestaurantRevenue(restRev);

			points.add(point);

			if ("monthly".equalsIgnoreCase(period)) {
				cursor = cursor.plusMonths(1);
			} else {
				cursor = cursor.plusDays(1);
			}
		}
		return points;
	}

	public List<ActivityItem> getRecentActivity() {
		List<AuditLog> logs = auditLogRepository.findTop10ByOrderByTimestampDesc();
		List<ActivityItem> items = new ArrayList<>();
		for (AuditLog log : logs) {
			ActivityItem item = new ActivityItem();
			item.setTime(log.getTimestamp());
			item.setAction(log.getAction());
			item.setDetail(log.getDescription());
			item.setAmount(log.getDetails() != null ? log.getDetails() : null);
			item.setType(determineActivityType(log));
			item.setInitials(getInitials(log.getUsername()));
			items.add(item);
		}
		return items;
	}

	private String determineActivityType(AuditLog log) {
		String entity = log.getEntityType() != null ? log.getEntityType().toLowerCase() : "";
		if (entity.contains("order") || entity.contains("menu") || entity.contains("restaurant")) {
			return "restaurant";
		} else if (entity.contains("reservation") || entity.contains("room") || entity.contains("booking")) {
			return "hotel";
		} else if (entity.contains("error") || entity.contains("fail")) {
			return "alert";
		}
		return "info";
	}

	private String getInitials(String username) {
		if (username == null || username.isBlank()) return "??";
		String[] parts = username.split(" ");
		if (parts.length >= 2) {
			return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
		}
		return username.substring(0, Math.min(2, username.length())).toUpperCase();
	}

	private BigDecimal calculateChangePercent(BigDecimal current, BigDecimal previous) {
		if (previous.compareTo(BigDecimal.ZERO) == 0) {
			return current.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
		}
		return current.subtract(previous).multiply(BigDecimal.valueOf(100))
				.divide(previous, 2, RoundingMode.HALF_UP);
	}
}
