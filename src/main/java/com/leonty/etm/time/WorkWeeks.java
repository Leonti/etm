package com.leonty.etm.time;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkWeeks implements Iterable<WorkWeek> {

	List<WorkWeek> workWeeks = new ArrayList<WorkWeek>();
	
	public WorkWeeks(List<WorkWeek> workWeeks) {
		workWeeks = new ArrayList<WorkWeek>(workWeeks);
	}
	
	@Override
	public Iterator<WorkWeek> iterator() {	
		return workWeeks.iterator();
	}
	
	public int size() {
		return workWeeks.size();
	}

	public WorkWeek get(int index) {
		return workWeeks.get(index);
	}
	
	public Long getRegularTimeSpanInSeconds() {
		
		long workedTime = 0;
		
		for (WorkWeek week : workWeeks) {
			workedTime += week.getRegularTimeSpanInSeconds();
		}
		
		return workedTime;		
	}
	
	public Long getRegularOvertimeTimeSpanInSeconds() {
		
		long workedTime = 0;

		for (WorkWeek week : workWeeks) {
			workedTime += week.getRegularOvertimeTimeSpanInSeconds();
		}

		return workedTime;				
	}
	
	public Long getExtraOvertimeTimeSpanInSeconds() {
		long workedTime = 0;
		
		for (WorkWeek week : workWeeks) {
			workedTime += week.getExtraOvertimeTimeSpanInSeconds();
		}
		
		return workedTime;			
	}
	
	public Long getTotalTimeSpanInSeconds() {
		return getRegularTimeSpanInSeconds() + getRegularOvertimeTimeSpanInSeconds() + getExtraOvertimeTimeSpanInSeconds();
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
		
		for (WorkWeek week : workWeeks) {
			payment = payment.add(week.getRegularPayment());
		}

		return payment;
	}
	
	public BigDecimal getRegularOvertimePayment(BigDecimal multiplier) {
		BigDecimal payment = new BigDecimal(0);
		
		for (WorkWeek week : workWeeks) {
			payment = payment.add(week.getRegularOvertimePayment(multiplier));
		}

		return payment;
	}
	
	public BigDecimal getExtraOvertimePayment(BigDecimal extraMultiplier) {
		BigDecimal payment = new BigDecimal(0);
		
		for (WorkWeek week : workWeeks) {
			payment = payment.add(week.getExtraOvertimePayment(extraMultiplier));
		}

		return payment;
	}		
}
