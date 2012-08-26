package com.leonty.etm.time;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.joda.time.DateTime;


public class WorkDay {

	private ArrayList<WorkEntry> entries = new ArrayList<WorkEntry>();
	
	public void addEntry(WorkEntry workEntry) {
		entries.add(workEntry);
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
			if (workedTime + workEntry.getTotalTimeSpan() > overtimeLimit) {

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
			
			workedTime += workEntry.getTotalTimeSpan().intValue();			
		}
	}
	
	public Long getRegularTimeSpan() {
		long workedTime = 0;
		
		for (WorkEntry workEntry : entries) {
			workedTime += workEntry.getRegularTimeSpan();
		}
		
		return workedTime;
	}
	
	public Long getRegularOvertimeTimeSpan() {
		long workedTime = 0;
		
		for (WorkEntry workEntry : entries) {
			workedTime += workEntry.getRegularOvertimeTimeSpan();
		}
		
		return workedTime;		
	}

	public Long getExtraOvertimeTimeSpan() {
		long workedTime = 0;
		
		for (WorkEntry workEntry : entries) {
			workedTime += workEntry.getExtraOvertimeTimeSpan();
		}
		
		return workedTime;		
	}	
	
	public Long getTotalTimeSpan() {
		long workedTime = 0;
		
		for (WorkEntry workEntry : entries) {
			workedTime += workEntry.getTotalTimeSpan();
		}
		
		return workedTime;
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
