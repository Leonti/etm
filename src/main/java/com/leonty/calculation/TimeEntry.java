package com.leonty.calculation;

import java.math.BigDecimal;
import java.util.Date;

public interface TimeEntry {

	Date getTimeIn();
	Date getTimeOut();
	void setTimeOut(Date timeOut);
	BigDecimal getWage();
}
