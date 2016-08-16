package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.components.BouncingComponent;
import com.colorshooter.game.components.DamageComponent;
import com.colorshooter.game.components.HealthComponent;
import com.colorshooter.game.components.PositionComponent;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/7/2016.
 */
public class DamageSystem extends EntitySystem{
    private Family healthFam;
    private Family damageFam;
    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> damagers;

    public DamageSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        healthFam = Family.all(HealthComponent.class, PositionComponent.class).get();
        damageFam = Family.all(DamageComponent.class, PositionComponent.class).get();
        entities = engine.getEntitiesFor(healthFam);
        damagers = engine.getEntitiesFor(damageFam);
    }

    @Override
    public void update(float dt) {
        HealthComponent health;
        DamageComponent dam;
        PositionComponent pos;
        PositionComponent pos2;
        boolean collide;

        for (Entity e : entities) {
            for (Entity e2 : damagers) {
                if (e == e2)
                    continue;

                //check if they collided
                collide = false;
                pos = pm.get(e);
                pos2 = pm.get(e2);

                if (cm.has(e) && cm.has(e2)) {
                    collide = CollisionSystem.checkCollision(cm.get(e), cm.get(e2));
                } else {
                    collide = CollisionSystem.checkCollision(
                            new float[] {
                                    pos.x, pos.y,
                                    pos.x + pos.width, pos.y,
                                    pos.x + pos.width, pos.y + pos.height,
                                    pos.x, pos.y + pos.height },
                            new float[] {
                                    pos2.x, pos2.y,
                                    pos2.x + pos2.width, pos2.y,
                                    pos2.x + pos2.width, pos2.y + pos2.height,
                                    pos2.x, pos2.y + pos2.height} );
                }
                //deal damage
                if (collide) {
                    health = hm.get(e);
                    dam = dm.get(e2);

                    if ((health.tag == dam.tag || dam.tag == 'n') && !health.invincible) {
                        health.health -= dam.damage;
                        health.invincible = true;
                    }
                }
            }
        }
    }

    public Family getFamily() {
        return Family.one(DamageComponent.class, PositionComponent.class).get();
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(healthFam);
        damagers = engine.getEntitiesFor(damageFam);
    }
}
