package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.CollisionComponent;
import com.colorshooter.game.components.PositionComponent;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/4/2016.
 */
public class CollisionSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;
    private float deltaTime;

    public CollisionSystem(int priority) {
        super(priority);
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.all(CollisionComponent.class, PositionComponent.class).get();
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void update(float dt) {
        deltaTime = dt;
        for (int i = 0; i < entities.size(); i++) { //i represents thing causing collision
            for (int j = 0; j < entities.size(); j++) {  //j represents thing being collided

                if (entities.get(i) == entities.get(j))
                    continue;

                //---handles collider reactions
                if (cm.get(entities.get(j)).collisionReaction == 9) {
                    if (checkCollision(cm.get(entities.get(i)), cm.get(entities.get(j)))) {
                        if (fm.has(entities.get(i)))
                            fm.get(entities.get(i)).isFrozen = true;
                        ((GameEntity) (entities.get(j))).setDisposed(true);
                    }
                }

                if (cm.get(entities.get(j)).collisionReaction == 11) {
                    if (checkCollision(cm.get(entities.get(i)), cm.get(entities.get(j)))) {
                        if (fm.has(entities.get(i)) && pim.has(entities.get(i)))
                            fm.get(entities.get(i)).isFrozen = true;
                    }
                }

                if (cm.get(entities.get(j)).collisionReaction == 8) {
                    if (checkCollision(cm.get(entities.get(i)), cm.get(entities.get(j)))) {
                        if (poim.has(entities.get(i)) && pim.has(entities.get(i)))
                            poim.get(entities.get(i)).isPoisoned = true;
                    }
                }

                if (cm.get(entities.get(j)).collisionReaction == 6) {
                    if (stopCollision(entities.get(i), entities.get(j))) {
                        if (bm.has(entities.get(i)) && pim.has(entities.get(i)))
                            bm.get(entities.get(i)).isBouncing = true;
                    }
                }

                if (cm.get(entities.get(j)).collisionReaction == 7) {
                    if (checkCollision(cm.get(entities.get(i)), cm.get(entities.get(j)))) {
                        if (bm.has(entities.get(i)) && pim.has(entities.get(i)))
                            bm.get(entities.get(i)).isBouncing = true;
                    }
                }

                if (cm.get(entities.get(j)).collisionReaction == 4) {
                    if (checkCollision(cm.get(entities.get(i)), cm.get(entities.get(j)))) {
                        if (bm.has(entities.get(i)))
                            bm.get(entities.get(i)).isBouncing = true;
                        ((GameEntity) (entities.get(j))).setDisposed(true);
                    }
                }

                if (cm.get(entities.get(i)).collisionReaction == 3) {
                    if (checkCollision(cm.get(entities.get(i)), cm.get(entities.get(j))))
                        pm.get(entities.get(i)).rotation += 165 + (float) (Math.random() * 30);
                }

                if (cm.get(entities.get(j)).collisionReaction == 2) {
                    if (stopCollision(entities.get(i), entities.get(j))) {
                        if (bm.has(entities.get(i)))
                            bm.get(entities.get(i)).isBouncing = true;
                    }
                }

                if (cm.get(entities.get(j)).collisionReaction == 10 && cm.get(entities.get(i)).collisionReaction == 10)
                    stopCollision(entities.get(i), entities.get(j));

                if (cm.get(entities.get(j)).collisionReaction == 1)
                    stopCollision(entities.get(i), entities.get(j));

                //---
                if (cm.get(entities.get(j)).collisionReaction == 5) {
                    if (checkCollision(cm.get(entities.get(i)), cm.get(entities.get(j)))) {
                        ((GameEntity) entities.get(j)).setDisposed(true);
                    }
                }
            }

        }
    }

    public static void updateBoundingBox(CollisionComponent col, float x, float y, float rotation) {
        col.boundingBox.translate(x, y);
        if  (col.rotateBox)
            col.boundingBox.setRotation(rotation);
    }

    public static void setBoundingBoxLocation(CollisionComponent col, float x, float y, float rotation) {
        col.boundingBox.setPosition(x, y);
        if (col.rotateBox)
            col.boundingBox.setRotation(rotation);
    }

    public static boolean checkCollision(CollisionComponent col, CollisionComponent col2) {
        return Intersector.overlapConvexPolygons(col.boundingBox, col2.boundingBox);
    }

    public static boolean checkCollision(float[] verts1, float[] verts2) {
        return Intersector.overlapConvexPolygons(verts1, verts2, new Intersector.MinimumTranslationVector());
    }

    public boolean stopCollision(Entity e, Entity e2) {
        Intersector.MinimumTranslationVector min = new Intersector.MinimumTranslationVector();
        boolean collide = Intersector.overlapConvexPolygons(cm.get(e).boundingBox, cm.get(e2).boundingBox, min);

        if (collide && !cm.get(e).unmovable) {
            MovementSystem.moveNormalized(pm.get(e), min.normal.x, min.normal.y, min.depth, cm.get(e));
        }
        return collide;
    }

    public Family getFamily() {
        return family;
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }
}
