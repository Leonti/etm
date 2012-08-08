package com.leonty.etm.rule;

public class DayLimits {
	
	private Long regularOvertimeLimit;
	private Long extraOvertimeLimit;
	
	public DayLimits(Long regularOvertimeLimit, Long extraOvertimeLimit) {
		this.regularOvertimeLimit = regularOvertimeLimit;
		this.extraOvertimeLimit = extraOvertimeLimit;
	}
	
	public Long getRegularOvertimeLimit() {
		return regularOvertimeLimit;
	}
	public void setRegularOvertimeLimit(Long regularOvertimeLimit) {
		this.regularOvertimeLimit = regularOvertimeLimit;
	}
	public Long getExtraOvertimeLimit() {
		return extraOvertimeLimit;
	}
	public void setExtraOvertimeLimit(Long extraOvertimeLimit) {
		this.extraOvertimeLimit = extraOvertimeLimit;
	}
	
	
}
