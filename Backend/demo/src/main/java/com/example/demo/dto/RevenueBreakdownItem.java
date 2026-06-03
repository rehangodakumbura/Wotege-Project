package com.example.demo.dto;

import java.math.BigDecimal;

public class RevenueBreakdownItem {

	private String name;
	private BigDecimal percentage;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}
}
