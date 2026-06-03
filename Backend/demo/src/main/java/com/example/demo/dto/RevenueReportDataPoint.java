package com.example.demo.dto;

import java.math.BigDecimal;

public class RevenueReportDataPoint {

	private String month;
	private BigDecimal rooms;
	private BigDecimal restaurant;
	private BigDecimal events;

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public BigDecimal getRooms() {
		return rooms;
	}

	public void setRooms(BigDecimal rooms) {
		this.rooms = rooms;
	}

	public BigDecimal getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(BigDecimal restaurant) {
		this.restaurant = restaurant;
	}

	public BigDecimal getEvents() {
		return events;
	}

	public void setEvents(BigDecimal events) {
		this.events = events;
	}
}
