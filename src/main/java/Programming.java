import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Programming implements Serializable {
	/**
	 * 
	 */
	private final static int BEGIN = 0;
	private final static int READ_DATE = 1;
	private final static int HALL_NUM = 2;
	private final static int READ_MOVIES = 3;

	private enum ad_times{MORNING,EVENING,NIGHT};

	private static final long serialVersionUID = 1L;
	private Set<String> movie_titles;
	// Set of movies for the week
	private String Title;
	private Map<Integer, Set<Projection>> projections;
	private Map<Integer, Set<String>> movies_hall;
	private String filename;
	private String program_date=null;
	private int state = BEGIN;
	private int num_projections = 0;
	private int sheet_num;
	public Programming() {
		movie_titles = new HashSet<String>();
		projections = new HashMap<Integer, Set<Projection>>();
		movies_hall = new HashMap<Integer, Set<String>>();
	}

	public Set<String> getMovie_titles() {
		return movie_titles;
	}

	public void setMovie_titles(Set<String> movie_titles) {
		this.movie_titles = movie_titles;
	}


	public String getKey()
	{
		return program_date + "\t\tSheet " + sheet_num;
	}


	public String getTitle() {
		return Title;
	}

	public int getSheet_num() {
		return sheet_num;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + num_projections;
		result = prime * result + ((program_date == null) ? 0 : program_date.hashCode());
		result = prime * result + sheet_num;
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
		Programming other = (Programming) obj;
		if (num_projections != other.num_projections)
			return false;
		if (program_date == null) {
			if (other.program_date != null)
				return false;
		} else if (!program_date.equals(other.program_date))
			return false;
		if (sheet_num != other.sheet_num)
			return false;
		return true;
	}

	public void setSheet_num(int sheet_num) {
		this.sheet_num = sheet_num;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public Map<Integer, Set<Projection>> getProjections() {
		return projections;
	}

	public void setProjections(Map<Integer, Set<Projection>> projections) {
		this.projections = projections;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Programming(String xlsFilePath) throws IOException {
		this();
		readExcel(xlsFilePath);
		for(int i=1;i<=Whiskers.getMAX_HALL();i++)
		{
			if(projections.containsKey(i))
			{
				for(Projection p : projections.get(i))
				{
					if(movies_hall.get(i) == null)
						movies_hall.put(i, new HashSet<String>());
					movies_hall.get(i).add(p.getName());

				}
				num_projections+= projections.get(i).size();
			}
		}
	}

	private void readExcel(String xlsFilePath) throws IOException {
		setFilename(xlsFilePath);
		int count=0;
		String date = "";
		String hall = "";
		String name;
		String start_time;
		String break_time;
		String end_time;
		File file;

		file = new File(xlsFilePath);
		System.out.println("Reading from : " + file.getAbsoluteFile());
		FileInputStream inputStream = new FileInputStream(file);
		Whiskers.log("reading " + xlsFilePath);
		Workbook workbook = getRelevantWorkbook(inputStream, xlsFilePath);

		sheet_num = Whiskers.getCHOSEN_SHEET();
		Sheet sheet = workbook.getSheetAt(sheet_num);

		Iterator<Row> iterator = sheet.iterator();
		Iterator<Cell> cellIterator = null;
		Iterator<Cell> cellIterator2 = null;
		Row row1;
		Row row2;
		Cell cell1=null;
		Cell cell2;
		String value=null;
		boolean from_hall =false;
		while (iterator.hasNext()) {
			if(!from_hall)
			{
				row1 = iterator.next();
				cellIterator = row1.cellIterator();

			}
			switch (state) {
			case BEGIN:
			case READ_DATE:
				if(!from_hall)
				{
					cell1 = cellIterator.next();

				}
				else
					from_hall = false;

				String[] header = cell1.getStringCellValue().split(" ");
				if(state == BEGIN)
				{
					Title = toTitle(header);
					Whiskers.log("Reading Programming for" + Title);
				}
				date = toDDMM(header[header.length - 1]);
				Whiskers.log("Read date: " + date + " at " + cell1.getAddress());
				state = HALL_NUM;
				row1 = iterator.next();
				break;
			case HALL_NUM:
				cell1 = cellIterator.next();

				hall = cell1.getStringCellValue();
				Whiskers.log("Read hall num: " + hall + " at " + cell1.getAddress());
				try {
					Integer.parseInt(hall);

				}
				catch(NumberFormatException e)
				{
					Whiskers.log("change state to READ DATE");
					from_hall = true;
					state = READ_DATE;
					break;
				}
				state = READ_MOVIES;
			case READ_MOVIES:
				cell1 = cellIterator.next();// hour viewer
				row2 = iterator.next();
				cellIterator2 = row2.cellIterator(); //title viewer

				cell2 = cellIterator2.next();//movie viewer
				while (cellIterator2.hasNext()) {
					cell2 = cellIterator2.next();//movie viewer
					value = cell2.getStringCellValue();
					Whiskers.log("trying to read " + value);
					if (!value.equals("")) {

						Whiskers.log("Read movie " + value + " at " + cell2.getAddress());
						name = value;
						start_time = cell1.getStringCellValue();
						Whiskers.log("Read time " + start_time + " at " + cell1.getAddress());
						if (cellIterator.hasNext())
							cell1 = cellIterator.next();
						break_time = cell1.getStringCellValue();
						Whiskers.log("Read time " + break_time + " at " + cell1.getAddress());
						if (cellIterator.hasNext())
							cell1 = cellIterator.next();
						end_time = cell1.getStringCellValue();
						Whiskers.log("Read time " + end_time + " at " + cell1.getAddress());

						/* Try to read from next cell*/
						if (cellIterator.hasNext())
							cell1 = cellIterator.next();
						if(cellIterator2.hasNext())
							cell2 = cellIterator2.next();
						if(cellIterator2.hasNext())
							cell2 = cellIterator2.next();
						Projection p = new Projection(name, start_time, break_time, end_time, date);
						if(program_date == null)
							program_date = date;
						count++;
						addProjection(Integer.parseInt(hall), p);
					}
					else //empty cell
					{
						cell1 = cellIterator.next();
						cell1 = cellIterator.next();
						if(cellIterator.hasNext())
							cell1 = cellIterator.next();


						if(cellIterator2.hasNext())
							cell2 = cellIterator2.next();
						if(cellIterator2.hasNext())
							cell2 = cellIterator2.next();

					}
				}


				state = HALL_NUM;
			}

		}
		System.out.println("Finished adding " + count + " projections");
		workbook.close();
		inputStream.close();

	}

	private String toDDMM(String string) {
		String date[] = string.split("/");
		if(date[0].length() == 1)
			date[0] = "0" + date[0];
		if(date[1].length() == 1)
			date[1] = "0" + date[1];
		return date[1] + "/" +date[0]+ "/" + date[2];
	}

	private String toTitle(String[] header) {
		StringBuilder b = new StringBuilder("");
		for(int i=0;i<header.length-2;i++)
		{
			b.append(header[i]+" ");
		}
		return b.toString();
	}

	public void addProjection(int hall_num, Projection p) {
		if (!projections.containsKey(hall_num)) {
			projections.put(hall_num, new HashSet<Projection>());
		}
		projections.get(hall_num).add(p);
		//if(p.getType().equals(main.MOVIE))
		movie_titles.add(p.getName());
	}

	public static Workbook getRelevantWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
		Workbook workbook = null;

		if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook(inputStream);
		} else if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(inputStream);
		} else {
			throw new IllegalArgumentException("Incorrect file format");
		}

		return workbook;
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("Programming summary for " + filename + " - " + program_date + " "+ Title +"\r\n");
		for(int i=1;i<=Whiskers.getMAX_HALL();i++)
		{
			if(projections.containsKey(i))
			{
				b.append("\r\n---------------HALL " + i + "-----------------\r\n");
				for(String s : movies_hall.get(i))
				{

					b.append("--" + s + "  " + getTimes(i,s) + "\r\n");
				}
			}
		}
		return b.toString();
	}

	private String getTimes(int hall_num, String movie_name) {
		StringBuilder sb = new StringBuilder();
		boolean morning=false;
		boolean evening=false;
		boolean night=false;
		sb.append("(");
		for(Projection p : projections.get(hall_num))
		{
			if(p.getName().equals(movie_name))
			{
				ad_times result=null;
				result = getTime(p);
				if(result.equals(ad_times.MORNING))
					morning = true;
				else if(result.equals(ad_times.EVENING))
					evening = true;
				else if(result.equals(ad_times.NIGHT))
					night = true;
			}

			if(morning && evening && night)
				break;
		}

		boolean sign = false;
		if(morning)
		{
			sb.append(" 7 ");
			sign=true;
		}
		if(evening)
		{
			if(sign)
				sb.append(",");
			sb.append(" 17 ");
			sign = true;
		}
		if(night)
		{
			if(sign)
				sb.append(",");
			sb.append(" 21 ");
		}
		sb.append(")");
		if(!morning && !evening && !night)
			return "[            ]";
		return sb.toString();
	}

	private ad_times getTime(Projection p) {
		if(p.getTime_hour() >= 7 && ((p.getTime_hour() == 17 && p.getTime_mins() < 30) || p.getTime_hour() < 17))
			return ad_times.MORNING;
		else if(( (p.getTime_hour() == 21 && p.getTime_mins() < 20) || (p.getTime_hour() < 21) ) && ((p.getTime_hour() == 17 && p.getTime_mins() >= 30) || p.getTime_hour() > 17))
			return ad_times.EVENING;
		return ad_times.NIGHT;
	}

	public String prettyPrint() {
		final StringBuilder b = new StringBuilder();
		System.out.println(Title);
		for(int i=1;i<=Whiskers.getMAX_HALL();i++)
		{
			if(projections.containsKey(i))
			{

				b.append("\r\n---------------HALL " + i + "-----------------\r\n");
				for(String s : movies_hall.get(i))
					b.append(s + "\r\n");
			}
		}

		movie_titles.forEach(new Consumer<String>() {
			public void accept(String s) {
				b.append(s + "\r\n");
			}
		});


		return b.toString();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		String s[] = filename.split("\\\\");
		this.filename = s[s.length-1];
	}

	public Map<Integer, Set<String>> getMovies_hall() {
		return movies_hall;
	}

	public void setMovies_hall(Map<Integer, Set<String>> movies_hall) {
		this.movies_hall = movies_hall;
	}


	public String getProgram_date() {
		return program_date;
	}

	public void setProgram_date(String program_date) {
		this.program_date = program_date;
	}

	public int getNum_projections() {
		return num_projections;
	}

	public void setNum_projections(int num_projections) {
		this.num_projections = num_projections;
	}
}
