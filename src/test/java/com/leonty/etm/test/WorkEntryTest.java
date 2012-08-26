package com.leonty.etm.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;

import com.leonty.etm.time.OvertimeCheckpoints;
import com.leonty.etm.time.WorkEntry;

public class WorkEntryTest {

	
	private WorkEntry createWorkEntry() {
		
		Calendar start = Calendar.getInstance();
		start.set(2012, Calendar.JANUARY, 1, 8, 30, 0);
		start.set(Calendar.MILLISECOND, 0);
		
		Calendar end = Calendar.getInstance();
		end.set(2012, Calendar.JANUARY, 1, 18, 0, 0);
		end.set(Calendar.MILLISECOND, 0);
		
		return new WorkEntry(start.getTime(), end.getTime(), new BigDecimal(11.5d));		
	}
	
	private WorkEntry createRegularOvertimeWorkEntry() {
		Calendar overtime = Calendar.getInstance();
		overtime.set(2012, Calendar.JANUARY, 1, 16, 0, 0);
		overtime.set(Calendar.MILLISECOND, 0);
		
		WorkEntry workEntry = createWorkEntry();
		
		// setting overtime
		workEntry.setOvertimeStart(OvertimeCheckpoints.REGULAR_OVERTIME, overtime.getTime());
		
		return workEntry;
	}

	
	private WorkEntry createExtraOvertimeWorkEntry() {
		Calendar extraOvertime = Calendar.getInstance();
		extraOvertime.set(2012, Calendar.JANUARY, 1, 17, 0, 0);
		extraOvertime.set(Calendar.MILLISECOND, 0);
		
		WorkEntry workEntry = createRegularOvertimeWorkEntry();
		
		// setting overtime
		workEntry.setOvertimeStart(OvertimeCheckpoints.EXTRA_OVERTIME, extraOvertime.getTime());
		
		return workEntry;
	}
	
	@Test
	public void testWithoutOvertime() {		

		WorkEntry workEntry = createWorkEntry();

		// normal payment
		assertTrue(new BigDecimal(109.25).compareTo(workEntry.getRegularPayment()) == 0);
		
		// overtime payment without overtime set 
		assertEquals(new BigDecimal(0), workEntry.getRegularOvertimePayment(new BigDecimal(1.5)));				

		// total time in seconds for entry
		assertEquals(new Long(34200L), workEntry.getTotalTimeSpanInSeconds());
		
		assertEquals(new Double(9.5d), workEntry.getTotalTimeSpanInHours());
	}
	
	@Test
	public void testWithRegularOvertime() {
				
		WorkEntry workEntry = createRegularOvertimeWorkEntry();

		// normal payment with overtime set
		assertTrue(new BigDecimal(86.25).compareTo(workEntry.getRegularPayment()) == 0);
		
		// overtime payment with overtime set
		assertTrue(new BigDecimal(34.50).compareTo(workEntry.getRegularOvertimePayment(new BigDecimal(1.5))) == 0);	
		
		// get normal time span in seconds
		assertEquals(new Long(27000L), workEntry.getRegularTimeSpanInSeconds());
		
		assertEquals(new Double(7.5d), workEntry.getRegularTimeSpanInHours());
		
		// get Overtime time span in seconds
		assertEquals(new Long(7200L), workEntry.getRegularOvertimeTimeSpanInSeconds());
		
		assertEquals(new Double(2d), workEntry.getRegularOvertimeTimeSpanInHours());
		
		// total time in seconds for entry
		assertEquals(new Long(34200L), workEntry.getTotalTimeSpanInSeconds());
		
		assertEquals(new Double(9.5d), workEntry.getTotalTimeSpanInHours());
	}
	
	@Test
	public void testWithExtraOvertime() {		
		
		WorkEntry workEntry = createExtraOvertimeWorkEntry();

		// get normal time span in seconds
		assertEquals(new Long(27000L), workEntry.getRegularTimeSpanInSeconds());
		
		// get Overtime time span in seconds
		assertEquals(new Long(3600L), workEntry.getRegularOvertimeTimeSpanInSeconds());
		
		assertEquals(new Long(3600L), workEntry.getExtraOvertimeTimeSpanInSeconds());		
		
		// normal payment with overtime set
		assertTrue(new BigDecimal(86.25).compareTo(workEntry.getRegularPayment()) == 0);
		
		// overtime payment
		assertTrue(new BigDecimal(17.25).compareTo(workEntry.getRegularOvertimePayment(new BigDecimal(1.5))) == 0);	
		
		// extra overtime payment
		assertTrue(new BigDecimal(23).compareTo(workEntry.getExtraOvertimePayment(new BigDecimal(2))) == 0);	
		
		// total time in seconds for entry
		assertEquals(new Long(34200L), workEntry.getTotalTimeSpanInSeconds());	
	}	

}
