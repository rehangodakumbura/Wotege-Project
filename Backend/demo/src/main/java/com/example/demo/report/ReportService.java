package com.example.demo.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.BookingChannelItem;
import com.example.demo.dto.RevenueBreakdownItem;
import com.example.demo.dto.RevenueReport;
import com.example.demo.dto.RevenueReportDataPoint;
import com.example.demo.dto.TopInventoryItem;
import com.example.demo.inventory.InventoryItem;
import com.example.demo.inventory.InventoryItemRepository;
import com.example.demo.inventory.InventoryMovement;
import com.example.demo.inventory.InventoryMovementRepository;
import com.example.demo.inventory.StockStatus;
import com.example.demo.order.OrderRepository;
import com.example.demo.reservation.ReservationRepository;
import com.example.demo.room.RoomRepository;
import com.example.demo.room.RoomStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportService {

	private final OrderRepository orderRepository;
	private final ReservationRepository reservationRepository;
	private final RoomRepository roomRepository;
	private final InventoryItemRepository inventoryItemRepository;
	private final InventoryMovementRepository inventoryMovementRepository;

	public ReportService(OrderRepository orderRepository, ReservationRepository reservationRepository,
			RoomRepository roomRepository, InventoryItemRepository inventoryItemRepository,
			InventoryMovementRepository inventoryMovementRepository) {
		this.orderRepository = orderRepository;
		this.reservationRepository = reservationRepository;
		this.roomRepository = roomRepository;
		this.inventoryItemRepository = inventoryItemRepository;
		this.inventoryMovementRepository = inventoryMovementRepository;
	}

	public RevenueReport getRevenueReport(LocalDate startDate, LocalDate endDate) {
		LocalDateTime start = startDate.atStartOfDay();
		LocalDateTime end = endDate.plusDays(1).atStartOfDay();

		BigDecimal restaurantRevenue = orderRepository.totalRevenueBetween(start, end);
		BigDecimal hotelRevenue = reservationRepository.totalReservationRevenueBetween(startDate, endDate);
		BigDecimal grossRevenueYtd = hotelRevenue.add(restaurantRevenue);

		BigDecimal estimatedCosts = grossRevenueYtd.multiply(BigDecimal.valueOf(0.55));
		BigDecimal netProfit = grossRevenueYtd.subtract(estimatedCosts);
		BigDecimal netProfitMargin = grossRevenueYtd.compareTo(BigDecimal.ZERO) > 0
				? netProfit.multiply(BigDecimal.valueOf(100)).divide(grossRevenueYtd, 2, RoundingMode.HALF_UP)
				: BigDecimal.ZERO;

		long totalRooms = roomRepository.count();
		long occupiedRooms = roomRepository.countByStatus(RoomStatus.OCCUPIED);
		BigDecimal avgOccupancy = totalRooms > 0
				? BigDecimal.valueOf(occupiedRooms).multiply(BigDecimal.valueOf(100))
						.divide(BigDecimal.valueOf(totalRooms), 2, RoundingMode.HALF_UP)
				: BigDecimal.ZERO;

		BigDecimal revPAR = totalRooms > 0
				? hotelRevenue.divide(BigDecimal.valueOf(totalRooms), 2, RoundingMode.HALF_UP)
				: BigDecimal.ZERO;

		List<RevenueReportDataPoint> monthlyData = buildMonthlyData(startDate, endDate);

		RevenueReport report = new RevenueReport();
		report.setGrossRevenueYtd(grossRevenueYtd);
		report.setNetProfitMargin(netProfitMargin);
		report.setAvgOccupancy(avgOccupancy);
		report.setRevPAR(revPAR);
		report.setMonthlyData(monthlyData);
		return report;
	}

	public List<RevenueBreakdownItem> getRevenueBreakdown() {
		List<RevenueBreakdownItem> items = new ArrayList<>();
		items.add(createBreakdownItem("Rooms", 42));
		items.add(createBreakdownItem("Restaurant", 35));
		items.add(createBreakdownItem("Events & Banquets", 15));
		items.add(createBreakdownItem("Packages & Extras", 8));
		return items;
	}

	public List<BookingChannelItem> getBookingChannels() {
		List<BookingChannelItem> items = new ArrayList<>();
		items.add(createChannelItem("Direct Booking", 35, new BigDecimal("245000")));
		items.add(createChannelItem("Booking.com", 25, new BigDecimal("175000")));
		items.add(createChannelItem("Expedia", 20, new BigDecimal("140000")));
		items.add(createChannelItem("Travel Agency", 12, new BigDecimal("84000")));
		items.add(createChannelItem("Corporate", 8, new BigDecimal("56000")));
		return items;
	}

	public List<TopInventoryItem> getTopInventory() {
		List<InventoryItem> items = inventoryItemRepository.findAll();
		List<TopInventoryItem> result = new ArrayList<>();
		for (InventoryItem item : items) {
			TopInventoryItem top = new TopInventoryItem();
			top.setItemName(item.getName());
			top.setCategory(item.getCategory());

			List<InventoryMovement> movements = inventoryMovementRepository.findAll();
			long inCount = movements.stream()
					.filter(m -> m.getItem().getId().equals(item.getId()) && m.getType() == com.example.demo.inventory.MovementType.IN)
					.count();
			long outCount = movements.stream()
					.filter(m -> m.getItem().getId().equals(item.getId()) && m.getType() == com.example.demo.inventory.MovementType.OUT)
					.count();
			long net = inCount - outCount;
			top.setTrend(net >= 0 ? "+" + (net * 7) + "%" : (net * 7) + "%");

			top.setStatus(item.getStatus() == StockStatus.REORDER ? "reorder" : "optimal");
			result.add(top);
		}
		return result;
	}

	private List<RevenueReportDataPoint> buildMonthlyData(LocalDate start, LocalDate end) {
		List<RevenueReportDataPoint> data = new ArrayList<>();
		LocalDate cursor = start.withDayOfMonth(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yy");
		while (!cursor.isAfter(end)) {
			LocalDate monthStart = cursor;
			LocalDate monthEnd = cursor.plusMonths(1).minusDays(1);
			LocalDateTime dtStart = monthStart.atStartOfDay();
			LocalDateTime dtEnd = monthEnd.plusDays(1).atStartOfDay();

			BigDecimal rooms = reservationRepository.totalReservationRevenueBetween(monthStart, monthEnd);
			BigDecimal restaurant = orderRepository.totalRevenueBetween(dtStart, dtEnd);

			RevenueReportDataPoint point = new RevenueReportDataPoint();
			point.setMonth(monthStart.format(formatter));
			point.setRooms(rooms);
			point.setRestaurant(restaurant);
			point.setEvents(BigDecimal.ZERO);
			data.add(point);

			cursor = cursor.plusMonths(1);
		}
		return data;
	}

	private RevenueBreakdownItem createBreakdownItem(String name, int percent) {
		RevenueBreakdownItem item = new RevenueBreakdownItem();
		item.setName(name);
		item.setPercentage(BigDecimal.valueOf(percent));
		return item;
	}

	private BookingChannelItem createChannelItem(String channel, int percent, BigDecimal revenue) {
		BookingChannelItem item = new BookingChannelItem();
		item.setChannel(channel);
		item.setPercentage(BigDecimal.valueOf(percent));
		item.setRevenue(revenue);
		return item;
	}
}
