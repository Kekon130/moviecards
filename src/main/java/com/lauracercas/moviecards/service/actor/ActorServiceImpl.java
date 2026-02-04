package com.lauracercas.moviecards.service.actor;

import com.lauracercas.moviecards.model.Actor;

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
public class ActorServiceImpl implements ActorService {

    @Autowired
    private RestTemplate template;
    private static final String url = "https://moviecards-service-plaza.azurewebsites.net/actors";

    @Override
    public List<Actor> getAllActors() {
        Actor[] actorsArray = template.getForObject(url, Actor[].class);
        return List.of(actorsArray);
    }

    @Override
    public Actor save(Actor actor) {
        if (actor.getId() != null && actor.getId() > 0) {
            this.template.put(url, actor);
        } else {
            actor.setId(0);
            this.template.postForObject(url, actor, String.class);
        }
        return actor;
    }

    @Override
    public Actor getActorById(Integer actorId) {
        return template.getForObject(url + "/" + actorId, Actor.class);
    }
}
