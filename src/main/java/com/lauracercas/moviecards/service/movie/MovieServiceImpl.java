package com.lauracercas.moviecards.service.movie;

import com.lauracercas.moviecards.model.Movie;
import com.lauracercas.moviecards.repositories.MovieJPA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private RestTemplate template;
    private final String url = "https://moviecards-service-plaza.azurewebsites.net/movies";

    @Override
    public List<Movie> getAllMovies() {
        Movie[] moviesArray = template.getForObject(url, Movie[].class);
        return List.of(moviesArray);
    }

    @Override
    public Movie save(Movie movie) {
        if (movie.getId() != null && movie.getId() > 0) {
            this.template.put(url, movie);
        } else {
            movie.setId(0);
            this.template.postForObject(url, movie, String.class);
        }
        return movie;
    }

    @Override
    public Movie getMovieById(Integer movieId) {
        return template.getForObject(url + "/" + movieId, Movie.class);
    }
}
