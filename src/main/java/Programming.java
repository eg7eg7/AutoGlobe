import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
	Set<String> movie_titles;
	// Set of movies for the week
	String Title;
	Map<Integer, Set<Projection>> projections;

	private int state = BEGIN;

	public Programming() {
		movie_titles = new HashSet<String>();
		// movie_hall_movies = new HashMap<Integer, Map<String,Set<Integer>>>();
		projections = new HashMap<Integer, Set<Projection>>();
	}

	public Set<String> getMovie_titles() {
		return movie_titles;
	}

	public void setMovie_titles(Set<String> movie_titles) {
		this.movie_titles = movie_titles;
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
		String date = "";
		String hall = "";
		String name;
		String start_time;
		String break_time;
		String end_time;
		try {

			FileInputStream inputStream = new FileInputStream(new File(xlsFilePath));
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

			while (iterator.hasNext()) {
				row1 = iterator.next();
				cellIterator = row1.cellIterator();
				switch (state) {
				case BEGIN:
				case READ_DATE:
					cell1 = cellIterator.next();
					
					String[] header = cell1.getStringCellValue().split(" ");
					if(state == BEGIN)
						Title = toTitle(header);
					date = header[header.length - 1];
					main.log("Read date: " + date + " at " + cell1.getAddress());
					state = HALL_NUM;
					row1 = iterator.next();// skip "Ulam"
					
					break;
				case HALL_NUM:
					cell1 = cellIterator.next();
					hall = cell1.getStringCellValue();
					main.log("Read hall num: " + hall + " at " + cell1.getAddress());
					state = READ_MOVIES;
				case READ_MOVIES:
					cell1 = cellIterator.next();// hour viewer
					row2 = iterator.next();
					cellIterator2 = row2.cellIterator();
					while (cellIterator2.hasNext()) {

						cell2 = cellIterator2.next();
						String value = cell2.getStringCellValue();

						if (!value.equals("")) {
						
							main.log("Read movie " + value + " at " + cell2.getAddress());
							name = value;
							start_time = cell1.getStringCellValue();
							main.log("Read time " + start_time + " at " + cell1.getAddress());
							cell1 = cellIterator.next();
							break_time = cell1.getStringCellValue();
							main.log("Read time " + break_time + " at " + cell1.getAddress());
							cell1 = cellIterator.next();
							end_time = cell1.getStringCellValue();
							main.log("Read time " + end_time + " at " + cell1.getAddress());
							if (cellIterator.hasNext())
								cell1 = cellIterator.next();
							Projection p = new Projection(name, start_time, break_time, end_time, date);
							addProjection(Integer.parseInt(hall), p);

						}
					}

					
					state = HALL_NUM;
					if(Integer.parseInt(hall) == main.getMAX_HALL())
						state = READ_DATE;
				}

			}

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
		System.out.println(Title);
		for(int i=1;i<=main.getMAX_HALL();i++)
		{
			if(projections.containsKey(i))
			{

				b.append("HALL " + i + "\n" + projections.get(i));
			}
		}
		
		movie_titles.forEach(new Consumer<String>() {
			public void accept(String s) {
				b.append(s + "\n");
			}
		});
			
		
		return b.toString();
	}
	
	
}
