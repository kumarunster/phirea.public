package timesheet;

import java.util.List;

public interface Timesheet {

	Boolean add(TimeEntry timeEntry);
	
	List<TimeEntry> findEntries(Day day);

	void checkValid(TimeEntry timeEntry);

	List<TimeEntry> findEntries(Filter filter);

}
