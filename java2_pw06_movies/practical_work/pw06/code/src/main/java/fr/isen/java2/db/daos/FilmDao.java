package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;

public class FilmDao {

	public List<Film> listFilms() {
		List<Film> listOfFilms = new ArrayList<>();
		
		try(Connection databaseConnection = DataSourceFactory.getDataSource().getConnection()) {
			try(Statement listFilmsQuery = databaseConnection.createStatement()){
				try(ResultSet queryResults = listFilmsQuery.executeQuery("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre")){
					while(queryResults.next()) {
						
						//initialize values into variables
						int idfilm = queryResults.getInt("idfilm");
						String title = queryResults.getString("title");
						LocalDate release_date = queryResults.getDate("release_date").toLocalDate();
						Genre genre = new Genre(queryResults.getInt("idgenre"), queryResults.getString("name"));
						int duration = queryResults.getInt("duration");
						String director_name = queryResults.getString("director");
						String summary = queryResults.getString("summary");
						
						//create new film object and add it to the list
						Film film = new Film(idfilm, title, release_date, genre, duration, director_name, summary);
						listOfFilms.add(film);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfFilms;
	}

	public List<Film> listFilmsByGenre(String genreName) {
		List<Film> listOfFilms = new ArrayList<>();
		
		try(Connection databaseConnection = DataSourceFactory.getDataSource().getConnection()) {
			try(PreparedStatement listFilmsQuery = databaseConnection.prepareStatement("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name =?")){
				listFilmsQuery.setString(1, genreName);
				try(ResultSet queryResults = listFilmsQuery.executeQuery()){
					while(queryResults.next()) {
						
						//initialize values into variables
						int idfilm = queryResults.getInt("idfilm");
						String title = queryResults.getString("title");
						LocalDate release_date = queryResults.getDate("release_date").toLocalDate();
						Genre genre = new Genre(queryResults.getInt("idgenre"), queryResults.getString("name"));
						int duration = queryResults.getInt("duration");
						String director_name = queryResults.getString("director");
						String summary = queryResults.getString("summary");
						
						//create new film object and add it to the list
						Film film = new Film(idfilm, title, release_date, genre, duration, director_name, summary);
						listOfFilms.add(film);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfFilms;
	}

	public Film addFilm(Film film) {
		try (Connection databaseConnection = DataSourceFactory.getDataSource().getConnection()){
			try(PreparedStatement addFilmQuery = databaseConnection.prepareStatement("INSERT INTO film(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
				
				//set parameters into query
				addFilmQuery.setString(1, film.getTitle());
				addFilmQuery.setDate(2, Date.valueOf(film.getReleaseDate()));
				addFilmQuery.setInt(3, film.getGenre().getId());
				addFilmQuery.setInt(4, film.getDuration());
				addFilmQuery.setString(5, film.getDirector());
				addFilmQuery.setString(6, film.getSummary());
				
				//execute query
				addFilmQuery.executeUpdate();
				
				try(ResultSet queryResults = addFilmQuery.getGeneratedKeys()){
					if(queryResults.next()) {
						film.setId(queryResults.getInt(1));
						return film;
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
