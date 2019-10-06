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

	private static final long serialVersionUID = 1L;
	private Set<String> movie_titles;
	// Set of movies for the week
	private String Title;
	private Map<Integer, Set<Projection>> projections;
	private Map<Integer, Set<String>> movies_hall;
	private String filename;
	private String program_date=null;
	private int state = BEGIN;

	public Programming() {
		movie_titles = new HashSet<String>();
		// movie_hall_movies = new HashMap<Integer, Map<String,Set<Integer>>>();
		projections = new HashMap<Integer, Set<Projection>>();
		movies_hall = new HashMap<Integer, Set<String>>();
	}

	public Set<String> getMovie_titles() {
		return movie_titles;
	}

	public void setMovie_titles(Set<String> movie_titles) {
		this.movie_titles = movie_titles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((program_date == null) ? 0 : program_date.hashCode());
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
		if (program_date == null) {
			if (other.program_date != null)
				return false;
		} else if (!program_date.equals(other.program_date))
			return false;
		return true;
	}

	public String getTitle() {
		return Title;
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

	public Programming(String xlsFilePath) {
		this();
		readExcel(xlsFilePath);
		for(int i=1;i<=main.getMAX_HALL();i++)
		{
			if(projections.containsKey(i))
			{
				for(Projection p : projections.get(i))
				{
					if(movies_hall.get(i) == null)
						movies_hall.put(i, new HashSet<String>());
					movies_hall.get(i).add(p.getName());
				}
			}
		}
	}

	private void readExcel(String xlsFilePath) {
		setFilename(xlsFilePath);
		int count=0;
		String date = "";
		String hall = "";
		String name;
		String start_time;
		String break_time;
		String end_time;
		File file;
		try {
			file = new File(xlsFilePath);
			System.out.println("Reading from : " + file.getAbsoluteFile());
			FileInputStream inputStream = new FileInputStream(file);
			main.log("reading " + xlsFilePath);
			Workbook workbook = getRelevantWorkbook(inputStream, xlsFilePath);
			Sheet sheet = workbook.getSheetAt(main.getCHOSEN_SHEET());
			Iterator<Row> iterator = sheet.iterator();
			Iterator<Cell> cellIterator = null;
			Iterator<Cell> cellIterator2 = null;
			Row row1;
			Row row2;
			Cell cell1;
			Cell cell2;
			String value=null;
			while (iterator.hasNext()) {
				row1 = iterator.next();
				cellIterator = row1.cellIterator();
				switch (state) {
				case BEGIN:
				case READ_DATE:
					cell1 = cellIterator.next();

					String[] header = cell1.getStringCellValue().split(" ");
					if(state == BEGIN)
					{
						Title = toTitle(header);
						main.log("Reading Programming for" + Title);
					}
					date = header[header.length - 1];
					main.log("Read date: " + date + " at " + cell1.getAddress());
					state = HALL_NUM;
					row1 = iterator.next(); // skip "Ulam" row

					break;
				case HALL_NUM:
					cell1 = cellIterator.next();
					hall = cell1.getStringCellValue();
					main.log("Read hall num: " + hall + " at " + cell1.getAddress());
					try {
						Integer.parseInt(hall);
					}
					catch(NumberFormatException e)
					{
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
						main.log("trying to read " + value);
						if (!value.equals("")) {

							main.log("Read movie " + value + " at " + cell2.getAddress());
							name = value;
							start_time = cell1.getStringCellValue();
							main.log("Read time " + start_time + " at " + cell1.getAddress());
							if (cellIterator.hasNext())
								cell1 = cellIterator.next();
							break_time = cell1.getStringCellValue();
							main.log("Read time " + break_time + " at " + cell1.getAddress());
							if (cellIterator.hasNext())
								cell1 = cellIterator.next();
							end_time = cell1.getStringCellValue();
							main.log("Read time " + end_time + " at " + cell1.getAddress());

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
					if(Integer.parseInt(hall) == main.getMAX_HALL())
					{
						//row1 = iterator.next();
						state = READ_DATE;
					}
				}

			}
			System.out.println("Finished adding " + count + " projections");
			workbook.close();
			inputStream.close();
		} catch (Exception e) {
			System.out.println(e);
		}

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

	private static Workbook getRelevantWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
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
		b.append("Printing programming summary for " + filename + " - " + program_date + "\n");
		for(int i=1;i<=main.getMAX_HALL();i++)
		{
			if(projections.containsKey(i))
			{
				b.append("\n---------------HALL " + i + "-----------------\n");
				for(String s : movies_hall.get(i))
				{
					b.append(s + "\n");
				}
			}
		}

		movie_titles.forEach(new Consumer<String>() {
			public void accept(String s) {
				//b.append(s + "\n");
			}
		});


		return b.toString();
	}

	public String prettyPrint() {
		final StringBuilder b = new StringBuilder();
		System.out.println(Title);
		for(int i=1;i<=main.getMAX_HALL();i++)
		{
			if(projections.containsKey(i))
			{

				b.append("\n---------------HALL " + i + "-----------------\n" + movies_hall.get(i));
			}
		}

		movie_titles.forEach(new Consumer<String>() {
			public void accept(String s) {
				b.append(s + "\n");
			}
		});


		return b.toString();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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
}
