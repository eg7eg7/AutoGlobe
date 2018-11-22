
public class Projection {
	private int type; 	//movie or projection
	private int time_hour;
	private int day_month;
	private int month;
	private String name;
	private int hall;
	
	
	public Projection(int type, int time_hour, int day_month, int month, String name, int hall) {
		super();
		this.type = type;
		this.time_hour = time_hour;
		this.day_month = day_month;
		this.month = month;
		this.name = name;
		this.hall = hall;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTime_hour() {
		return time_hour;
	}
	public void setTime_hour(int time_hour) {
		this.time_hour = time_hour;
	}
	public int getDay_month() {
		return day_month;
	}
	public void setDay_month(int day_month) {
		this.day_month = day_month;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Projection [type=" + type + ", time=" + time_hour + ", date" + day_month + "/" + month
				+ ", name=" + name + "]";
	}
	
	
}
