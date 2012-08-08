package com.leonty.etm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;

import com.leonty.calculation.Overtime;
import com.leonty.etm.rule.DayLimits;
import com.leonty.etm.time.WorkDay;
import com.leonty.etm.time.WorkEntry;

public class WorkDayTest {

	private Overtime overtime = new Overtime();
	
	private WorkEntry createWorkEntry(int houtIn, int minuteIn, int hourOur, int minuteOut) {
		
		Calendar start = Calendar.getInstance();
		start.set(2012, Calendar.JANUARY, 1, houtIn, minuteIn, 0);
		start.set(Calendar.MILLISECOND, 0);
		
		Calendar end = Calendar.getInstance();
		end.set(2012, Calendar.JANUARY, 1, hourOur, minuteOut, 0);		
		end.set(Calendar.MILLISECOND, 0);
		
		return new WorkEntry(start.getTime(), end.getTime(), new BigDecimal(11.5d));		
	} 
	
	private DayLimits getDayLimits(Double overtimeLimit, Double extraOvertimeLimit) {
		return new DayLimits(new BigDecimal(overtimeLimit).multiply(new BigDecimal(60*60)).longValueExact(), new BigDecimal(extraOvertimeLimit).multiply(new BigDecimal(60*60)).longValueExact());
	}

	@Test
	public void testSingleEntryNoExtra() {

		WorkEntry workEntry = createWorkEntry(8, 30, 18, 0);
		
		WorkDay day = new WorkDay();
		
		day.addEntry(workEntry);

		day = overtime.calculateDay(day, getDayLimits(7.5, 24d));		
		
		testRegularValues(day);	
	}
	
	
	@Test
	public void testMultipleEntriesNoExtra() {					
	
		WorkDay day = new WorkDay();
		
		day.addEntry(createWorkEntry(8, 30, 10, 0));
		day.addEntry(createWorkEntry(10, 0, 15, 0));
		day.addEntry(createWorkEntry(15, 0, 18, 0));

		day = overtime.calculateDay(day, getDayLimits(7.5, 24d));		
		
		testRegularValues(day);				
	}
	
	@Test
	public void testSingleEntryExtra() {
		WorkEntry workEntry = createWorkEntry(8, 30, 18, 0);
		
		WorkDay day = new WorkDay();
		
		day.addEntry(workEntry);

		day = overtime.calculateDay(day, getDayLimits(7.5, 8.5));		
				
		testExtraValues(day);			
	} 
	
	@Test
	public void testMultipleEntriesExtra() {					
	
		WorkDay day = new WorkDay();
		
		day.addEntry(createWorkEntry(8, 30, 10, 0));
		day.addEntry(createWorkEntry(10, 0, 15, 0));
		day.addEntry(createWorkEntry(15, 0, 18, 0));

		day = overtime.calculateDay(day, getDayLimits(7.5, 8.5));
		
		testExtraValues(day);						
	}	
	
	private void testRegularValues(WorkDay day) {
		
		// total time in seconds for entry
		assertEquals(new Long(34200L), day.getTotalTimeSpan());			
		
		// get normal time span in seconds
		assertEquals(new Long(27000L), day.getRegularTimeSpan());
		
		// get Overtime time span in seconds
		assertEquals(new Long(7200L), day.getRegularOvertimeTimeSpan());	

		// normal payment
		assertTrue(new BigDecimal(86.25).compareTo(day.getRegularPayment()) == 0);

		// overtime payment with overtime set
		assertTrue(new BigDecimal(34.50).compareTo(day.getRegularOvertimePayment(new BigDecimal(1.5))) == 0);
		
		// change the limit
		day.setRegularOvertimeStart(new BigDecimal(7L).multiply(new BigDecimal(60*60)).longValueExact());

		// get normal time span in seconds
		assertEquals(new Long(25200L), day.getRegularTimeSpan());		

		// get Overtime time span in seconds
		assertEquals(new Long(9000L), day.getRegularOvertimeTimeSpan());		

		// normal payment
		assertTrue(new BigDecimal(80.5).compareTo(day.getRegularPayment()) == 0);

		// overtime payment with overtime set
		assertTrue(new BigDecimal(43.125).compareTo(day.getRegularOvertimePayment(new BigDecimal(1.5))) == 0);		
	}
	
	private void testExtraValues(WorkDay day) {
		// total time in seconds for entry
		assertEquals(new Long(34200L), day.getTotalTimeSpan());			
		
		// get normal time span in seconds
		assertEquals(new Long(27000L), day.getRegularTimeSpan());
		
		// get Overtime time span in seconds
		assertEquals(new Long(3600L), day.getRegularOvertimeTimeSpan());	

		// get extra Overtime time span in seconds
		assertEquals(new Long(3600L), day.getExtraOvertimeTimeSpan());		
		
		// normal payment
		assertTrue(new BigDecimal(86.25).compareTo(day.getRegularPayment()) == 0);

		// overtime payment with overtime set
		assertTrue(new BigDecimal(17.25).compareTo(day.getRegularOvertimePayment(new BigDecimal(1.5))) == 0);

		// overtime payment with overtime set
		assertTrue(new BigDecimal(23).compareTo(day.getExtraOvertimePayment(new BigDecimal(2L))) == 0);		

		
		// change the normal overtime limit
		day.setRegularOvertimeStart(new BigDecimal(7L).multiply(new BigDecimal(60*60)).longValueExact());

		// get normal time span in seconds
		assertEquals(new Long(25200L), day.getRegularTimeSpan());		

		// get Overtime time span in seconds
		assertEquals(new Long(5400L), day.getRegularOvertimeTimeSpan());		

		// get extra Overtime time span in seconds
		assertEquals(new Long(3600L), day.getExtraOvertimeTimeSpan());
		
		// normal payment
		assertTrue(new BigDecimal(80.5).compareTo(day.getRegularPayment()) == 0);

		// overtime payment with normal overtime changed
		assertTrue(new BigDecimal(25.875).compareTo(day.getRegularOvertimePayment(new BigDecimal(1.5))) == 0);	
		
		// overtime payment with overtime set
		assertTrue(new BigDecimal(23).compareTo(day.getExtraOvertimePayment(new BigDecimal(2L))) == 0);
		
		// change extra overtime limit

		
		// change the extra overtime limit
		day.setExtraOvertimeStart(new BigDecimal(9L).multiply(new BigDecimal(60*60)).longValueExact());		

		// get normal time span in seconds
		assertEquals(new Long(25200L), day.getRegularTimeSpan());		

		// get Overtime time span in seconds
		assertEquals(new Long(7200L), day.getRegularOvertimeTimeSpan());		

		// get extra Overtime time span in seconds
		assertEquals(new Long(1800L), day.getExtraOvertimeTimeSpan());
		
		// normal payment
		assertTrue(new BigDecimal(80.5).compareTo(day.getRegularPayment()) == 0);

		// overtime payment with normal overtime changed
		assertTrue(new BigDecimal(34.5).compareTo(day.getRegularOvertimePayment(new BigDecimal(1.5))) == 0);	
		
		// overtime payment with overtime set
		assertTrue(new BigDecimal(11.5).compareTo(day.getExtraOvertimePayment(new BigDecimal(2L))) == 0);	
	
	}

}
