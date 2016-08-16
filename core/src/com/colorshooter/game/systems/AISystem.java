package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.colorshooter.game.EntityConstructors;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.*;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/3/2016.
 */

/**
 * Processes all entities with an AIComponent so each can run their AI. Has methods for keeping position in correct locations
 * ({@code getClosestPlayer}, and {@code checkPosition}).
 */
public class AISystem extends EntitySystem{
    private ImmutableArray<Entity> entities;
    private Family family = Family.all(AIComponent.class, PositionComponent.class).get();
    private GameEntity[] players;
    private GameScreen screen;

    public AISystem(GameScreen s, GameEntity[] p, int priority) {
        super(priority);
        players = p;
        screen = s;
    }

    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    public void update(float dt) {
        AIComponent ai;
        PositionComponent pos;
        MovementComponent mov;

        for (Entity e : entities) {
            ai = aim.get(e);
            pos = pm.get(e);
            mov = mm.get(e);
            GameEntity closestPlayer = getClosestPlayer(pos);
            PositionComponent playerPos = pm.get(closestPlayer);

            //out of bounds
            if (mm.has(e)) {
                if (pos.x < 10 || pos.y < 10 || screen.getStage().getWidth() - pos.x + pos.width < 100 || screen.getStage().getHeight() - pos.y + pos.height < 100) {
                    float tempRotate = pos.rotation;
                    PositionSystem.lookAt(pos, screen.getStage().getWidth() / 2, screen.getStage().getHeight() / 2, cm.get(e));
                    MovementSystem.moveBy(pos, mov.speedPerSecond * dt, cm.get(e));
                    ai.targetRotation = pos.rotation;
                    pos.rotation = tempRotate;
                }
            }

            //If no more players, stop running ai
            if (playerPos == null)
                return;

            //gradual turning
            if (ai.gradualTurning) {
                if (pos.rotation != ai.targetRotation) {
                    if (Math.abs(ai.targetRotation - pos.rotation) < 0.5f)
                        pos.rotation = ai.targetRotation;
                    pos.rotation += (ai.targetRotation - pos.rotation) * dt * 4;
                }
            } else
                pos.rotation = ai.targetRotation;

            //bouncing
            if (bm.has(e)) {
                if (bm.get(e).isBouncing) {
                    BouncingComponent bounce = bm.get(e);
                    MovementSystem.moveTowards(pos, playerPos.x + playerPos.originX, playerPos.y + playerPos.originY, -80f * (bounce.bounceDuration - bounce.currentDuration) / bounce.bounceDuration * dt, cm.get(e));
                    continue;
                }
            }

            //frozen
            if (fm.has(e)) {
                if (fm.get(e).isFrozen) {
                    continue;
                }
            }

            //poison
            if (poim.has(e) && hm.has(e)) {
                if (poim.get(e).isPoisoned) {
                    if (poim.get(e).currentDuration % 0.40 >= 0.0f && poim.get(e).currentDuration % 0.40 <= 0.3f)
                        hm.get(e).health -= 5f;
                }
            }

            //ai types---
            if (ai.AIType == 'r') {
                ai.currenttime += dt;
                if (ai.currenttime >= ai.targetTime) {
                    ai.targetRotation = (float) Math.random() * 360;
                    if (cm.has(e))
                        CollisionSystem.updateBoundingBox(cm.get(e),0,0, pos.rotation);
                    ai.currenttime = 0.0f;
                }
                //System.out.println("Distance: " + MovementSystem.getDistance(pos, (pm.get(closestPlayer)).x + (pm.get(closestPlayer)).originX, (pm.get(closestPlayer)).y + (pm.get(closestPlayer)).originY) + " Current Awareness: " + ai.awarenessRadius);
                if (MovementSystem.getDistance(pos, (pm.get(closestPlayer)).x + (pm.get(closestPlayer)).originX, (pm.get(closestPlayer)).y + (pm.get(closestPlayer)).originY) <= ai.awarenessRadius) {
                    PositionSystem.lookAt(pos, playerPos.x + playerPos.originX, playerPos.y + playerPos.originY, cm.get(closestPlayer));
                    if (shm.has(e)) {
                        shootProjectile(e);
                    }
                }
            }

            if (ai.AIType == 'a') {
                if (MovementSystem.getDistance(pos, (pm.get(closestPlayer)).x + (pm.get(closestPlayer)).originX, (pm.get(closestPlayer)).y + (pm.get(closestPlayer)).originY) <= ai.awarenessRadius) {
                    PositionSystem.lookAt(pos, playerPos.x + playerPos.originX, playerPos.y + playerPos.originY, cm.get(closestPlayer));
                    if (shm.has(e))
                        shootProjectile(e);
                    pos.rotation += 180;
                } else {
                    PositionSystem.lookAt(pos, playerPos.x + playerPos.originX, playerPos.y + playerPos.originY, cm.get(closestPlayer));
                }
            }

            if (ai.AIType == 'f') {
                PositionSystem.lookAt(pos, playerPos.x + playerPos.originX, playerPos.y + playerPos.originY, cm.get(closestPlayer));
                if (shm.has(e) && MovementSystem.getDistance(pos, (pm.get(closestPlayer)).x + (pm.get(closestPlayer)).originX, (pm.get(closestPlayer)).y + (pm.get(closestPlayer)).originY) <= ai.awarenessRadius) {
                    shootProjectile(e);
                }
            }

            if (ai.AIType == 'e' || ai.AIType == 'l') {
                //System.out.println("Distance: " + MovementSystem.getDistance(pos, (pm.get(closestPlayer)).x + (pm.get(closestPlayer)).originX, (pm.get(closestPlayer)).y + (pm.get(closestPlayer)).originY) + " Current Awareness: " + ai.awarenessRadius);
                if (MovementSystem.getDistance(pos, (pm.get(closestPlayer)).x + (pm.get(closestPlayer)).originX, (pm.get(closestPlayer)).y + (pm.get(closestPlayer)).originY) <= ai.awarenessRadius) {
                    PositionSystem.lookAt(pos, playerPos.x + playerPos.originX, playerPos.y + playerPos.originY, cm.get(closestPlayer));
                    if (shm.has(e))
                        shootProjectile(e);
                    pos.rotation += 180;
                    if (ai.AIType == 'l') {
                        MovementSystem.moveBy(pos, mov.speedPerSecond / 2 * dt, cm.get(e));
                    }
                } else {
                    ai.currenttime += dt;
                    if (ai.currenttime >= ai.targetTime) {
                        ai.targetRotation = (float) Math.random() * 360;
                        if (cm.has(e))
                            CollisionSystem.updateBoundingBox(cm.get(e), 0, 0, pos.rotation);
                        ai.currenttime = 0.0f;
                    }
                }
            }

            if (ai.AIType == 'w') {
                ai.currenttime += dt;
                if (ai.currenttime >= ai.targetTime) {
                    ai.targetRotation = (float) Math.random() * 360;
                    if (cm.has(e))
                        CollisionSystem.updateBoundingBox(cm.get(e), 0, 0, pos.rotation);
                    ai.currenttime = 0.0f;
                }
                if (shm.has(e))
                    shootProjectile(e);
            }
            //---
        }
    }

