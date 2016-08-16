package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.components.BouncingComponent;
import com.colorshooter.game.components.FrozenComponent;

import static com.colorshooter.game.Mappers.bm;
import static com.colorshooter.game.Mappers.fm;

/**
 * Created by pnore_000 on 8/8/2016.
 */
public class FrozenSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;

    public FrozenSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.one(FrozenComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        for (Entity e : entities) {
            updateFreeze(fm.get(e), dt);
        }
    }

    public void updateFreeze(FrozenComponent bounce, float dt) {
        if (bounce.isFrozen) {
            bounce.currentDuration += dt;
            if (bounce.currentDuration >= bounce.frozenDuration) {
                bounce.isFrozen = false;
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
