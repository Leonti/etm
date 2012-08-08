package com.leonty.etm.time;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class WorkEntry {
	private Date timeIn;
	private Date timeOut;
	private OvertimeCheckpoints checkpoints = new OvertimeCheckpoints();
	
	private BigDecimal wage;
	
	public WorkEntry(Date timeIn, Date timeOut, BigDecimal wage) {
		super();
		this.timeIn = timeIn;
		this.timeOut = timeOut;
		this.wage = wage;
	}
	
	public void setOvertimeStart(int position, Date overtimeStart) {
		checkpoints.setCheckpoint(position, overtimeStart);
	}
		
	public BigDecimal getRegularPayment() {
		Calendar start = Calendar.getInstance();
		start.setTime(timeIn);

		Calendar end = Calendar.getInstance();
		end.setTime(getRegularOvertimeStart());
		
		BigDecimal timeDiff = BigDecimal.valueOf(CalendarUtils.difference(start, end, CalendarUtils.Unit.SECOND));
		
		return this.wage.multiply(
				timeDiff).divide(new BigDecimal(60*60), 4, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getRegularOvertimePayment(BigDecimal multiplier) {
		if (getTime(OvertimeCheckpoints.REGULAR_OVERTIME) == null) {
			return new BigDecimal(0);
		} else {
			Calendar start = Calendar.getInstance();
			start.setTime(getTime(OvertimeCheckpoints.REGULAR_OVERTIME));
			Calendar end = Calendar.getInstance();
			
			if (getTime(OvertimeCheckpoints.EXTRA_OVERTIME) == null) { 
				end.setTime(timeOut);
			} else {
				end.setTime(getTime(OvertimeCheckpoints.EXTRA_OVERTIME));
			}
			
			BigDecimal timeSpan = BigDecimal.valueOf(CalendarUtils.difference(start, end, CalendarUtils.Unit.SECOND));
			
			return calculatePayment(timeSpan, multiplier);		
		}
	}
	
	public BigDecimal getExtraOvertimePayment(BigDecimal multiplier) {
		if (getTime(OvertimeCheckpoints.REGULAR_OVERTIME) == null 
				|| getTime(OvertimeCheckpoints.EXTRA_OVERTIME) == null) {
			return new BigDecimal(0);
		} else {
			Calendar start = Calendar.getInstance();
			start.setTime(getTime(OvertimeCheckpoints.EXTRA_OVERTIME));
			Calendar end = Calendar.getInstance();
			end.setTime(timeOut);	
			
			BigDecimal timeSpan = BigDecimal.valueOf(CalendarUtils.difference(start, end, CalendarUtils.Unit.SECOND));
			
			return calculatePayment(timeSpan, multiplier);			
		}
	}
	
	// timeSpan in seconds
	private BigDecimal calculatePayment(BigDecimal timeSpan, BigDecimal multiplier) {
		return this.wage.multiply(
				timeSpan).multiply(
					multiplier
				).divide(new BigDecimal(60*60), 4, RoundingMode.HALF_UP);		
	}
	
	public Long getRegularTimeSpan() {
		Calendar start = Calendar.getInstance();
		start.setTime(getTimeIn());		

		Calendar end = Calendar.getInstance();
		end.setTime(getRegularOvertimeStart());
		
		return CalendarUtils.difference(start, end, CalendarUtils.Unit.SECOND);
	}
	
	public Long getRegularOvertimeTimeSpan() {
		Calendar start = Calendar.getInstance();
		start.setTime(getRegularOvertimeStart());		

		Calendar end = Calendar.getInstance();
		end.setTime(getExtraOvertimeStart());
		
		return CalendarUtils.difference(start, end, CalendarUtils.Unit.SECOND);		
	}
	
	public Long getExtraOvertimeTimeSpan() {
		Calendar start = Calendar.getInstance();
		start.setTime(getExtraOvertimeStart());		

		Calendar end = Calendar.getInstance();
		end.setTime(getTimeOut());
		
		return CalendarUtils.difference(start, end, CalendarUtils.Unit.SECOND);	
	}
	
	public Long getTotalTimeSpan() {
		Calendar start = Calendar.getInstance();
		start.setTime(getTimeIn());
		
		Calendar end = Calendar.getInstance();
		end.setTime(getTimeOut());
		
		return CalendarUtils.difference(start, end, CalendarUtils.Unit.SECOND);		
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
}