    public Family getFamily() {
        return family;
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    /**
     * returns the player closest to the passed in {@code PositionComponent}.
     */
    private GameEntity getClosestPlayer(PositionComponent pos) {
        if (players.length == 1)
            return players[0];

        int smallestIndex = 0;
        float smallestDistance = MovementSystem.getDistance(pos, pm.get(players[0]).x +  pm.get(players[0]).originX, pm.get(players[0]).y +  pm.get(players[0]).originY);
        float currentDistance = 0f;
        for (int i = 1; i < players.length; i ++) {
            currentDistance = MovementSystem.getDistance(pos, pm.get(players[i]).x +  pm.get(players[i]).originX, pm.get(players[i]).y +  pm.get(players[i]).originY);
            if (currentDistance < smallestDistance) {
                smallestIndex = i;
                smallestDistance = currentDistance;
            }
        }
        return players[smallestIndex];
    }

    public boolean checkPosition(PositionComponent pos) {
        return (pos.x > screen.getStage().getWidth() || pos.x + pos.width < 0) || (pos.y > screen.getStage().getHeight() || pos.y + pos.height < 0);
    }

    public void shootProjectile(Entity e) {
        if (aim.get(e).projectileType == 'l')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateLaser(0, 0, pm.get(e).rotation, ImageComponent.atlas.findRegion("EnemyLaser"), 1), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'e')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateEnemyExplosionLaser(0, 0, pm.get(e).rotation), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 's')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateSwirl(0, 0, pm.get(e).rotation), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'w')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateWeakLaser(0, 0, pm.get(e).rotation, ImageComponent.atlas.findRegion("EnemyLaser"), 1), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'p')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateEnemyPinkLaser(0, 0, pm.get(e).rotation, 1), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'g')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateGhostLaser(0, 0, pm.get(e).rotation, 1), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'i')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateWispProjectile(0, 0, pm.get(e).rotation, ImageComponent.atlas.findRegion("WispProjectile"), 1), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'h')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateShockWave(0, 0, pm.get(e).rotation, 1), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'm')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateEnemyHomingMissile(0, 0, pm.get(e).rotation), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'b')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateBubbleAttack(0, 0, pm.get(e).rotation), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'f')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateFireLaser(0, 0, pm.get(e).rotation), pm.get(e), shm.get(e));
        else if (aim.get(e).projectileType == 'c')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateIceLaser(0, 0, pm.get(e).rotation, 1), pm.get(e), shm.get(e));
    }
}
