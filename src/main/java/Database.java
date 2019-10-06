import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Database {
	private static ArrayList<Programming> programmingList = new ArrayList<Programming>();
	private static String globeFile = "Autoglobe.dat";
	
	public Database() {
	}

	public void fillDatabaseFromFile() {
		// TODO clean previous data and read again
		// TODO Get file location here
		programmingList.clear();
		int num_programs = 0;
		
		ObjectInputStream oos = null;
		try {
			FileInputStream fos = new FileInputStream(globeFile);
			oos = new ObjectInputStream(fos);

			num_programs = oos.readInt();
			for(int i=0;i<num_programs;i++)
			{
				try {
					addProgramming((Programming) oos.readObject());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
				
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(oos !=null)
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static ArrayList<Programming> getProgrammingList() {
		return programmingList;
	}

	public static void setProgrammingList(ArrayList<Programming> programmingList) {
		Database.programmingList = programmingList;
	}

	public void addProgramming(Programming p) {
		if (!programmingList.contains(p))
		{
			programmingList.add(p);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<programmingList.size();i++)
		{
			sb.append(programmingList.get(i).toString());
			sb.append(showNewMovies(i));
			sb.append(showExpiredMovies(i));
		}
		return sb.toString();
	}

	public String showExpiredMovies(int i) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nExpired movies :\n");
		if(i-2 < 0)
		{

			sb.append("No expired movies");
			return sb.toString();
		}
		else if (i>programmingList.size())
		{
			sb.append("Invalid programming value");
			return sb.toString();
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
		
		for(String s : removed_movies)
			sb.append(s + "\n");
		return sb.toString();
	}
	
	public String showNewMovies(int i) {
		StringBuilder sb = new StringBuilder();
		sb.append("\nNew movies :\n");
		if(i-2 < 0)
		{

			sb.append("No new movies");
			return sb.toString();
		}
		else if (i>programmingList.size())
		{
			sb.append("Invalid programming value");
			return sb.toString();
		}
		Set<String> old_movies = programmingList.get(i-2).getMovie_titles();
		Set<String> new_movies = programmingList.get(i-1).getMovie_titles();
		Set<String> brand_new = new HashSet<String>();


		String[] new_movies_arr = new_movies.stream().toArray(n -> new String[n]);


		for(int j=0;j<new_movies_arr.length;j++)
		{
			String movie_name = new_movies_arr[j];
			if(!old_movies.contains(movie_name))
			{
				brand_new.add(movie_name);
			}
		}
		
		for(String s : brand_new)
			sb.append(s + "\n");
		return sb.toString();
	}

	public void saveToFile() {
		File file = new File(globeFile);
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeInt(programmingList.size());
			for(int i=0;i<programmingList.size();i++)
				oos.writeObject(programmingList.get(i));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if(oos !=null)
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}


}
