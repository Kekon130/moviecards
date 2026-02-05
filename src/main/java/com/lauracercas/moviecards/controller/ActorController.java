package com.lauracercas.moviecards.controller;

import com.lauracercas.moviecards.dict.Dict;
import com.lauracercas.moviecards.model.Actor;
import com.lauracercas.moviecards.model.Movie;
import com.lauracercas.moviecards.service.actor.ActorService;
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
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("actors")
    public String getActorsList(Model model) {
        model.addAttribute(Dict.ACTORS, actorService.getAllActors());
        return Dict.ACTORLIST;
    }

    @GetMapping("actors/new")
    public String newActor(Model model) {
        model.addAttribute(Dict.ACTOR, new Actor());
        model.addAttribute(Dict.TITLE, Messages.NEW_ACTOR_TITLE);
        return Dict.ACTORFORM;
    }

    @PostMapping("saveActor")
    public String saveActor(@ModelAttribute Actor actor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return Dict.ACTORFORM;
        }
        Actor actorSaved = actorService.save(actor);
        if (actor.getId() != null) {
            model.addAttribute("message", Messages.UPDATED_ACTOR_SUCCESS);
        } else {
            model.addAttribute("message", Messages.SAVED_ACTOR_SUCCESS);
        }

        model.addAttribute(Dict.ACTOR, actorSaved);
        model.addAttribute(Dict.TITLE, Messages.EDIT_ACTOR_TITLE);
        return Dict.ACTORFORM;
    }

    @GetMapping("editActor/{actorId}")
    public String editActor(@PathVariable Integer actorId, Model model) {
        Actor actor = actorService.getActorById(actorId);
        List<Movie> movies = actor.getMovies();
        model.addAttribute(Dict.ACTOR, actor);
        model.addAttribute("movies", movies);

        model.addAttribute(Dict.TITLE, Messages.EDIT_ACTOR_TITLE);

        return Dict.ACTORFORM;
    }

}
