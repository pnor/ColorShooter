package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.colorshooter.game.components.*;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/2/2016.
 */
public class MovementSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;

    public MovementSystem(int priority) {
        super(priority);
    }

    public void addedToEngine(Engine engine) {
        family = Family.all(PositionComponent.class, MovementComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    public void update(float dt) {
        PositionComponent pos;
        MovementComponent mov;
        CollisionComponent col;
        BouncingComponent bounce;

        for (Entity e : entities) {
            pos = pm.get(e);
            mov = mm.get(e);

            if (mov.move)
                moveBy(pos, mov.speedPerSecond * dt, cm.get(e));

            //Bouncing Movement
            if (bm.has(e)) {
                if (pm.has(e) && mm.has(e)) {
                    col = cm.get(e);
                    bounce = bm.get(e);

                    if (bounce.isBouncing) {
                        pos.rotation += 20 * ((bounce.bounceDuration - bounce.currentDuration) / bounce.bounceDuration);
                        moveByRotation(pos, pos.rotation, 40f * dt, col);
                    }
                }
            }
        }
    }

    /**
     * Moves a {@code PositionComponent} by the amount of x and y.
     * @param pos {@code PositionComponent}
     * @param x amount to move
     * @param y amount to move
     * @param col {@code CollisionComponent}
     */
    public static void moveBy(PositionComponent pos, float x, float y, CollisionComponent col){
        pos.x += x;
        pos.y += y;
        if (col != null) {
            CollisionSystem.updateBoundingBox(col, x, y, pos.rotation);
        }
    }

    /**
     * Moves a {@code PositionComponent} by a speed value and the {@code PositionComponent}'s rotation
     * @param pos {@code PositionComponent}
     * @param speed speed
     * @param col {@code CollisionComponent}
     */
    public static void moveBy(PositionComponent pos, float speed, CollisionComponent col) {
        float theta = (float) Math.toRadians(pos.rotation);
        moveNormalized(pos, (float) Math.cos(theta), (float) Math.sin(theta), speed, col);
    }

    /**
     * Moves a {@code PositionComponent} by a rotation and speed
     * @param pos {@code PositionComponent}
     * @param rotation rotation to move by
     * @param speed speed
     * @param col {@code CollisionComponent}
     */
    public static void moveByRotation(PositionComponent pos, float rotation, float speed, CollisionComponent col) {
        float theta = (float) Math.toRadians(rotation);
        moveNormalized(pos, (float) Math.cos(theta), (float) Math.sin(theta), speed, col);
    }

    /**
     * Moves a {@code PositionComponent} to a set point by a speed value
     * @param pos {@code PositionComponent}
     * @param x x-coordinate
     * @param y y-coordinate
     * @param speed speed
     * @param col {@code CollisionComponent}
     */
    public static void moveTowards(PositionComponent pos, float x,  float y, float speed, CollisionComponent col) {
        float theta = (float) Math.atan2(y - (pos.y + pos.originY), x - (pos.x + pos.originX));
        moveNormalized(pos, (float) Math.cos(theta), (float) Math.sin(theta), speed, col);
    }

    public static void moveNormalized(PositionComponent pos, float x, float y, float speed, CollisionComponent col) {
        Vector2 normal = new Vector2(x, y);
        normal.nor();
        normal.scl(speed);

        moveBy(pos, normal.x, normal.y, col);
    }

    /**
     * Gets the distance between a {@code PositionComponent} and a point
     * @param pos {@code PositionComponent}
     * @param x x-coordinate
     * @param y y-coordinate
     * @return the distance
     */
    public static float getDistance(PositionComponent pos, float x, float y) {
        return (float) Math.sqrt(Math.pow(x - (pos.x + pos.originX), 2) + Math.pow(y - (pos.y + pos.originY), 2));
    }

    public Family getFamily() {
        return family;
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }
}
