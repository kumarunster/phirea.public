package usecases;

import java.util.List;

import timesheet.Day;
import timesheet.TimeEntry;

public interface GetTimeEntry {

	List<TimeEntry> findEntries(Day day);

}
