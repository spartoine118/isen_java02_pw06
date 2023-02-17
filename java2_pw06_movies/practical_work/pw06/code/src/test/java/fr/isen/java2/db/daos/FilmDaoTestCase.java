package fr.isen.java2.db.daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.isen.java2.db.entities.Film;

public class FilmDaoTestCase {
	
	private GenreDao genreDAO = new GenreDao();
	private FilmDao filmDAO = new FilmDao();
	
	@Before
	public void initDb() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate("DROP TABLE film");
		stmt.executeUpdate("DROP TABLE genre");
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS genre (idgenre INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , name VARCHAR(50) NOT NULL);");
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS film (\r\n"
				+ "  idfilm INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + "  title VARCHAR(100) NOT NULL,\r\n"
				+ "  release_date DATETIME NULL,\r\n" + "  genre_id INT NOT NULL,\r\n" + "  duration INT NULL,\r\n"
				+ "  director VARCHAR(100) NOT NULL,\r\n" + "  summary MEDIUMTEXT NULL,\r\n"
				+ "  CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES genre (idgenre));");
		//stmt.executeUpdate("DELETE FROM film");
		//stmt.executeUpdate("DELETE FROM genre");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (1,'Drama')");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (2,'Comedy')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (1, 'Title 1', '2015-11-26 12:00:00.000', 1, 120, 'director 1', 'summary of the first film')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (2, 'My Title 2', '2015-11-14 12:00:00.000', 2, 114, 'director 2', 'summary of the second film')");
		stmt.executeUpdate("INSERT INTO film(idfilm,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (3, 'Third title', '2015-12-12 12:00:00.000', 2, 176, 'director 3', 'summary of the third film')");
		stmt.close();
		connection.close();
	}
	
	 @Test
	 public void shouldListFilms() {
		 //WHEN
		 List<Film> listOfFilms = filmDAO.listFilms();
		 
		 //THEN
		 assertThat(listOfFilms).hasSize(3);
		 assertThat(listOfFilms).extracting("id","title").containsOnly(tuple(1,"Title 1"), tuple(2,"My Title 2"), tuple(3,"Third title"));
		 
		  
	 }
	
	 @Test
	 public void shouldListFilmsByGenre() {
		//WHEN
		 List<Film> listOfFilms = filmDAO.listFilmsByGenre("Comedy");
		 
		 //THEN
		 assertThat(listOfFilms).hasSize(2);
		 assertThat(listOfFilms).extracting("id","title").containsOnly(tuple(2,"My Title 2"), tuple(3,"Third title"));
	 }
	
	 @Test
	 public void shouldAddFilm() throws Exception {
		 //WHEN
		 //public Film(Integer id, String title, LocalDate releaseDate, Genre genre, Integer duration, String director,String summary) {
		 Film batman = new Film(null, "Batman", LocalDate.now(), genreDAO.getGenre("Drama"), 80, "Paul Walker", "Cool Movie");
		 Film batmanWithId = filmDAO.addFilm(batman);
		 
		 //THEN
		 assertThat(batmanWithId.getId()).isEqualTo(4);
		 assertThat(batmanWithId.getTitle()).contains("Batman");
	 }
	 
}
