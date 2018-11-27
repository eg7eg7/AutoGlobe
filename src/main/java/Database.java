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
			return;
		}

		Set<String> old_movies = programmingList.get(i-2).getMovie_titles();
		Set<String> new_movies = programmingList.get(i-1).getMovie_titles();
		Set<String> removed_movies = new HashSet<String>();
		
		
		String[] old_movies_arr = old_movies.stream().toArray(n -> new String[n]);
		
		
		for(int j=0;j<old_movies_arr.length;j++)
		{
			String movie_name = old_movies_arr[j];
			if(!new_movies.contains(movie_name))
			{
				removed_movies.add(movie_name);
			}
		}
		
		System.out.println(removed_movies);
		
	}

}
