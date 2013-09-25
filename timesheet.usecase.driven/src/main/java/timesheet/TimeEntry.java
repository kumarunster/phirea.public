package timesheet;


public class TimeEntry {

	private Day day;
	private String activity;
	private Integer id;

	public void setDay(Day day) {
		this.day = day;
	}

	public Day getDay() {
		return this.day;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
	
	public String getActivity() {
		return this.activity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
