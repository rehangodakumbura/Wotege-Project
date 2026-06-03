package com.example.demo.report;

import java.time.LocalDate;
import java.util.List;

import com.example.demo.dto.BookingChannelItem;
import com.example.demo.dto.RevenueBreakdownItem;
import com.example.demo.dto.RevenueReport;
import com.example.demo.dto.TopInventoryItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@GetMapping("/revenue")
	public ResponseEntity<RevenueReport> getRevenueReport(
			@RequestParam LocalDate start,
			@RequestParam LocalDate end) {
		return ResponseEntity.ok(reportService.getRevenueReport(start, end));
	}

	@GetMapping("/revenue-breakdown")
	public ResponseEntity<List<RevenueBreakdownItem>> getRevenueBreakdown() {
		return ResponseEntity.ok(reportService.getRevenueBreakdown());
	}

	@GetMapping("/booking-channels")
	public ResponseEntity<List<BookingChannelItem>> getBookingChannels() {
		return ResponseEntity.ok(reportService.getBookingChannels());
	}

	@GetMapping("/top-inventory")
	public ResponseEntity<List<TopInventoryItem>> getTopInventory() {
		return ResponseEntity.ok(reportService.getTopInventory());
	}
}
