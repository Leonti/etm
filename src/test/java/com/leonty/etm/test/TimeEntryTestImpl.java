package com.leonty.etm.test;

import java.math.BigDecimal;
import java.util.Date;

import com.leonty.etm.calculation.TimeEntry;

public class TimeEntryTestImpl implements TimeEntry {

	private Date timeIn;
	private Date timeOut;
	private BigDecimal wage;
	private String jobTitle;
		
	public TimeEntryTestImpl(Date timeIn, Date timeOut, BigDecimal wage, String jobTitle) {
		super();
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.wage = wage;
		this.jobTitle = jobTitle;
	}

	@Override
	public Date getTimeIn() {
		return timeIn;
	}

	@Override
	public Date getTimeOut() {
		return timeOut;
	}

	@Override
	public BigDecimal getWage() {
		return wage;
	}
	
	@Override
	public String toString() {
		return timeIn.toString() + " " + timeOut.toString() + " " + wage;
	}

	@Override
	public void setTimeOut(Date timeOut) {
		this.timeOut = timeOut;
	}

	@Override
	public String getJobTitle() {
		return jobTitle;
	}

}
