package com.lauracercas.moviecards.service.actor;

import com.lauracercas.moviecards.model.Actor;

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
public class ActorServiceImpl implements ActorService {

    private RestTemplate template;
    private final String actorUrl;

    @Autowired
    public ActorServiceImpl(RestTemplate template) {
        this.template = template;
        this.actorUrl = "https://moviecards-service-plaza.azurewebsites.net/actors";
    }

    @Override
    public List<Actor> getAllActors() {
        Actor[] actors = template.getForObject(this.actorUrl, Actor[].class);
        List<Actor> actorList = Arrays.asList(actors);
        return actorList;
    }

    @Override
    public Actor save(Actor actor) {
        if (actor.getId() != null && actor.getId() > 0) {
            template.put(this.actorUrl, actor);
        } else {
            actor.setId(0);
            template.postForObject(this.actorUrl, actor, String.class);
        }
        return actor;
    }

    @Override
    public Actor getActorById(Integer actorId) {
        Actor actor = template.getForObject(this.actorUrl + "/" + actorId, Actor.class);
        return actor;
    }
}
