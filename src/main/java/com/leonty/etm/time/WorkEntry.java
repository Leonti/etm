package com.leonty.etm.time;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class WorkEntry {
	private Date timeIn;
	private Date timeOut;
	private OvertimeCheckpoints checkpoints = new OvertimeCheckpoints();
	
	private BigDecimal wage;
	private String jobTitle;
	
	public WorkEntry(Date timeIn, Date timeOut, BigDecimal wage, String jobTitle) {
		super();
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.wage = wage;
		this.jobTitle = jobTitle;
	}
	
	public void setOvertimeStart(int position, Date overtimeStart) {
		checkpoints.setCheckpoint(position, overtimeStart);
	}
		
	public BigDecimal getRegularPayment() {
		
		DateTime start = new DateTime(timeIn);
		DateTime end = new DateTime(getRegularOvertimeStart());
		
		Duration duration = new Duration(start, end);
		
		BigDecimal durationInSeconds = BigDecimal.valueOf(duration.getStandardSeconds());
		
		return this.wage.multiply(
				durationInSeconds).divide(new BigDecimal(60*60), 4, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getRegularOvertimePayment(BigDecimal multiplier) {
		if (getTime(OvertimeCheckpoints.REGULAR_OVERTIME) == null) {
			return new BigDecimal(0);
		} else {
			
			DateTime start = new DateTime(getTime(OvertimeCheckpoints.REGULAR_OVERTIME));
			DateTime end;
			
			if (getTime(OvertimeCheckpoints.EXTRA_OVERTIME) == null) { 

				end = new DateTime(timeOut);
			} else {

				end = new DateTime(getTime(OvertimeCheckpoints.EXTRA_OVERTIME));
			}

			Duration duration = new Duration(start, end);		
			BigDecimal durationInSeconds = BigDecimal.valueOf(duration.getStandardSeconds());			
			
			return calculatePayment(durationInSeconds, multiplier);		
		}
	}
	
	public BigDecimal getExtraOvertimePayment(BigDecimal multiplier) {
		if (getTime(OvertimeCheckpoints.REGULAR_OVERTIME) == null 
				|| getTime(OvertimeCheckpoints.EXTRA_OVERTIME) == null) {
			return new BigDecimal(0);
		} else {

			DateTime start = new DateTime(getTime(OvertimeCheckpoints.EXTRA_OVERTIME));
			DateTime end = new DateTime(timeOut);
			
			Duration duration = new Duration(start, end);		
			BigDecimal durationInSeconds = BigDecimal.valueOf(duration.getStandardSeconds());			
			
			return calculatePayment(durationInSeconds, multiplier);			
		}
	}
	
	// timeSpan in seconds
	private BigDecimal calculatePayment(BigDecimal timeSpan, BigDecimal multiplier) {
		return this.wage.multiply(
				timeSpan).multiply(
					multiplier
				).divide(new BigDecimal(60*60), 4, RoundingMode.HALF_UP);		
	}
	
	public Long getRegularTimeSpanInSeconds() {	
		return new Duration(new DateTime(getTimeIn()), new DateTime(getRegularOvertimeStart()))
			.getStandardSeconds();			
	}
	
	public Long getRegularOvertimeTimeSpanInSeconds() {						
		return new Duration(new DateTime(getRegularOvertimeStart()), new DateTime(getExtraOvertimeStart()))
			.getStandardSeconds();		
	}
	
	public Long getExtraOvertimeTimeSpanInSeconds() {		
		return new Duration(new DateTime(getExtraOvertimeStart()), new DateTime(getTimeOut()))
			.getStandardSeconds();		
	}
	
	public Long getTotalTimeSpanInSeconds() {		
		return new Duration(new DateTime(getTimeIn()), new DateTime(getTimeOut()))
			.getStandardSeconds();		
	}

	public Double getRegularTimeSpanInHours() {
		return getRegularTimeSpanInSeconds()/3600d;			
	}
	
	public Double getRegularOvertimeTimeSpanInHours() {						
		return getRegularOvertimeTimeSpanInSeconds()/3600d;	
	}
	
	public Double getExtraOvertimeTimeSpanInHours() {		
		return getExtraOvertimeTimeSpanInSeconds()/3600d;		
	}
	
	public Double getTotalTimeSpanInHours() {		
		return getTotalTimeSpanInSeconds()/3600d;		
	}	
	
	public Date getTimeIn() {
		return timeIn;
	}
	
	public Date getTimeOut() {
		return timeOut;
	}
	
	public Date getRegularOvertimeStart() {
		if (getTime(OvertimeCheckpoints.REGULAR_OVERTIME) != null) {
			return getTime(OvertimeCheckpoints.REGULAR_OVERTIME);
		} else {
			return timeOut;
		}		
	}
	
	public Date getExtraOvertimeStart() {
		if (getTime(OvertimeCheckpoints.EXTRA_OVERTIME) != null) {
			return getTime(OvertimeCheckpoints.EXTRA_OVERTIME);
		} else {
			return timeOut;
		}
	}
	
	public Date getTime(int position) {
		return checkpoints.getDateCheckpoint(position);
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	
	public BigDecimal getWage() {
		return this.wage;
	}
}
