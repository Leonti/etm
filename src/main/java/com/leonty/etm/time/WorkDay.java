package com.leonty.etm.time;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;


public class WorkDay {

	private ArrayList<WorkEntry> entries = new ArrayList<WorkEntry>();
	private Date dayStart;
	private Date dayEnd;
	
	public WorkDay(Date dayStart, Date dayEnd) {
		this.dayStart = dayStart;
		this.dayEnd = dayEnd;
	}

	public void addEntry(WorkEntry workEntry) {
		entries.add(workEntry);
	}
	
	public void addEntries(List<WorkEntry> workEntries) {
		entries.addAll(workEntries);
	}
	
	public List<WorkEntry> getEntries() {
		return entries;
	}

	public Date getDayStart() {
		return dayStart;
	}

	public Date getDayEnd() {
		return dayEnd;
	}	
	
	public void setRegularOvertimeStart(Long overtimeLimit) {
		setOvertimeStart(OvertimeCheckpoints.REGULAR_OVERTIME, overtimeLimit);
	}
	
	public void setExtraOvertimeStart(Long overtimeLimit) {
		setOvertimeStart(OvertimeCheckpoints.EXTRA_OVERTIME, overtimeLimit);
	}
	
	public void setOvertimeStart(int position, Long overtimeLimit) {
		Integer workedTime = 0;
		
		for (WorkEntry workEntry: entries) {
			
			// when total time for the day exceeds the limit 
			if (workedTime + workEntry.getTotalTimeSpanInSeconds() > overtimeLimit) {

				DateTime start = new DateTime(workEntry.getTimeIn());
				
				// overtime just reached
				if (workedTime < overtimeLimit) {
				
					DateTime overtimeStart = new DateTime(start);
					
					Integer overtimeDiff = overtimeLimit.intValue() - workedTime;
					
					overtimeStart = overtimeStart.plusSeconds(overtimeDiff);
					
					// setting start of the overtime
					workEntry.setOvertimeStart(position, overtimeStart.toDate());
					
				// already on overtime - set overtime time to the time start	
				} else {
					workEntry.setOvertimeStart(position, workEntry.getTimeIn());
				}
			}
			
			workedTime += workEntry.getTotalTimeSpanInSeconds().intValue();			
		}
	}
	
	public Long getRegularTimeSpanInSeconds() {
		long workedTime = 0;
		
		for (WorkEntry workEntry : entries) {
			workedTime += workEntry.getRegularTimeSpanInSeconds();
		}
		
		return workedTime;
	}
	
	public Long getRegularOvertimeTimeSpanInSeconds() {
		long workedTime = 0;
		
		for (WorkEntry workEntry : entries) {
			workedTime += workEntry.getRegularOvertimeTimeSpanInSeconds();
		}
		
		return workedTime;		
	}

	public Long getExtraOvertimeTimeSpanInSeconds() {
		long workedTime = 0;
		
		for (WorkEntry workEntry : entries) {
			workedTime += workEntry.getExtraOvertimeTimeSpanInSeconds();
		}
		
		return workedTime;		
	}	
	
	public Long getTotalTimeSpanInSeconds() {
		long workedTime = 0;
		
		for (WorkEntry workEntry : entries) {
			workedTime += workEntry.getTotalTimeSpanInSeconds();
		}
		
		return workedTime;
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
	
	public BigDecimal getRegularPayment() {
		BigDecimal payment = new BigDecimal(0);
		
		for (WorkEntry workEntry : entries) {
			payment = payment.add(workEntry.getRegularPayment());
		}

		return payment;
	}
	
	public BigDecimal getRegularOvertimePayment(BigDecimal multiplier) {
		BigDecimal payment = new BigDecimal(0);
		
		for (WorkEntry workEntry : entries) {
			payment = payment.add(workEntry.getRegularOvertimePayment(multiplier));
		}
		
		return payment;
	}

	public BigDecimal getExtraOvertimePayment(BigDecimal extraMultiplier) {
		BigDecimal payment = new BigDecimal(0);
		
		for (WorkEntry workEntry : entries) {
			payment = payment.add(workEntry.getExtraOvertimePayment(extraMultiplier));
		}
		
		return payment;
	}
	
	public BigDecimal getTotalPayment(BigDecimal multiplier, BigDecimal extraMultiplier) {
		return getRegularPayment().add(getRegularOvertimePayment(multiplier).add(getExtraOvertimePayment(extraMultiplier)));
	}	
}
