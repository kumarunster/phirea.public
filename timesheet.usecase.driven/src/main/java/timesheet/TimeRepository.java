package timesheet;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class TimeRepository implements Timesheet {

	Map<Day, List<TimeEntry>> timeEntries = new LinkedHashMap<Day, List<TimeEntry>>();
	
	public Boolean add(TimeEntry timeEntry) {
		Boolean result = false;
		check(timeEntry);	
		
		List<TimeEntry> list = getTimeEntries(timeEntry.getDay());
			
		list.add(timeEntry);
		
		result = true;
		return result;
	}

	private List<TimeEntry> getTimeEntries(Day day) {
		List<TimeEntry> list = timeEntries.get(day);
		if(list == null) {
			list = new ArrayList<TimeEntry>();
			timeEntries.put(day, list);
		}
		return list;
	}

	private void check(TimeEntry timeEntry) {
		Day day = timeEntry.getDay();
		if(day == null)
			throw new IllegalArgumentException("day cannot be null");
	}

	public void checkValid(TimeEntry timeEntry) {
		check(timeEntry);
	}
	
	public List<TimeEntry> findEntries(Day day) {
		List<TimeEntry> result = getTimeEntries(day);
		return result;
	}


	public List<TimeEntry> findEntries(Filter filter) {
		List<TimeEntry> result = new ArrayList<TimeEntry>();
		
		Day fromDay = filter.getFromDay();
		Day toDay = filter.getToDay();
		
		for (Entry<Day, List<TimeEntry>> entry : timeEntries.entrySet()) {
			if(entry.getKey().between(fromDay, toDay))
				result.addAll(entry.getValue());
		}
		
		
		return result;
	}

	public TimeEntry set(TimeEntry timeEntry) {
		List<TimeEntry> timeEntries = getTimeEntries(timeEntry.getDay());
		int indexOf = 0;
		for (TimeEntry entry : timeEntries) {
			if(entry.getId().equals(timeEntry.getId())) {
				break;
			}
			indexOf++;
		}
		
		timeEntries.set(indexOf, timeEntry);
		
		return timeEntry;
	}
	
	

}
