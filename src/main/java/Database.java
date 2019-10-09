import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Database {
	private static ArrayList<Programming> programmingList = new ArrayList<Programming>();
	private static String globeFile = "weeklyprog.whis";

	public Database() {
	}

	public void fillDatabaseFromFile() {
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
			System.out.println("Data file cannot be found");
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
			programmingList.sort(new ProgrammingCompare().reversed());
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<programmingList.size();i++)
		{
			Programming p = programmingList.get(i);
			sb.append(p.getProgram_date() + " " + p.getTitle() + "\n" + p.getNum_projections() + " Projections\n" + p.getFilename() + " sheet " + p.getSheet_num() + "\n\n");
		}
		return sb.toString();
	}

	public String printPrettyProgram(int i)
	{
		if(i > programmingList.size())
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append(programmingList.get(i).toString());
		sb.append(showNewMovies(i));
		sb.append(showExpiredMovies(i));
		sb.append("\n\n\n\nAlways verify the summary against the original programming file before use");
		return sb.toString();
	}

	public String printPrettyProgram(Programming p) {
		for(int i=0;i<programmingList.size();i++)
			if(p.equals(programmingList.get(i)))
				return printPrettyProgram(i);
		return "";
	}

	public String showExpiredMovies(int i) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n-------------REMOVED MOVIES-------------\n");
		if(i-1 < 0)
		{

			sb.append("No expired movies");
			return sb.toString();
		}
		else if (i>programmingList.size()-1)
		{
			sb.append("Invalid programming value");
			return sb.toString();
		}
		Set<String> old_movies = programmingList.get(i-1).getMovie_titles();
		Set<String> new_movies = programmingList.get(i).getMovie_titles();
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
		int count = 1;
		for(String s : removed_movies)
			sb.append(count++ + ". " + s + "\n");
		return sb.toString();
	}

	public String showNewMovies(int i) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n-------------NEW MOVIES-------------\n");
		if(i-1 < 0)
		{

			sb.append("No new movies");
			return sb.toString();
		}
		else if (i>programmingList.size()-1)
		{
			sb.append("Invalid programming value");
			return sb.toString();
		}
		Set<String> old_movies = programmingList.get(i-1).getMovie_titles();
		Set<String> new_movies = programmingList.get(i).getMovie_titles();

		Whiskers.log("checking new movies from " + programmingList.get(i-1).getProgram_date() +" to " +programmingList.get(i).getProgram_date());
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
		int count = 1;
		for(String s : brand_new)
			sb.append(count++ + ". " + s + "\n");
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

	public ObservableList<String> getProgramList() {
		ObservableList<String> list = FXCollections.observableArrayList();
		for(int i=0;i<programmingList.size();i++)
		{
			list.add(i, programmingList.get(i).getKey());
		}
		return list;
	}

	public Programming getProgramming(String key) {
		for(Programming p:programmingList)
			if(p.getKey().equals(key))
				return p;
		return null;
	}

	public void removeProgram(String key) {
		Programming t=null;
		for(Programming p:programmingList)
		{
			if(p.getKey().equals(key))
			{
				t=p;
				break;
			}
		}
		programmingList.remove(t);

	}




}
