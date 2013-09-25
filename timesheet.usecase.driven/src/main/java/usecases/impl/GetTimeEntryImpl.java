package usecases.impl;

import java.util.ArrayList;
import java.util.List;

import timesheet.Day;
import timesheet.TimeEntry;
import timesheet.Timesheet;
import usecases.GetTimeEntry;

public class GetTimeEntryImpl implements GetTimeEntry {

	private Timesheet timesheet;

	public void setTimesheet(Timesheet timeSheet) {
		this.timesheet = timeSheet;
	}
	

	public List<TimeEntry> findEntries(Day day) {
		List<TimeEntry> result = this.timesheet.findEntries(day);
		return result;
	}

}
