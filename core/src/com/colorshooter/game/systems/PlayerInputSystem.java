package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.colorshooter.game.EntityConstructors;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameEvent;
import com.colorshooter.game.components.*;
import com.colorshooter.game.scenes.tests.GameScreen;

import java.awt.geom.Point2D;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/7/2016.
 */
public class PlayerInputSystem extends EntitySystem{
    private Family family;
    private ImmutableArray<Entity> entities;
    private GameScreen screen;
    private Point2D.Float mouse;

    public PlayerInputSystem(GameScreen s, int priority) {
        super(priority);
        screen = s;
    }

    @Override
    public void addedToEngine(Engine engine) {
        family = Family.all(PlayerInputComponent.class).get();
        entities = engine.getEntitiesFor(family);
        mouse = new Point2D.Float();
    }

    @Override
    public void update(float dt) {
        PlayerInputComponent controller;
        PositionComponent pos;
        MovementComponent mov;
        CollisionComponent col;
        BouncingComponent bounce;
        ShootComponent shoot;
        ImageComponent img;

        for (Entity e : entities) {
            mouse.setLocation(Gdx.input.getX(), fixYCoordinates(Gdx.input.getY()));

            controller = pim.get(e);
            pos = pm.get(e);
            mov = mm.get(e);
            col = cm.get(e);
            bounce = bm.get(e);
            shoot = shm.get(e);
            img = im.get(e);

            updateColor(e);

            if (!bounce.isBouncing && !fm.get(e).isFrozen) {
                PositionSystem.lookAt(pos, mouse.x, mouse.y, col);

                if (Gdx.input.isKeyPressed(controller.forward)) {
                    if (Gdx.input.isKeyPressed(controller.sprint))
                        MovementSystem.moveTowards(pos, mouse.x, mouse.y, mov.speedPerSecond * dt * 1.65f, col);
                    else
                        MovementSystem.moveTowards(pos, mouse.x, mouse.y, mov.speedPerSecond * dt, col);
                } else if (Gdx.input.isKeyPressed(controller.back)) {
                    MovementSystem.moveByRotation(pos, pos.rotation + 180, mov.speedPerSecond * dt / 1.25f, col);
                } else if (Gdx.input.isKeyPressed(controller.right)) {
                    MovementSystem.moveByRotation(pos, pos.rotation - 90, mov.speedPerSecond * dt / 1.25f, col);
                } else if (Gdx.input.isKeyPressed(controller.left)) {
                    MovementSystem.moveByRotation(pos, pos.rotation + 90, mov.speedPerSecond * dt / 1.25f, col);
                }

                if (Gdx.input.isKeyPressed(controller.shoot)) {
                    playerShoot(e);
                }
            } else if (bounce.isBouncing) {
                MovementSystem.moveTowards(pos, mouse.x, mouse.y, -140f * (bounce.bounceDuration - bounce.currentDuration) / bounce.bounceDuration * dt, col);
            }

            if (poim.get(e).isPoisoned) {
                img.texRegion = ImageComponent.atlas.findRegion("PoisonedShip");
                if (!em.has(e)) {
                    e.add(new EventComponent());
                    EventComponent eve = em.get(e);
                    eve.ticking = true;
                    eve.repeat = true;
                    eve.targetTime = 0.3f;
                    eve.event = new GameEvent() {
                        @Override
                        public void event(GameEntity e, Engine engine) {
                            hm.get(e).health -= 2;
                        }
                    };
                }
            } else if (!poim.get(e).isPoisoned && img.texRegion == ImageComponent.atlas.findRegion("PoisonedShip")) {
                e.remove(EventComponent.class);
                colm.get(e).oldColor = 'z';
                updateColor(e);
            }
        }
    }

    private float fixYCoordinates(float y) {
        return screen.getStage().getHeight() - y;
    }

    public Family getFamily() {
        return family;
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    public void updateColor(Entity e) {
        ColorComponent color = colm.get(e);
        if (color.color != color.oldColor) {
            if (color.color == 'x') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShip");
                shm.get(e).attackDelay = 0.2f;
            }
            else if (color.color == 'r') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipRed");
                shm.get(e).attackDelay = 0.8f;
            }
            else if (color.color == 'g') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipGreen");
                shm.get(e).attackDelay = 1f;
            }
            else if (color.color == 'p') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipPink");
                shm.get(e).attackDelay = 0.15f;
            }
            else if (color.color == 'v') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipPurple");
                shm.get(e).attackDelay = 0.4f;
            }
            else if (color.color == 'b') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipBlue");
                shm.get(e).attackDelay = 0.4f;
            }
            else if (color.color == 'y') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipYellow");
                shm.get(e).attackDelay = 0.07f;
            }
        }
        color.oldColor = color.color;
    }

    public void playerShoot(Entity e) {
        ColorComponent color = colm.get(e);
        if (color.color == 'x')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateLaser(0, 0, pm.get(e).rotation, ImageComponent.atlas.findRegion("Laser"), 0), pm.get(e), shm.get(e));
        else if (color.color == 'r')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateExplosionLaser(0, 0, pm.get(e).rotation, 0), pm.get(e), shm.get(e));
        else if (color.color == 'b')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generatePiercingArrow(0, 0, pm.get(e).rotation, 0), pm.get(e), shm.get(e));
        else if (color.color == 'g')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateMine(0, 0, pm.get(e).rotation, 0), pm.get(e), shm.get(e));
        else if (color.color == 'y')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateRicochetLaser(0, 0, pm.get(e).rotation, 0), pm.get(e), shm.get(e));
        else if (color.color == 'v')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateHomingMissile(0, 0, pm.get(e).rotation, 0), pm.get(e), shm.get(e));
        else if (color.color == 'p')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generatePinkLaser(0, 0, pm.get(e).rotation, 0), pm.get(e), shm.get(e));
    }
}
