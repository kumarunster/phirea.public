package usecases.impl;

import timesheet.TimeEntry;
import timesheet.Timesheet;
import usecases.EditTimeEntry;

public class EditTimeEntryImpl implements EditTimeEntry {

	private Timesheet timesheet;
	
	public void setTimesheet(Timesheet timeSheet) {
		this.timesheet = timeSheet;
	}

	public TimeEntry editTimeEntry(TimeEntry timeEntry) {
		
		return timesheet.set(timeEntry);
	}

}
