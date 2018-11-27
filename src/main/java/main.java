
public class main {

	private static int CHOSEN_SHEET = 8;
	
	public static void main(String[] args) {
		Database db = new Database();
		db.addProgramming(new Programming("1.xls"));

	}

	public static void log(String string) {
		//System.out.println(string);
		//TODO add log file
	}

	public static int getCHOSEN_SHEET() {
		return CHOSEN_SHEET;
	}

	public static void setCHOSEN_SHEET(int cHOSEN_SHEET) {
		CHOSEN_SHEET = cHOSEN_SHEET;
	}

}
