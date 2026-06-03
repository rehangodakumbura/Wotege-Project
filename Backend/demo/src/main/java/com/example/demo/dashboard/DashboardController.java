package com.example.demo.dashboard;

import java.util.List;

import com.example.demo.dto.ActivityItem;
import com.example.demo.dto.DashboardSummary;
import com.example.demo.dto.RevenueChartDataPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/summary")
	public ResponseEntity<DashboardSummary> getSummary() {
		return ResponseEntity.ok(dashboardService.getSummary());
	}

	@GetMapping("/revenue-chart")
	public ResponseEntity<List<RevenueChartDataPoint>> getRevenueChart(
			@RequestParam(defaultValue = "weekly") String period) {
		return ResponseEntity.ok(dashboardService.getRevenueChart(period));
	}

	@GetMapping("/recent-activity")
	public ResponseEntity<List<ActivityItem>> getRecentActivity() {
		return ResponseEntity.ok(dashboardService.getRecentActivity());
	}
}
