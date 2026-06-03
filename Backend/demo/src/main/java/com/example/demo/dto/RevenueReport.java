package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public class RevenueReport {

	private BigDecimal grossRevenueYtd;
	private BigDecimal netProfitMargin;
	private BigDecimal avgOccupancy;
	private BigDecimal revPAR;
	private List<RevenueReportDataPoint> monthlyData;

	public BigDecimal getGrossRevenueYtd() {
		return grossRevenueYtd;
	}

	public void setGrossRevenueYtd(BigDecimal grossRevenueYtd) {
		this.grossRevenueYtd = grossRevenueYtd;
	}

	public BigDecimal getNetProfitMargin() {
		return netProfitMargin;
	}

	public void setNetProfitMargin(BigDecimal netProfitMargin) {
		this.netProfitMargin = netProfitMargin;
	}

	public BigDecimal getAvgOccupancy() {
		return avgOccupancy;
	}

	public void setAvgOccupancy(BigDecimal avgOccupancy) {
		this.avgOccupancy = avgOccupancy;
	}

	public BigDecimal getRevPAR() {
		return revPAR;
	}

	public void setRevPAR(BigDecimal revPAR) {
		this.revPAR = revPAR;
	}

	public List<RevenueReportDataPoint> getMonthlyData() {
		return monthlyData;
	}

	public void setMonthlyData(List<RevenueReportDataPoint> monthlyData) {
		this.monthlyData = monthlyData;
	}
}
