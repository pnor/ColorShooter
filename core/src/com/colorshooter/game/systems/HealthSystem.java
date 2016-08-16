package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.colorshooter.game.EntityConstructors;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.HealthComponent;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.components.PositionComponent;

import static com.colorshooter.game.Mappers.hm;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 7/5/2016.
 */
public class HealthSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;
    //private TextureAtlas atlas;

    public HealthSystem(TextureAtlas a, int priority) {
        super(priority);
        //atlas = a;
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = (Family.one(HealthComponent.class).get());
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        HealthComponent health;

        for (Entity e : entities) {
            health = hm.get(e);

            if (health.invincible) {
                health.currentDuration += dt;
                if (health.currentDuration >= health.invinciblityDuration) {
                    health.invincible = false;
                    health.currentDuration = 0.0f;
                }
            }

            checkIfAlive(health);
            if (health.health > health.maxHealth)
                health.health = health.maxHealth;
            if (!health.isAlive) {
                if (health.deathAction == 0) {
                    ((GameEntity) e).setDisposed(true);
                }

                if (health.deathAction == 1) {
                    if (pm.has(e)) {
                        PositionComponent pos = pm.get(e);
                        getEngine().addEntity(EntityConstructors.generateExplosion(pos.originX + pos.x - 36, pos.originY + pos.y - 36));
                    }
                    ((GameEntity) e).setDisposed(true);
                }

                if (health.deathAction == 2) {
                    if (pm.has(e)) {
                        PositionComponent pos = pm.get(e);
                        getEngine().addEntity(EntityConstructors.generateBigExplosion(pos.originX + pos.x - 94, pos.originY + pos.y - 94));
                    }
                    ((GameEntity) e).setDisposed(true);
                }
            }

        }
    }

    public void checkIfAlive(HealthComponent heal) {
        if (heal.health <= 0) {
            heal.isAlive = false;
        }
    }

    public Family getFamily() {
        return family;
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }
}
