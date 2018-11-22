import java.io.File;
import java.util.ArrayList;

public class Database {
	private static ArrayList<Programming> programmingList = new ArrayList<Programming>();
	private static String globeFile = "Autoglobe.dat";
	public Database()
	{
		fillDatabaseFromFile(new File(globeFile));
	}

	private void fillDatabaseFromFile(File f) {
		// TODO clean previous data and read again
		
	}

	public static ArrayList<Programming> getProgrammingList() {
		return programmingList;
	}

	public static void setProgrammingList(ArrayList<Programming> programmingList) {
		Database.programmingList = programmingList;
	}
	
	public void addProgramming(Programming p)
	{
		if(!programmingList.contains(p))
			programmingList.add(p);
	}

	@Override
	public String toString() {
		return programmingList.toString();
	}
	
}
