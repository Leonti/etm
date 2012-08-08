package com.leonty.etm.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.leonty.etm.test.WorkDayTest;
import com.leonty.etm.test.WorkEntryTest;
import com.leonty.etm.test.WorkWeekTest;

@RunWith(Suite.class)
@SuiteClasses({ WorkDayTest.class, WorkEntryTest.class, WorkWeekTest.class })
public class TimeCalculations {

}
