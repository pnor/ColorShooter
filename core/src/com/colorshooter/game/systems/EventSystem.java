package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.EventComponent;

import static com.colorshooter.game.Mappers.em;

/**
 * Created by pnore_000 on 8/2/2016.
 */
public class EventSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private Family family;

    public EventSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.all(EventComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        EventComponent event;

        for (Entity e : entities) {
            event = em.get(e);

            if (event.ticking) {
                event.currentTime += dt;
                if (event.currentTime >= event.targetTime) {
                    event.currentTime -= event.targetTime;
                    event.event.event((GameEntity) e, getEngine());

                    if (!event.repeat)
                        event.ticking = false;
                }
            }
        }
    }


}
