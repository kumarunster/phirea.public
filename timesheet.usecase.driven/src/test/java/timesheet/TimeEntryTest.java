package timesheet;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

public class TimeEntryTest {
	
	
	@Test
	public void canAddTimeEntry() {
		TimeEntry timeEntry = new TimeEntry();
		Day day = new Day();
		timeEntry.setDay(day);
		
		Timesheet timeSheet = new TimeRepository();
		timeSheet.add(timeEntry);
		
		List<TimeEntry> timeEntries = timeSheet.findEntries(day);
		assertNotNull(timeEntries);
		assertEquals(1, timeEntries.size());
		assertSame(timeEntry, timeEntries.get(0));
	}
	
	@Test
	public void canFindEntriesForLastWeek() {
		
		TimeEntry timeEntry = new TimeEntry();
		
		Calendar today = Calendar.getInstance();
		Calendar workCal = (Calendar) today.clone();
		workCal.add(Calendar.WEEK_OF_YEAR, -1);
		
		Day day = new Day(workCal.getTime());
		timeEntry.setDay(day);
		
		Timesheet timeSheet = new TimeRepository();
		timeSheet.add(timeEntry);
		
		Filter filter = StandardFilter.LAST_WEEK;
		
		List<TimeEntry> timeEntries = timeSheet.findEntries(filter);
		assertNotNull(timeEntries);
		assertEquals(1, timeEntries.size());
		assertSame(timeEntry, timeEntries.get(0));
		
	}
	
	
	@Test
	public void canFindEntriesForLastMonth() {
		
		TimeEntry timeEntry = new TimeEntry();
		
		Calendar today = Calendar.getInstance();
		Calendar workCal = (Calendar) today.clone();
		workCal.add(Calendar.WEEK_OF_YEAR, -4);
		
		Day day = new Day(workCal.getTime());
		timeEntry.setDay(day);
		
		Timesheet timeSheet = new TimeRepository();
		timeSheet.add(timeEntry);
		
		Filter filter = StandardFilter.LAST_MONTH;
		
		List<TimeEntry> timeEntries = timeSheet.findEntries(filter);
		assertNotNull(timeEntries);
		assertEquals(1, timeEntries.size());
		assertSame(timeEntry, timeEntries.get(0));
		
	}
	
	

}
