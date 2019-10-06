import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class main {

	private static int CHOSEN_SHEET = 8;
	private static int MAX_HALL = 18;
	private static final boolean showLog = false;
	private static final boolean printLog = true;
	public static final String MOVIE = "Movie";
	public static final String EVENT = "Event";
	public static final String NO_BREAK = "No intermission";
	public static String log_name = "WeeklyProg.log";
	public static File f = new File(log_name);
	public static BufferedWriter bw = null;
	public static FileWriter fw = null;

	private static int line_num=0;
	public static void main(String[] args) {
		
		try {
			if (f.exists())
				f.delete();
			f.createNewFile();
			
				
			
			fw = new FileWriter(log_name, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		bw = new BufferedWriter(fw);
		Database db = new Database();
		db.fillDatabaseFromFile();
		
		//Database db2 = new Database();

		db.addProgramming(new Programming("04.xls"));
		db.addProgramming(new Programming("01.xls"));
		db.addProgramming(new Programming("02.xls"));
		db.addProgramming(new Programming("03.xls"));
		System.out.println(db);
		db.printPrettyProgram(2);
		
		//db2.fillDatabaseFromFile();
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		//System.out.println(db);
		db.saveToFile();
	}

	public static void log(String string) {
		if (showLog)
			System.out.println(string);
		if (printLog) {
			try {
				writeLog(++line_num + " - "+ string);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void writeLog(String info) throws IOException {
		if (!f.exists())
			f.createNewFile();

		try {
			bw.write(info);
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public static int getCHOSEN_SHEET() {
		return CHOSEN_SHEET;
	}

	public static void setCHOSEN_SHEET(int cHOSEN_SHEET) {
		CHOSEN_SHEET = cHOSEN_SHEET;
	}

	public static int getMAX_HALL() {
		return MAX_HALL;
	}

	public static void setMAX_HALL(int mAX_HALL) {
		MAX_HALL = mAX_HALL;
	}

}
