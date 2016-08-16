package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.components.PoisonComponent;

import static com.colorshooter.game.Mappers.poim;

/**
 * Created by pnore_000 on 8/7/2016.
 */
public class PoisonSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;

    public PoisonSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.one(PoisonComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        for (Entity e : entities) {
            updatePoison(poim.get(e), dt);
        }
    }

    public void updatePoison(PoisonComponent poi, float dt) {
        if (poi.isPoisoned) {
            poi.currentDuration += dt;
            if (poi.currentDuration >= poi.poisonDuration) {
                poi.isPoisoned = false;
                poi.currentDuration = 0.0f;
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
