package com.example.demo.dto;

import java.math.BigDecimal;

public class RevenueChartDataPoint {

	private String label;
	private BigDecimal hotelRevenue;
	private BigDecimal restaurantRevenue;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BigDecimal getHotelRevenue() {
		return hotelRevenue;
	}

	public void setHotelRevenue(BigDecimal hotelRevenue) {
		this.hotelRevenue = hotelRevenue;
	}

	public BigDecimal getRestaurantRevenue() {
		return restaurantRevenue;
	}

	public void setRestaurantRevenue(BigDecimal restaurantRevenue) {
		this.restaurantRevenue = restaurantRevenue;
	}
}
