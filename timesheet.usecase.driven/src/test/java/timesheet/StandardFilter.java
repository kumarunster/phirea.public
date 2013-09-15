package timesheet;

import java.util.Calendar;

public enum StandardFilter implements Filter {

	LAST_WEEK() {
		public Day getFromDay() {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			
			return new Day(calendar.getTime());
		}

		public Day getToDay() {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			
			return new Day(calendar.getTime());
		}
	}, 
	LAST_MONTH() {
		public Day getFromDay() {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			
			return new Day(calendar.getTime());
		}

		public Day getToDay() {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -1);
			int lastDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);
			calendar.set(Calendar.DAY_OF_MONTH, lastDay);
			return new Day(calendar.getTime());
		}
	};

	public Day getFromDay() {
		// TODO Auto-generated method stub
		return null;
	}

	public Day getToDay() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
