package com.leonty.etm.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OvertimeCheckpoints {

	public static int REGULAR_OVERTIME = 0;
	public static int EXTRA_OVERTIME = 1;
	
	private ArrayList<Long> checkpoints = new ArrayList<Long>();
	private ArrayList<Date> dateCheckpoints = new ArrayList<Date>();
	
	public void setCheckpoint(int position, Long overtimeStart) {
		
		checkSize(position, checkpoints);
		
		checkpoints.set(position, overtimeStart);
	}
	
	public void setCheckpoint(int position, Date overtimeStart) {
		
		checkSize(position, dateCheckpoints);
		
		dateCheckpoints.set(position, overtimeStart);
	}	
	
	public Long getCheckpoint(int position) {
		if (dateCheckpoints.size() <= position) {
			return null;
		}
		
		return checkpoints.get(position);
	}
	
	public Date getDateCheckpoint(int position) {
		if (dateCheckpoints.size() <= position) {
			return null;
		}
		
		return dateCheckpoints.get(position);
	}
	
	private void checkSize(int position, List<? extends Object> list) {
		
		// how many new entries we have to create to match new position
		// for example we are setting overtime with position 1 and array is empty - we have to create 2 entries
		int newEntries = position - list.size() + 1;
		for (int i = 0; i < newEntries; i++) {
			
			// this is just to fill empty spaces
			list.add(null);
		}		
	}	
}
