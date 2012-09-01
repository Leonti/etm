package com.leonty.etm.calculation;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public interface TimeEntry {

	DateTime getTimeIn();
	DateTime getTimeOut();
	void setTimeOut(DateTime timeOut);
	BigDecimal getWage();
	String getJobTitle();
}
