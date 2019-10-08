import java.io.Serializable;

public class Projection implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4805570389453823828L;
	
	private String type; // movie or projection
	
	private int time_hour;
	private int time_mins;
	
	private String time_hour_string;
	private String time_break_string;
	private String time_end_string;
	
	private int time_break;
	private int time_end;
	
	private int day_month;
	private int month;
	private int year;
	
	private String date;
	private String projection_name;




	public Projection(String name, String time_hour, String time_break, String time_end, String date) {
		projection_name = name;
		setTime_hour(time_hour);
		setTime_break(time_break);
		setTime_end(time_end);
		setDate(date);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getTime_hour() {
		return time_hour;
	}

	public void setTime_hour(String time_hour) {
		
		try {
			if (!time_hour.equals(""))
			{
				String s[] = time_hour.split(":");
				
				this.time_hour = Integer.parseInt(s[0]);
				this.time_mins = Integer.parseInt(s[1]);
				setTime_hour_string(time_hour);
			}
			
		} catch (Exception e) {
			Whiskers.log(e.toString());
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
		StringBuilder builder = new StringBuilder();
		builder.append("Projection [type=").append(type).append(", time_hour_string=").append(time_hour_string)
				.append(", time_break_string=").append(time_break_string).append(", time_end_string=")
				.append(time_end_string).append(", day_month=").append(day_month).append(", month=").append(month)
				.append(", year=").append(year).append(", date=").append(date).append(", projection_name=")
				.append(projection_name).append("]");
		return builder.toString();
	}

	public int getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		String s = "";
		try {
			if (!time_end.equals(""))
			{
			this.time_end_string = time_end;
			s = time_end.replace(":", "");
			this.time_end = Integer.parseInt(s);
			
			}
		} catch (Exception e) {

		}
	}

	public int getTime_break() {
		return time_break;
	}

	public void setTime_break(String time_break) {
		try {
			if (!time_break.equals("")) // movie
			{
				type = Whiskers.MOVIE;
				time_break = time_break.replace("(", "").replace(")", "");
				time_break_string = time_break;
				this.time_break = Integer.parseInt(time_break.replace(":", ""));
			} else {
				type = Whiskers.EVENT;
				time_break_string = Whiskers.NO_BREAK;
			}
		} catch (Exception e) {
			Whiskers.log(e.toString());

		}
	}

	public String getTime_hour_string() {
		return time_hour_string;
	}

	public void setTime_hour_string(String time_hour_string) {
		this.time_hour_string = time_hour_string;
	}

	public String getTime_break_string() {
		return time_break_string;
	}

	public void setTime_break_string(String time_break_string) {
		this.time_break_string = time_break_string;
	}

	public String getTime_end_string() {
		return time_end_string;
	}

	public void setTime_end_string(String time_end_string) {
		this.time_end_string = time_end_string;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
		String[] s = date.split("/");
		this.day_month = Integer.parseInt(s[1]);
		this.month = Integer.parseInt(s[0]);
		this.year = Integer.parseInt(s[2]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + day_month;
		result = prime * result + month;
		result = prime * result + ((projection_name == null) ? 0 : projection_name.hashCode());
		result = prime * result + time_break;
		result = prime * result + ((time_break_string == null) ? 0 : time_break_string.hashCode());
		result = prime * result + time_end;
		result = prime * result + ((time_end_string == null) ? 0 : time_end_string.hashCode());
		result = prime * result + time_hour;
		result = prime * result + ((time_hour_string == null) ? 0 : time_hour_string.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + year;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Projection other = (Projection) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (day_month != other.day_month)
			return false;
		if (month != other.month)
			return false;
		if (projection_name == null) {
			if (other.projection_name != null)
				return false;
		} else if (!projection_name.equals(other.projection_name))
			return false;
		if (time_break != other.time_break)
			return false;
		if (time_break_string == null) {
			if (other.time_break_string != null)
				return false;
		} else if (!time_break_string.equals(other.time_break_string))
			return false;
		if (time_end != other.time_end)
			return false;
		if (time_end_string == null) {
			if (other.time_end_string != null)
				return false;
		} else if (!time_end_string.equals(other.time_end_string))
			return false;
		if (time_hour != other.time_hour)
			return false;
		if (time_hour_string == null) {
			if (other.time_hour_string != null)
				return false;
		} else if (!time_hour_string.equals(other.time_hour_string))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (year != other.year)
			return false;
		return true;
	}

	public int getTime_mins() {
		return time_mins;
	}

	public void setTime_mins(int time_mins) {
		this.time_mins = time_mins;
	}
}
