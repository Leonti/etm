package com.leonty.etm.test;

import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.leonty.etm.calculation.TimeEntry;

public class TimeEntryTestImpl implements TimeEntry {

	private DateTime timeIn;
	private DateTime timeOut;
	private BigDecimal wage;
	private String jobTitle;
		
	public TimeEntryTestImpl(DateTime timeIn, DateTime timeOut, BigDecimal wage, String jobTitle) {
		super();
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.wage = wage;
		this.jobTitle = jobTitle;
	}

	@Override
	public DateTime getTimeIn() {
		return timeIn;
	}

	@Override
	public DateTime getTimeOut() {
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
	public void setTimeOut(DateTime timeOut) {
		this.timeOut = timeOut;
	}

	@Override
	public String getJobTitle() {
		return jobTitle;
	}

}
