package com.leonty.etm.time;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class WorkWeeks implements Collection<WorkWeek> {

	List<WorkWeek> workWeeks;
	
	public WorkWeeks(List<WorkWeek> workWeeks) {
		this.workWeeks = new ArrayList<WorkWeek>(workWeeks);
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
	
	public BigDecimal getTotalPayment(BigDecimal multiplier, BigDecimal extraMultiplier) {
		BigDecimal payment = new BigDecimal(0);
		
		for (WorkWeek week : workWeeks) {
			payment = payment.add(week.getTotalPayment(multiplier, extraMultiplier));
		}

		return payment;
	}	

	@Override
	public boolean add(WorkWeek arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends WorkWeek> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}		
}
