package com.lauracercas.moviecards.controller;

import com.lauracercas.moviecards.dict.Dict;
import com.lauracercas.moviecards.model.Actor;
import com.lauracercas.moviecards.model.Movie;
import com.lauracercas.moviecards.service.movie.MovieService;
import com.lauracercas.moviecards.util.Messages;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
@Controller
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("movies")
    public String getMoviesList(Model model) {
        model.addAttribute(Dict.MOVIES, movieService.getAllMovies());
        return Dict.MOVIELIST;
    }

    @GetMapping("movies/new")
    public String newMovie(Model model) {
        model.addAttribute(Dict.MOVIE, new Movie());
        model.addAttribute(Dict.TITLE, Messages.NEW_MOVIE_TITLE);
        return Dict.MOVIEFORM;
    }

    @PostMapping("saveMovie")
    public String saveMovie(@ModelAttribute Movie movie, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return Dict.MOVIEFORM;
        }
        Movie movieSaved = movieService.save(movie);
        if (movieSaved.getId() != null) {
            model.addAttribute("message", Messages.UPDATED_MOVIE_SUCCESS);
        } else {
            model.addAttribute("message", Messages.SAVED_MOVIE_SUCCESS);
        }

        model.addAttribute(Dict.MOVIE, movieSaved);
        model.addAttribute(Dict.TITLE, Messages.EDIT_MOVIE_TITLE);
        return Dict.MOVIEFORM;
    }

    @GetMapping("editMovie/{movieId}")
    public String editMovie(@PathVariable Integer movieId, Model model) {
        Movie movie = movieService.getMovieById(movieId);
        List<Actor> actors = movie.getActors();
        model.addAttribute(Dict.MOVIE, movie);
        model.addAttribute(Dict.ACTORS, actors);

        model.addAttribute(Dict.TITLE, Messages.EDIT_MOVIE_TITLE);

        return Dict.MOVIEFORM;
    }

}
