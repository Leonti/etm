package com.leonty.etm.test;

import java.math.BigDecimal;
import java.util.Date;

import com.leonty.calculation.TimeEntry;

public class TimeEntryTestImpl implements TimeEntry {

	private Date timeIn;
	private Date timeOut;
	private BigDecimal wage;
		
	public TimeEntryTestImpl(Date timeIn, Date timeOut, BigDecimal wage) {
		super();
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.wage = wage;
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

}