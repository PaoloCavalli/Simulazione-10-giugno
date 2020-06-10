package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<String> getGenere(){
		String sql =" SELECT DISTINCT  genre " + 
				"FROM movies_genres " +
				"ORDER BY genre ASC ";
		List<String> genere = new ArrayList<String>();
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				
		     genere.add(res.getString("genre"));
		     
			}
		    conn.close();
		    return genere;
		    
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public List<Actor> getVertici(String genere, Map<Integer,Actor> idMap ){
		String sql="SELECT a1.id, a1.first_name, a1.last_name, a1.gender " + 
				"FROM roles r1,movies m1, movies_genres mg1,actors a1 " + 
				"WHERE r1.movie_id=m1.id  AND a1.id=r1.actor_id  AND m1.id= mg1.movie_id AND mg1.genre=? " + 
				"GROUP BY r1.actor_id";
		List<Actor> attori = new ArrayList<Actor>();
		
		Connection conn = DBConnect.getConnection();
        
		try {
			PreparedStatement st = conn.prepareStatement(sql);
		    st.setString(1, genere);
		    ResultSet res = st.executeQuery();
		    
		    while (res.next()) {
		    	if(!idMap.containsKey(res.getInt("a1.id"))) {
		    		Actor actor = new Actor(res.getInt("a1.id"), res.getString("a1.first_name"), res.getString("a1.last_name"),
							res.getString("a1.gender"));
		    		idMap.put(actor.getId(), actor);
		    		attori.add(actor);
		    	}
		    	else {
		    		attori.add(idMap.get(res.getInt("a1.id")));
		    	}
		    	
		    }
		    conn.close();
		    return attori;
		    
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenze(String genere ,Map<Integer,Actor> idMap ){
		
		String sql="SELECT r1.actor_id, r2.actor_id, COUNT(*) AS peso " + 
				"from roles r1, roles r2, movies m1, movies_genres mg1 " + 
				"WHERE r1.movie_id=r2.movie_id AND r1.actor_id> r2.actor_id AND r1.movie_id=m1.id AND m1.id= mg1.movie_id AND mg1.genre=? " + 
				"GROUP BY r1.actor_id, r2.actor_id";
		List<Adiacenza> adiacenze = new ArrayList<Adiacenza>();

        
		try {
			Connection conn = DBConnect.getConnection();
		
			PreparedStatement st = conn.prepareStatement(sql);
		    st.setString(1, genere);
		    ResultSet res = st.executeQuery();
		    while(res.next()) {
		    	
		    	adiacenze.add(new Adiacenza(idMap.get(res.getInt("r1.actor_id")),
		    			                    idMap.get(res.getInt("r2.actor_id")), res.getInt("peso")));
		    	
		    }
		    conn.close();
		    return adiacenze;
		    
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
}
