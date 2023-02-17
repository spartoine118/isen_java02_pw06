package fr.isen.java2.db.daos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	public List<Genre> listGenres() {
		List<Genre> listOfGenres = new ArrayList<>();
		
		try(Connection databaseConnection = DataSourceFactory.getDataSource().getConnection()) {
			try(Statement listGenresQuery = databaseConnection.createStatement()){
				try(ResultSet queryResults = listGenresQuery.executeQuery("select * from genre")){
					while(queryResults.next()) {
						Genre genre = new Genre(queryResults.getInt("idgenre"), queryResults.getString("name"));
						listOfGenres.add(genre);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listOfGenres;
	}

	public Genre getGenre(String name) {
		try(Connection databaseConnection = DataSourceFactory.getDataSource().getConnection()) {
			try(PreparedStatement getGenreQuery = databaseConnection.prepareStatement("select * from genre where name=?")){
				getGenreQuery.setString(1, name);
				try(ResultSet queryResults = getGenreQuery.executeQuery()){
					while(queryResults.next()) {
						return new Genre(queryResults.getInt("idgenre"),queryResults.getString("name"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addGenre(String name) {
		try (Connection databaseConnection = DataSourceFactory.getDataSource().getConnection()){
			try(PreparedStatement addGenreQuery = databaseConnection.prepareStatement("insert into genre(name) values(?)", Statement.NO_GENERATED_KEYS)){
				addGenreQuery.setString(1, name);
				addGenreQuery.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
