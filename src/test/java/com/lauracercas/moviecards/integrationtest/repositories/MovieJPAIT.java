package com.lauracercas.moviecards.integrationtest.repositories;

import com.lauracercas.moviecards.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
@DataJpaTest
public class MovieJPAIT {

    @Autowired
    private RestTemplate template;
    private final String movieUrl = "https://moviecards-service-plaza.azurewebsites.net/movies";

    @Test
    public void testSaveMovie() {
        Movie movie = new Movie();
        movie.setTitle("Movie");
        movie.setCountry("country");
        movie.setReleaseYear(1995);
        movie.setDuration(190);
        movie.setDirector("Director");
        movie.setGenre("Genre");
        movie.setSinopsis("sinopsis");

        Movie savedMovie = template.postForObject(this.movieUrl, movie, Movie.class);

        assertNotNull(savedMovie.getId());

        Movie found = template.getForObject(this.movieUrl + "/" + savedMovie.getId(), Movie.class);
        Optional<Movie> foundMovie = Optional.ofNullable(found);

        assertTrue(foundMovie.isPresent());
        assertEquals(savedMovie, foundMovie.get());
    }

    @Test
    public void testFindById() {
        Movie movie = new Movie();
        movie.setTitle("movie2");
        Movie savedMovie = template.postForObject(this.movieUrl, movie, Movie.class);

        Movie found = template.getForObject(this.movieUrl + "/" + savedMovie.getId(), Movie.class);
        Optional<Movie> foundMovie = Optional.ofNullable(found);

        assertTrue(foundMovie.isPresent());
        assertEquals(savedMovie, foundMovie.get());
    }
}
