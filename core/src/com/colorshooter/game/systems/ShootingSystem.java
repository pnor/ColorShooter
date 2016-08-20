package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.PositionComponent;
import com.colorshooter.game.components.ShootComponent;

import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.pm;
import static com.colorshooter.game.Mappers.shm;

/**
 * Created by pnore_000 on 7/7/2016.
 */
public class ShootingSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;

    public ShootingSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.one(ShootComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        ShootComponent shoot;
        for (Entity e : entities) {
            shoot = shm.get(e);
            if (shoot.isAttacking) {
                shoot.currentTime += dt;
                if (shoot.currentTime >= shoot.attackDelay) {
                    shoot.isAttacking = false;
                    shoot.currentTime = 0.0f;
                }
            }
        }
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    public static void shoot(Engine engine, GameEntity e, PositionComponent pos, ShootComponent shoot) {
        if (!pm.has(e)) {
            System.out.println("failed!!!");
            return;
        }
        if (shoot.isAttacking) {
            return;
        }
        PositionComponent pos2 = pm.get(e);
        //pos2.x = pos.x + (pos.width * 1.2f) * (float) Math.cos(Math.toRadians(pos.rotation));
        //pos2.y = (pos.y + (pos.originY * 0.8f)) + (pos.height * 1.15f) * (float) Math.sin(Math.toRadians(pos.rotation));

        pos2.x = (pos.x + pos.originX - pos2.originX) + (pos.width + pos2.width / 4) * (float) Math.cos(Math.toRadians(pos.rotation));
        pos2.y = (pos.y + pos.originY - pos2.originY) + (pos.height + pos2.height/ 4) * (float) Math.sin(Math.toRadians(pos.rotation));
        pos2.rotation = pos.rotation;

        if (cm.has(e)) {
            CollisionSystem.setBoundingBoxLocation(cm.get(e), pos2.x, pos2.y, pos2.rotation);
        }
        shoot.isAttacking = true;
        engine.addEntity(e);
    }

    public Family getFamily() {
        return family;
    }


}
