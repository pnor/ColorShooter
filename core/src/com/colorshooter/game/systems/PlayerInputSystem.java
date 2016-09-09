package com.colorshooter.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.colorshooter.game.EntityConstructors;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameEvent;
import com.colorshooter.game.components.*;
import com.colorshooter.game.scenes.GameScreen;

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
            //mouse.setLocation(Gdx.input.getX(), fixYCoordinates(Gdx.input.getY()));
            setMouse();

            controller = pim.get(e);
            pos = pm.get(e);
            mov = mm.get(e);
            col = cm.get(e);
            bounce = bm.get(e);
            shoot = shm.get(e);
            img = im.get(e);

            updateColor(e);

            if ((!bm.has(e) && !fm.has(e)) || !bounce.isBouncing && !fm.get(e).isFrozen) {
                PositionSystem.lookAt(pos, mouse.x, mouse.y, col);

                if (Gdx.input.isKeyPressed(controller.forward) && !checkMouseInsidePlayer(mouse, (GameEntity) e)) {
                    if (Gdx.input.isKeyPressed(controller.sprint))
                        MovementSystem.moveTowards(pos, mouse.x, mouse.y, mov.speedPerSecond * dt * 1.65f, col);
                    else
                        MovementSystem.moveTowards(pos, mouse.x, mouse.y, mov.speedPerSecond * dt, col);
                } else if (!playerOutOfBounds(e)) {
                    if (Gdx.input.isKeyPressed(controller.back)) {
                        MovementSystem.moveByRotation(pos, pos.rotation + 180, mov.speedPerSecond * dt / 1.25f, col);
                    }else if (Gdx.input.isKeyPressed(controller.right)) {
                        MovementSystem.moveByRotation(pos, pos.rotation - 90, mov.speedPerSecond * dt / 1.25f, col);
                    } else if (Gdx.input.isKeyPressed(controller.left)) {
                        MovementSystem.moveByRotation(pos, pos.rotation + 90, mov.speedPerSecond * dt / 1.25f, col);
                    }
                }

                if (Gdx.input.isKeyPressed(controller.shoot)) {
                    playerShoot(e);
                }
            } else if (bm.has(e) && bounce.isBouncing) {
                MovementSystem.moveTowards(pos, mouse.x, mouse.y, -230f * (bounce.bounceDuration - bounce.currentDuration) / bounce.bounceDuration * dt, col);
            }

            if (poim.has(e)) {
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
                    updatePlayerImage(e);
                }
            }
        }
    }

    private void setMouse() {
        Vector2 mousePointer = screen.getStage().getViewport().unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        mouse.setLocation(mousePointer.x, mousePointer.y);
    }

    private boolean playerOutOfBounds(Entity e) {
        PositionComponent pos = pm.get(e);
        return (pos.x < 0 || pos.y < 0 || screen.getStage().getWidth() - pos.x + pos.width < 100 || screen.getStage().getHeight() - pos.y + pos.height < 100);
    }

    public Family getFamily() {
        return family;
    }

    private boolean checkMouseInsidePlayer(Point2D.Float mouse, GameEntity e) {
        PositionComponent pos = pm.get(e);
        return Math.abs(pos.x + pos.originX - mouse.x) <= 5 && Math.abs(pos.y + pos.originY - mouse.y) <= 5;
    }

    public void updateProcessing(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    public void updateColor(Entity e) {
        if (!colm.has(e))
            return;

        ColorComponent color = colm.get(e);
        if (color.color != color.oldColor) {
            if (color.color == 'x') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShip");
                shm.get(e).attackDelay = 0.2f;
            }
            else if (color.color == 'r') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipRed");
                shm.get(e).attackDelay = 0.85f;
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
                shm.get(e).attackDelay = 0.09f;
            }
            else if (color.color == 'o') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipOrange");
                shm.get(e).attackDelay = 2f;
            }
            else if (color.color == 'w') {
                im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipWhite");
                shm.get(e).attackDelay = 0.02f;
            }
        }
        color.oldColor = color.color;
    }

    public static void updatePlayerImage(Entity e) {
        if (!colm.has(e))
            return;

        ColorComponent color = colm.get(e);
        if (color.color == 'x')
            im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShip");
        else if (color.color == 'r')
            im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipRed");
        else if (color.color == 'g')
            im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipGreen");
        else if (color.color == 'p')
            im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipPink");
        else if (color.color == 'v')
            im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipPurple");
        else if (color.color == 'b')
            im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipBlue");
        else if (color.color == 'y')
            im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipYellow");
        else if (color.color == 'o')
            im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipOrange");
        else if (color.color == 'w')
            im.get(e).texRegion = ImageComponent.atlas.findRegion("PlayerShipWhite");
    }

    public void playerShoot(Entity e) {
        ColorComponent color = colm.get(e);
        if (color == null || color.color == 'x')
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
        else if (color.color == 'o')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateOrangeArrowSeed(0, 0, pm.get(e).rotation), pm.get(e), shm.get(e));
        else if (color.color == 'w')
            ShootingSystem.shoot(getEngine(), EntityConstructors.generateWhiteBeam(0, 0, pm.get(e).rotation, 0), pm.get(e), shm.get(e));
    }
}
