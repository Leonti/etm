package com.leonty.etm.time;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.leonty.etm.exception.InvalidDaysCountExceptions;

public class WorkWeek {

	private ArrayList<WorkDay> days = new ArrayList<WorkDay>();
	
	public void addDay(WorkDay day) {
		
		// if week already has all 7 days - throw an exception
		if (days.size() == 7) {
			throw new InvalidDaysCountExceptions("Week can't contain more than 7 days!");
		}
		days.add(day);
	}
	
	public ArrayList<WorkDay> getDays() {
		return days;
	}
	
	public void setOvertimeStart(Long overtimeLimit, Long dayRegularOvertimeLimit) {
		setOvertimeStart(overtimeLimit, true, dayRegularOvertimeLimit);
	}
	
	public void setOvertimeStart(Long overtimeLimit) {
		setOvertimeStart(overtimeLimit, false, 0L);
	}
	
	// extra means - convert regular overtime to extra overtime
	public void setOvertimeStart(Long overtimeLimit, boolean convertToExtra, Long dayRegularOvertimeLimit) {
		long workedTime = 0;

		for (WorkDay workDay: days) {
			
			if (workedTime + workDay.getTotalTimeSpanInSeconds() > overtimeLimit) {
				
					
				// overtime just reached
				if (workedTime < overtimeLimit) {
					
					
					long overtimeDiff = overtimeLimit - workedTime;
					
					
					workDay.setRegularOvertimeStart(overtimeDiff);
					
				// already on overtime - set overtime limit to 0 - day will start from overtime	
				} else {
					workDay.setRegularOvertimeStart(0L);
				}
				
				if (convertToExtra) {
					
					// set extra overtime where regular is supposed to be (we are already on regular overtime)
					workDay.setExtraOvertimeStart(dayRegularOvertimeLimit);
				}
								
			}			
			workedTime += workDay.getTotalTimeSpanInSeconds();	
		}		
	}
	
	/**
	 * Convert overtime say 20 hours (72000) relative to regular time (to include overtimes)
	 * For example if we have 3 days with 7.5 regular time (each) and 2 hours of overtime(each)
	 * it will return 24 hours (86400) = 7,5 + 2 + 7,5 + 2 + 5
	 * 
	 *  This is needed to calculate absolute point in week to set overtime against total time, not regular
	 *  It is used in cases when overtime starts not from some amount of hours but from amount of regular hours
	 *  
	 * @param overtimeLimit
	 * @return
	 */
	public Long getRelativeToRegular(Long overtimeLimit) {
		long regularTime = 0;
		long totalTime = 0;
		
		for (WorkDay workDay: days) {
			
			// just reached overtime assuming that we are counting only regular time
			if (regularTime + workDay.getRegularTimeSpanInSeconds() >= overtimeLimit) {
				
				// Now return new calculated time ready for use with "setOvertimeStart"
				// overtimeLimit - regularTime - time from the beginning of the day until the overtime starts
				// total time - total time from previous days so it's relative to the start of the week				
				return overtimeLimit - regularTime + totalTime;
			}
			
			regularTime += workDay.getRegularTimeSpanInSeconds();
			totalTime += workDay.getTotalTimeSpanInSeconds();
		}
		
		return overtimeLimit;
	}
	
	public int getConsecutiveDays() {
		return days.size();
	}	
	
	public Long getRegularTimeSpanInSeconds() {
		
		long workedTime = 0;
		
		for (WorkDay day : days) {
			workedTime += day.getRegularTimeSpanInSeconds();
		}
		
		return workedTime;		
	}
	
	public Long getRegularOvertimeTimeSpanInSeconds() {
		
		long workedTime = 0;

		for (WorkDay day : days) {
			workedTime += day.getRegularOvertimeTimeSpanInSeconds();
		}

		return workedTime;				
	}
	
	public Long getExtraOvertimeTimeSpanInSeconds() {
		long workedTime = 0;
		
		for (WorkDay day : days) {
			workedTime += day.getExtraOvertimeTimeSpanInSeconds();
		}
		
		return workedTime;			
	}
	
	public Long getTotalTimeSpanInSeconds() {
		return getRegularTimeSpanInSeconds() + getRegularOvertimeTimeSpanInSeconds() + getExtraOvertimeTimeSpanInSeconds();
	}	
	
	public Long getTotalTimeSpanInSeconds(int daysCount) {
		
		long workedTime = 0;
		
		for (int i = 0; i < daysCount; i++) {
			
			workedTime += days.get(i).getTotalTimeSpanInSeconds();
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
		
		for (WorkDay workDay : days) {
			payment = payment.add(workDay.getRegularPayment());
		}

		return payment;
	}
	
	public BigDecimal getRegularOvertimePayment(BigDecimal multiplier) {
		BigDecimal payment = new BigDecimal(0);
		
		for (WorkDay workDay : days) {
			payment = payment.add(workDay.getRegularOvertimePayment(multiplier));
		}

		return payment;
	}
	
	public BigDecimal getExtraOvertimePayment(BigDecimal extraMultiplier) {
		BigDecimal payment = new BigDecimal(0);
		
		for (WorkDay workDay : days) {
			payment = payment.add(workDay.getExtraOvertimePayment(extraMultiplier));
		}

		return payment;
	}
	
	public BigDecimal getTotalPayment(BigDecimal multiplier, BigDecimal extraMultiplier) {
		return getRegularPayment().add(getRegularOvertimePayment(multiplier)).add(getExtraOvertimePayment(extraMultiplier));
	}
}
