package timesheet.usecases;

import java.util.List;

import org.junit.Test;

import timesheet.Day;
import timesheet.TimeEntry;
import timesheet.TimeRepository;
import timesheet.Timesheet;
import usecases.AddTimeEntry;
import usecases.EditTimeEntry;
import usecases.GetTimeEntry;
import usecases.impl.AddTimeEntryImpl;
import usecases.impl.EditTimeEntryImpl;
import usecases.impl.GetTimeEntryImpl;

import static org.junit.Assert.*;

public class TestUseCases {
	
	@Test
	public void shouldCanAddEntry() throws Exception {
		AddTimeEntry addTimeEntry = new AddTimeEntryImpl();
		
		Timesheet timeSheet = new TimeRepository();
		
		((AddTimeEntryImpl) addTimeEntry).setTimesheet(timeSheet);
		
		TimeEntry timeEntry = new TimeEntry();
		Day day = new Day();
		timeEntry.setDay(day);
		
		TimeEntry result = addTimeEntry.add(timeEntry);
		
		assertNotNull(result);
		
		List<TimeEntry> timeEntries = timeSheet.findEntries(day);
		assertNotNull(timeEntries);
		assertEquals(1, timeEntries.size());
		assertSame(timeEntry, timeEntries.get(0));
		
	}
	
	
	
	@Test
	public void shouldCanEditEntry() throws Exception {
		AddTimeEntry addTimeEntry = new AddTimeEntryImpl();
		EditTimeEntry editTimeEntry = new EditTimeEntryImpl();
		GetTimeEntry getTimeEntry = new GetTimeEntryImpl();
		
		Timesheet timeSheet = new TimeRepository();
		
		((AddTimeEntryImpl) addTimeEntry).setTimesheet(timeSheet);
		((EditTimeEntryImpl) editTimeEntry).setTimesheet(timeSheet);
		((GetTimeEntryImpl) getTimeEntry).setTimesheet(timeSheet);
		
		
		TimeEntry timeEntry = new TimeEntry();
		Day day = new Day();
		timeEntry.setDay(day);
		timeEntry.setId(1);
		
		TimeEntry result = addTimeEntry.add(timeEntry);
		assertNotNull(result);
		
		List<TimeEntry> timeEntries = getTimeEntry.findEntries(day);
		assertNotNull(timeEntries);
		assertEquals(1, timeEntries.size());
		assertSame(timeEntry, timeEntries.get(0));
		
		timeEntry.setActivity("My Tasks");
		
		TimeEntry editedTimeEntry = editTimeEntry.editTimeEntry(timeEntry);
		assertEquals("My Tasks", editedTimeEntry.getActivity());
		
	}
}
