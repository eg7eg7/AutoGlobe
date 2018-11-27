import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class Database {
	private static ArrayList<Programming> programmingList = new ArrayList<Programming>();
	private static String globeFile = "Autoglobe.dat";

	private File f;

	public Database() {
		fillDatabaseFromFile(new File(globeFile));
	}

	private void fillDatabaseFromFile(File f) {
		// TODO clean previous data and read again
		// TODO Get file location here

	}

	public static ArrayList<Programming> getProgrammingList() {
		return programmingList;
	}

	public static void setProgrammingList(ArrayList<Programming> programmingList) {
		Database.programmingList = programmingList;
	}

	public void addProgramming(Programming p) {
		if (!programmingList.contains(p))
			programmingList.add(p);
	}

	@Override
	public String toString() {
		return programmingList.toString();
	}

	public void showExpiredMovies(int i) {
		if(i-2 < 0)
		{

			System.out.println("No expired movies");
		}

		
	}

}
