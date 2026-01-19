package com.lauracercas.moviecards.service.movie;

import com.lauracercas.moviecards.model.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
@Service
public class MovieServiceImpl implements MovieService {

    private RestTemplate template;
    private final String movieUrl;

    @Autowired
    public MovieServiceImpl(RestTemplate template) {
        this.template = template;
        this.movieUrl = "https://moviecards-service-plaza.azurewebsites.net/movies";
    }

    @Override
    public List<Movie> getAllMovies() {
        Movie[] movies = template.getForObject(this.movieUrl, Movie[].class);
        List<Movie> movieList = Arrays.asList(movies);
        return movieList;
    }

    @Override
    public Movie save(Movie movie) {
        if (movie.getId() != null && movie.getId() > 0) {
            template.put(this.movieUrl, movie);
        } else {
            movie.setId(0);
            template.postForObject(this.movieUrl, movie, String.class);
        }
        return movie;
    }

    @Override
    public Movie getMovieById(Integer movieId) {
        Movie movie = template.getForObject(this.movieUrl + "/" + movieId, Movie.class);
        return movie;
    }
}
