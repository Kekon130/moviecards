package com.lauracercas.moviecards.integrationtest.repositories;

import com.lauracercas.moviecards.model.Actor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Autor: Laura Cercas Ramos
 * Proyecto: TFM Integración Continua con GitHub Actions
 * Fecha: 04/06/2024
 */
@DataJpaTest
public class ActorJPAIT {

    @Autowired
    private RestTemplate template;
    private final String actorUrl = "https://moviecards-service-plaza.azurewebsites.net/actors";

    @Test
    public void testSaveActor() {
        Actor actor = new Actor();
        actor.setName("actor");
        actor.setBirthDate(new Date());
        actor.setCountry("spain");

        Actor savedActor = template.postForObject(this.actorUrl, actor, Actor.class);

        assertNotNull(savedActor.getId());

        Actor found = template.getForObject(this.actorUrl + "/" + savedActor.getId(), Actor.class);
        Optional<Actor> foundActor = Optional.ofNullable(found);

        assertTrue(foundActor.isPresent());
        assertEquals(savedActor, foundActor.get());
    }

    @Test
    public void testFindById() {
        Actor actor = new Actor();
        actor.setName("actor");
        actor.setBirthDate(new Date());
        Actor savedActor = template.postForObject(this.actorUrl, actor, Actor.class);

        Actor found = template.getForObject(this.actorUrl + "/" + savedActor.getId(), Actor.class);
        Optional<Actor> foundActor = Optional.ofNullable(found);

        assertTrue(foundActor.isPresent());
        assertEquals(savedActor, foundActor.get());
    }
}
