package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.LifetimeComponent;

import static com.colorshooter.game.Mappers.lfm;


/**
 * Created by pnore_000 on 7/15/2016.
 */
public class LifetimeSystem extends EntitySystem {
    private Family family;
    private ImmutableArray<Entity> entities;

    public LifetimeSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.all(LifetimeComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        LifetimeComponent life;
        for (Entity e : entities) {
            life = lfm.get(e);
            life.currentTime += dt;
            if (life.currentTime >= life.endTime) {
                ((GameEntity) e).setDisposed(true);
            }
        }
    }
}
