package timesheet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Day {

	Date date;
	
	public Day() {
		this.date = truncate(new Date());
	}
	
	public Day(Date time) {
		this.date = truncate(time);
	}
	
	private Date truncate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}

	public boolean between(Day fromDay, Day toDay) {
		return (fromDay.date.before(this.date) && toDay.date.after(date) ) || 
				(fromDay.date.equals(this.date) || toDay.date.equals(this.date) );
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		return sdf.format(this.date);
	}

}
