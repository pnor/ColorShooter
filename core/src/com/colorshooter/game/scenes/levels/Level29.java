package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generateMovingObject;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/31/2016.
 */
public class Level29 extends GameScreen{

    private boolean added1;
    private boolean added2;
    private boolean added3;

    public Level29(ColorShooter game) {
        super(29, game);
    }

    @Override
    public void show() {
        super.show();
        added1 = false;
        added2 = false;
        added3 = false;

        setTimer(new GameTimer());
        getTimer().setTime(120f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space1"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(500,900, "Core", 30f, getEngine());
        em.get(enemySpawn).currentTime = 28f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(300, -40, "SwirlWisp", 11f, getEngine());
        GameEntity enemySpawn3 = generateEnemySpawnPoint(600, -40, "SwirlWisp", 12f, getEngine());

        GameEntity powerUps = generateItemSpawnPoint(250, 250, "Health", 16f,  getEngine());
        em.get(powerUps).currentTime = 4f;
        GameEntity powerUps2 = generateItemSpawnPoint(250, 750, "SpeedUp", 16f,  getEngine());
        em.get(powerUps2).currentTime = 8f;
        GameEntity powerUps3 = generateRandomPowerUp(975, 250, 16f,  getEngine());
        em.get(powerUps3).currentTime = 12f;
        GameEntity powerUps4 = generateItemSpawnPoint(975, 750, "DoubleUp", 10f, getEngine());
        GameEntity powerUps5 = generateItemSpawnPoint(700, 200, "SuperShootUp", 24f, getEngine());


        GameEntity colors = generateRandomColorSpawnPoint(600, 450, 20f, getEngine());
        GameEntity colors2 = generateItemSpawnPoint(800, 450, "Orange", 55f, getEngine());

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
        getEngine().addEntity(powerUps4);
        getEngine().addEntity(powerUps5);

        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);

        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!added1 && getTimer().getTime() <= 100f) {
            getEngine().addEntity(generateEnemySpawnPoint(180, 1000, "FinalUFO", 10f, getEngine()));
            getEngine().addEntity(generateEnemySpawnPoint(-100, -100, "FinalUFO", 12f, getEngine()));
            added1 = true;
        }
        if (!added2 && getTimer().getTime() <= 70f) {
            getEngine().addEntity(generateEnemySpawnPoint(400, 1000, "EnemyShipGold", 30f, getEngine()));
            getEngine().addEntity(generateEnemySpawnPoint(-600, 1000, "GhostUFO", 12f, getEngine()));
            added2 = true;
        }
        if (!added3 && getTimer().getTime() <= 50f) {
            getEngine().addEntity(generateRandomEnemySpawnPoint(-100, -100, 12f, getEngine()));
            getEngine().addEntity(generateRandomEnemySpawnPoint(1400, -100, 14f, getEngine()));
            added3 = true;
        }
    }

    @Override
    public void hide() {
        super.hide();
        this.dispose();
    }
}
