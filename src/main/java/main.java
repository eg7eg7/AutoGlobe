
public class main {

	private static int CHOSEN_SHEET = 8;
	private static int MAX_HALL = 18;
	private static final boolean showLog = false;
	
	public static void main(String[] args) {
		Database db = new Database();
		db.addProgramming(new Programming("4.xls"));
		System.out.println(db);

	}

	public static void log(String string) {
		if(showLog)
			System.out.println(string);
		//TODO add log file
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
