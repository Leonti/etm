package com.leonty.etm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;

import com.leonty.calculation.Overtime;
import com.leonty.etm.rule.DayLimits;
import com.leonty.etm.rule.WeekLimits;
import com.leonty.etm.time.WorkDay;
import com.leonty.etm.time.WorkEntry;
import com.leonty.etm.time.WorkWeek;

public class WorkWeekTest {

	private Overtime overtime = new Overtime();

	private DayLimits getDayLimits(Double overtimeLimit, Double extraOvertimeLimit) {
		return new DayLimits(new BigDecimal(overtimeLimit).multiply(new BigDecimal(60*60)).longValueExact(), new BigDecimal(extraOvertimeLimit).multiply(new BigDecimal(60*60)).longValueExact());
	}
	
	private WeekLimits getWeekLimits(Double overtimeLimit, int consecutiveDays) {
		return new WeekLimits(new BigDecimal(overtimeLimit).multiply(new BigDecimal(60*60)).longValueExact(), consecutiveDays);
	}	
	
	public WorkDay createDay(int dayOfMonth) {
		
		Calendar start = Calendar.getInstance();
		start.set(2012, Calendar.JANUARY, dayOfMonth, 8, 30, 0);			
		start.set(Calendar.MILLISECOND, 0);
		
		Calendar end = Calendar.getInstance();
		end.set(2012, Calendar.JANUARY, dayOfMonth, 10, 0, 0);		
		end.set(Calendar.MILLISECOND, 0);
		
		WorkEntry firstWorkEntry = new WorkEntry(start.getTime(), end.getTime(), new BigDecimal(11.5d));
		
		start.set(2012, Calendar.JANUARY, dayOfMonth, 10, 0, 0);
		start.set(Calendar.MILLISECOND, 0);
		end.set(2012, Calendar.JANUARY, dayOfMonth, 15, 0, 0);
		end.set(Calendar.MILLISECOND, 0);
		
		WorkEntry secondWorkEntry = new WorkEntry(start.getTime(), end.getTime(), new BigDecimal(11.5d));

		start.set(2012, Calendar.JANUARY, dayOfMonth, 15, 0, 0);
		start.set(Calendar.MILLISECOND, 0);
		end.set(2012, Calendar.JANUARY, dayOfMonth, 18, 0, 0);
		end.set(Calendar.MILLISECOND, 0);
		
		WorkEntry thirdWorkEntry = new WorkEntry(start.getTime(), end.getTime(), new BigDecimal(11.5d));		
	
		WorkDay day = new WorkDay();
		
		day.addEntry(firstWorkEntry);
		day.addEntry(secondWorkEntry);
		day.addEntry(thirdWorkEntry);

		// apply day rules
		day = overtime.calculateDay(day, getDayLimits(7.5, 24d));		
		
		return day;
	}
	
	public WorkWeek createAndPopulateWeek() {
		
	//	WorkWeek week = new WorkWeek(new BigDecimal(20L));
		
		WorkWeek week = new WorkWeek();
		
		// create week with 5 working days
		for (int i = 1; i < 6; i++) {
			week.addDay(createDay(i));			
		}
		
		return week;
	}
	
	@Test
	public void testSimple() {
		WorkWeek week = createAndPopulateWeek();
		
		week = overtime.calculateWeek(week, 
				getWeekLimits(20d, 7), 
				getDayLimits(7.5, 24d));
		
		assertEquals(new Long(171000), week.getTotalTimeSpanInSeconds());
		
		assertEquals(new Long(86400L), week.getRelativeToRegular(72000L));
		
		// should be 20 hours/ 72000 seconds
		assertEquals(new Long(72000L), week.getRegularTimeSpanInSeconds());
		
		assertEquals(new Long(99000L), week.getRegularOvertimeTimeSpanInSeconds());
		
		assertTrue(new BigDecimal(230L).compareTo(week.getRegularPayment()) == 0);
		
		assertTrue(new BigDecimal(474.375).compareTo(week.getRegularOvertimePayment(new BigDecimal(1.5))) == 0);
		
		assertTrue(new BigDecimal(0L).compareTo(week.getExtraOvertimePayment(new BigDecimal(2L))) == 0);
	}
	
	@Test
	public void testConsecutive() {
		WorkWeek week = createAndPopulateWeek();			

		week = overtime.calculateWeek(week, 
				getWeekLimits(20d, 2), 
				getDayLimits(7.5, 24d));		
		
		assertEquals(new Long(171000), week.getTotalTimeSpanInSeconds());
		
		// should be first 2 consecutive days 7.5*2 = 15 hours/ 54000 seconds - after that regular overtime starts
		assertEquals(new Long(54000L), week.getRegularTimeSpanInSeconds());
		
		// 2 2 7.5 7.5 7.5
		assertEquals(new Long(95400L), week.getRegularOvertimeTimeSpanInSeconds());
		
		// 0 0 2 2 2
		assertEquals(new Long(21600L), week.getExtraOvertimeTimeSpanInSeconds());
		
		assertTrue(new BigDecimal(172.5).compareTo(week.getRegularPayment()) == 0);
		
		assertTrue(new BigDecimal(457.125).compareTo(week.getRegularOvertimePayment(new BigDecimal(1.5))) == 0);
		
		assertTrue(new BigDecimal(138).compareTo(week.getExtraOvertimePayment(new BigDecimal(2L))) == 0);
	}	

}
