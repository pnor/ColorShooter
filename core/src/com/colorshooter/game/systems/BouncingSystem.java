package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.components.BouncingComponent;
import com.colorshooter.game.components.PositionComponent;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/5/2016.
 */
public class BouncingSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;

    public BouncingSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.one(BouncingComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        for (Entity e : entities) {
             updateBouncing(bm.get(e), dt);
        }
    }

    public void updateBouncing(BouncingComponent bounce, float dt) {
        if (bounce.isBouncing) {
            bounce.currentDuration += dt;
            if (bounce.currentDuration >= bounce.bounceDuration) {
                bounce.isBouncing = false;
                bounce.currentDuration = 0.0f;
            }
        }
    }

    public Family getFamily() {
        return family;
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }
}
