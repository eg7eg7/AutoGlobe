import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Programming implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Set<Projection> movies;
	Set<String> movie_titles;
	//Set of movies for the week
	Map<Integer, Set<Projection>> projections;
	
	//Map<Integer,Map<String,Set<Integer>>> movie_hall_movies;
	//Map key=movie hall, value = Map of(key = movie, value = hours for movie)
	
	
	public Programming() {
		super();
		movies = new HashSet<Projection>();
		//movie_hall_movies = new HashMap<Integer, Map<String,Set<Integer>>>();
		projections = new HashMap<Integer, Set<Projection>>();
		}
	

	
	public void addProjection(Projection p)
	{
		
	}
}
	/*
	public Map<Integer, Map<String, Set<Integer>>> getMovie_hall() {
		return movie_hall_movies;
	}
	public void setMovie_hall(Map<Integer, Map<String, Set<Integer>>> movie_hall) {
		this.movie_hall_movies = movie_hall;
	}
	
	public void addMovie(String name,int hall, int time)
	{
		movies.add(name);
		if(!movie_hall_movies.containsKey(hall))
		{
			movie_hall_movies.put(hall, new HashMap<String,Set<Integer>>());
			movie_hall_movies.get(hall).put(name, new HashSet<Integer>(time));
		}
	}
	*/
