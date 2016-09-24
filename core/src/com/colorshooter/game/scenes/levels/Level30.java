package com.colorshooter.game.scenes.levels;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameEvent;
import com.colorshooter.game.components.*;
import com.colorshooter.game.scenes.GameScreen;
import com.colorshooter.game.scenes.menus.EnterScoreMenu;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateRandomColorSpawnPoint;
import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 9/1/2016.
 */
public class Level30 extends GameScreen{
    private boolean phase2;
    private GameEntity boss;

    public Level30(ColorShooter game) {
        super(30, game);
    }

    @Override
    public void show() {
        super.show();
        phase2 = false;

        setBackground(ImageComponent.backgroundAtlas.findRegion("TurretSpace"));

        setPlayer(generatePlayer(28, 414));
        //colm.get(getPlayer()).color = 'w';

        boss = generateBossTurret(1300, 360);
        shm.get(boss).isAttacking = true;
        shm.get(boss).currentTime = -0.6f;
        GameEntity sub1 = generateTurret(1300, 560);
        GameEntity sub2 = generateTurret(1300, 260);

        GameEntity sub3 = generateTurret(1200, 500);
        GameEntity sub4 = generateTurret(1200, 300);
        GameEntity sub5 = generateYellowTurret(1240, 700);
        GameEntity sub6 = generateYellowTurret(1240, 100);

        GameEntity sub7 = generateYellowTurret(1100, 200);
        GameEntity sub8 = generateYellowTurret(1100, 600);

        GameEntity sub9 = generateYellowTurret(850, 510);
        GameEntity sub10 = generateYellowTurret(850, 290);

        GameEntity sub11 = generateBlueTurret(960, 660);
        shm.get(sub11).isAttacking = true;
        shm.get(sub11).currentTime = -1f;
        GameEntity sub12 = generateTurret(1000, 560);
        GameEntity sub13 = generateBlueTurret(880, 357);
        shm.get(sub13).isAttacking = true;
        shm.get(sub13).currentTime = -1.7f;
        GameEntity sub14 = generateTurret(1000, 260);
        GameEntity sub15 = generateBlueTurret(960, 40);
        shm.get(sub15).isAttacking = true;
        shm.get(sub15).currentTime = -2.4f;

        GameEntity sub16 = generateTurret(1100, 450);
        GameEntity sub17 = generateTurret(1100, 350);

        GameEntity sub18 = generateTurret(1100, 800);
        GameEntity sub19 = generateTurret(1100, 10);


        GameEntity podSpawn = generateEnemySpawnPoint(-100, -100, "PodShip", 30f, getEngine());
        GameEntity podSpawn2 = generateEnemySpawnPoint(-100, 1000, "PodShip", 30f, getEngine());

        GameEntity powerUps = generateItemSpawnPoint(200, 200, "DoubleUp", 10f,  getEngine());
        GameEntity powerUps2 = generateItemSpawnPoint(500, 700, "Health", 7f,  getEngine());
        GameEntity powerUps3 = generateItemSpawnPoint(200, 700, "MaxHealth", 12f,  getEngine());
        GameEntity powerUps4 = generateRandomPowerUp(500, 200, 6f,  getEngine());

        GameEntity colors = generateRandomColorSpawnPoint(312, 800, 8f,  getEngine());
        GameEntity colors2 = generateRandomColorSpawnPoint(312, 100, 8f,  getEngine());


        getEngine().addEntity(boss);

        getEngine().addEntity(sub1);
        getEngine().addEntity(sub2);

        getEngine().addEntity(sub3);
        getEngine().addEntity(sub4);
        getEngine().addEntity(sub5);
        getEngine().addEntity(sub6);

        getEngine().addEntity(sub7);
        getEngine().addEntity(sub8);
        getEngine().addEntity(sub9);
        getEngine().addEntity(sub10);
        getEngine().addEntity(sub11);
        getEngine().addEntity(sub12);
        getEngine().addEntity(sub13);

        getEngine().addEntity(sub14);
        getEngine().addEntity(sub15);

        getEngine().addEntity(sub16);
        getEngine().addEntity(sub17);

        getEngine().addEntity(sub18);
        getEngine().addEntity(sub19);

        getEngine().addEntity(podSpawn);
        getEngine().addEntity(podSpawn2);

        getEngine().addEntity(getPlayer());
        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
        getEngine().addEntity(powerUps4);
        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!phase2 && ((!boss.getDisposed() && getEngine().getEntitiesFor(Family.all(AIComponent.class).get()).size() <= 3) || (!boss.getDisposed() && hm.get(boss).health < 2000))) {
            phase2 = true;
            getEngine().addEntity(generateItemSpawnPoint(100, 550, "White",25f, getEngine()));

            aim.get(boss).projectileType = '1';
            am.get(boss).animationTime = 0.09f;
            EventComponent ev = em.get(boss);
            ev.currentTime = 4f;
            ev.targetTime = 4f;
            ev.event = new GameEvent() {
                @Override
                public void event(GameEntity e, Engine engine) {
                    PositionComponent pos = pm.get(e);

                    int choice = (int) (Math.random() * 5);

                    if (choice == 0) {
                        aim.get(e).awarenessRadius = 1600;
                        shm.get(e).attackDelay = 0.6f;
                        aim.get(e).projectileType = '2';
                    } else if (choice == 1) {
                        aim.get(e).awarenessRadius = 1600;
                        shm.get(e).attackDelay = 0.25f;
                        aim.get(e).projectileType = '3';
                    } else if (choice == 2) {
                        shm.get(e).attackDelay = 0.05f;
                        aim.get(e).projectileType = '4';
                        aim.get(e).awarenessRadius = 1000;
                    } else if (choice == 3) {
                        aim.get(e).awarenessRadius = 1600;
                        shm.get(e).attackDelay = 0.65f;
                        aim.get(e).projectileType = '1';
                        for (int i = 0; i < 9; i++) {
                            engine.addEntity(generateBlueBeam(pos.x + pos.originX, pos.y + pos.originY, pos.rotation - 45 + i * 10, 1));
                        }
                    }
                }
            };

        }
    }

    @Override
    public void hide() {
        super.hide();
        this.dispose();
    }

    @Override
    public Screen getNextLevel() {
        return new EnterScoreMenu(getLevel(), getGame());
    }

}
