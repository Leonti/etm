package com.leonty.etm.rule;

public class WeekLimits {

	private int consecutiveDaysLimit = 7;
	private Long overtimeLimit;
	
	public WeekLimits(Long overtimeLimit, int consecutiveDaysLimit) {

		this.consecutiveDaysLimit = consecutiveDaysLimit;
		this.overtimeLimit = overtimeLimit;
	}

	public int getConsecutiveDaysLimit() {
		return consecutiveDaysLimit;
	}
	
	public void setConsecutiveDaysLimit(int consecutiveDaysLimit) {
		this.consecutiveDaysLimit = consecutiveDaysLimit;
	}
	
	public Long getOvertimeLimit() {
		return overtimeLimit;
	}
	
	public void setOvertimeLimit(Long overtimeLimit) {
		this.overtimeLimit = overtimeLimit;
	}	
}
