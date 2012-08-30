package com.leonty.etm.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import com.leonty.etm.calculation.TimeEntriesParser;
import com.leonty.etm.calculation.TimeEntry;
import com.leonty.etm.time.WorkWeek;

public class TimeEntriesParserTest {

	
	private List<TimeEntry> createTestTimeEntries() {
		
		List<TimeEntry> timeEntries = new ArrayList<TimeEntry>();
		
		for(int i = 1; i < 11; i++) {
			DateTime start = new DateTime(2012, 8, i, 8, 0);
			DateTime end = new DateTime(2012, 8, i, 17, 0);			
			timeEntries.add(new TimeEntryTestImpl(start.toDate(), end.toDate(), new BigDecimal(10d), "job"));
		}
		
		return timeEntries;
	}
	
	@Test
	public void testCounts() {

		DateTime start = new DateTime(2012, 8, 1, 8, 0);
		DateTime end = new DateTime(2012, 8, 10, 17, 0);		
		
		List<WorkWeek> workWeeks = TimeEntriesParser.getWorkWeeks(start.toDate(), end.toDate(), createTestTimeEntries());
		
		Assert.assertTrue("10 days should amount to 2 weeks", 2 == workWeeks.size());
		Assert.assertTrue("First week should have 7 days", 7 == workWeeks.get(0).getDays().size());
		Assert.assertTrue("Second week should have 3 days from 10 days total", 3 == workWeeks.get(1).getDays().size());
	}
	
	@Test
	public void multipleDaysSpan() {

		DateTime start = new DateTime(2012, 8, 1, 8, 0);
		DateTime end = new DateTime(2012, 8, 10, 17, 0);		
		
		List<TimeEntry> timeEntries = new ArrayList<TimeEntry>();
 
		timeEntries.add(new TimeEntryTestImpl(new DateTime(2012, 8, 1, 8, 0).toDate(), new DateTime(2012, 8, 2, 17, 0).toDate(), new BigDecimal(10d), "job"));
			
		List<WorkWeek> workWeeks = TimeEntriesParser.getWorkWeeks(start.toDate(), end.toDate(), timeEntries);

		// start at 8.00 one day, end 17.00 on second
		
		// should have 24 hours in first entry
		// 9 hours on second
		Assert.assertEquals("First day should contain 24 hours", new Double(24d), workWeeks.get(0).getDays().get(0).getTotalTimeSpanInHours());
		Assert.assertEquals("Second day should contain 9 hours", new Double(9d), workWeeks.get(0).getDays().get(1).getTotalTimeSpanInHours());
	}
	
	@Test
	public void testNullTimeOut() {
		DateTime start = new DateTime(2012, 8, 1, 8, 0);
		DateTime end = new DateTime(2012, 8, 2, 17, 0);		
		
		List<TimeEntry> timeEntries = new ArrayList<TimeEntry>();
 
		timeEntries.add(new TimeEntryTestImpl(new DateTime(2012, 8, 1, 8, 0).toDate(), null, new BigDecimal(10d), "job"));
			
		List<WorkWeek> workWeeks = TimeEntriesParser.getWorkWeeks(start.toDate(), end.toDate(), timeEntries);

		Assert.assertEquals("First day should contain 24 hours", new Double(24d), workWeeks.get(0).getDays().get(0).getTotalTimeSpanInHours());
		Assert.assertEquals("Second day should contain 9 hours", new Double(9d), workWeeks.get(0).getDays().get(1).getTotalTimeSpanInHours());		
	}
}
