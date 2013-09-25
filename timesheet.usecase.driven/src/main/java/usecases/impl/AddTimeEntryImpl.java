package usecases.impl;

import timesheet.TimeEntry;
import timesheet.Timesheet;
import usecases.AddTimeEntry;


public class AddTimeEntryImpl implements AddTimeEntry {

	private Timesheet timesheet;

	public void setTimesheet(Timesheet timeSheet) {
		this.timesheet = timeSheet;
	}
	
	public TimeEntry add(TimeEntry timeEntry) {
		
		timesheet.add(timeEntry);
		
		return timeEntry;
	}

}
