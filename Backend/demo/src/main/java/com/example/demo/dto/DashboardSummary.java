package com.example.demo.dto;

import java.math.BigDecimal;

public class DashboardSummary {

	private BigDecimal totalGrossRevenue;
	private BigDecimal revenueChangePercent;
	private BigDecimal roomOccupancyPercent;
	private BigDecimal occupancyChangePercent;
	private BigDecimal restaurantLoadPercent;
	private int restaurantLoadSeats;
	private int restaurantTotalSeats;
	private BigDecimal restaurantAvgWaitMinutes;
	private int activeGuestsCount;
	private BigDecimal activeGuestChangePercent;
	private BigDecimal dailyRestaurantSales;
	private BigDecimal dailyRestaurantSalesChangePercent;

	public BigDecimal getTotalGrossRevenue() {
		return totalGrossRevenue;
	}

	public void setTotalGrossRevenue(BigDecimal totalGrossRevenue) {
		this.totalGrossRevenue = totalGrossRevenue;
	}

	public BigDecimal getRevenueChangePercent() {
		return revenueChangePercent;
	}

	public void setRevenueChangePercent(BigDecimal revenueChangePercent) {
		this.revenueChangePercent = revenueChangePercent;
	}

	public BigDecimal getRoomOccupancyPercent() {
		return roomOccupancyPercent;
	}

	public void setRoomOccupancyPercent(BigDecimal roomOccupancyPercent) {
		this.roomOccupancyPercent = roomOccupancyPercent;
	}

	public BigDecimal getOccupancyChangePercent() {
		return occupancyChangePercent;
	}

	public void setOccupancyChangePercent(BigDecimal occupancyChangePercent) {
		this.occupancyChangePercent = occupancyChangePercent;
	}

	public BigDecimal getRestaurantLoadPercent() {
		return restaurantLoadPercent;
	}

	public void setRestaurantLoadPercent(BigDecimal restaurantLoadPercent) {
		this.restaurantLoadPercent = restaurantLoadPercent;
	}

	public int getRestaurantLoadSeats() {
		return restaurantLoadSeats;
	}

	public void setRestaurantLoadSeats(int restaurantLoadSeats) {
		this.restaurantLoadSeats = restaurantLoadSeats;
	}

	public int getRestaurantTotalSeats() {
		return restaurantTotalSeats;
	}

	public void setRestaurantTotalSeats(int restaurantTotalSeats) {
		this.restaurantTotalSeats = restaurantTotalSeats;
	}

	public BigDecimal getRestaurantAvgWaitMinutes() {
		return restaurantAvgWaitMinutes;
	}

	public void setRestaurantAvgWaitMinutes(BigDecimal restaurantAvgWaitMinutes) {
		this.restaurantAvgWaitMinutes = restaurantAvgWaitMinutes;
	}

	public int getActiveGuestsCount() {
		return activeGuestsCount;
	}

	public void setActiveGuestsCount(int activeGuestsCount) {
		this.activeGuestsCount = activeGuestsCount;
	}

	public BigDecimal getActiveGuestChangePercent() {
		return activeGuestChangePercent;
	}

	public void setActiveGuestChangePercent(BigDecimal activeGuestChangePercent) {
		this.activeGuestChangePercent = activeGuestChangePercent;
	}

	public BigDecimal getDailyRestaurantSales() {
		return dailyRestaurantSales;
	}

	public void setDailyRestaurantSales(BigDecimal dailyRestaurantSales) {
		this.dailyRestaurantSales = dailyRestaurantSales;
	}

	public BigDecimal getDailyRestaurantSalesChangePercent() {
		return dailyRestaurantSalesChangePercent;
	}

	public void setDailyRestaurantSalesChangePercent(BigDecimal dailyRestaurantSalesChangePercent) {
		this.dailyRestaurantSalesChangePercent = dailyRestaurantSalesChangePercent;
	}
}
