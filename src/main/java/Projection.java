import java.io.Serializable;

public class Projection implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4805570389453823828L;
	private int type; 	//movie or projection
	private int time_hour;
	private String time_hour_string;
	private int time_break;
	private int time_end;
	private int day_month;
	private int month;
	private String projection_name;
	

	
	public Projection(String name,String time_hour,String time_break,String time_end, String date) {
		projection_name = name;
		setTime_hour(time_hour);
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
	public void setTime_hour(String time_hour) {
		String s="";
		try {
			if(!time_hour.equals(""))
			s = time_hour.replace(":", "");
			this.time_hour = Integer.parseInt(s);
			setTime_hour_string(s);
		}
		catch(Exception e)
		{
			main.log(e.toString());
		}
		
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
		return projection_name;
	}
	public void setName(String name) {
		this.projection_name = name;
	}

	@Override
	public String toString() {
		return "Projection [type=" + type + ", time=" + time_hour + ", date" + day_month + "/" + month
				+ ", name=" + projection_name + "]";
	}

	public int getTime_end() {
		return time_end;
	}

	public void setTime_end(int time_end) {
		this.time_end = time_end;
	}

	public int getTime_break() {
		return time_break;
	}

	public void setTime_break(int time_break) {
		this.time_break = time_break;
	}



	public String getTime_hour_string() {
		return time_hour_string;
	}



	public void setTime_hour_string(String time_hour_string) {
		this.time_hour_string = time_hour_string;
	}
	
	
}
