package com.colorshooter.game.scenes.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.tests.GameScreen;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.Mappers.*;
import static com.colorshooter.game.Mappers.pom;

/**
 * Created by pnore_000 on 8/20/2016.
 */
public class Level2 extends GameScreen implements Screen {

    public Level2() {
        super(2);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(70f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space1"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "UFO", 12f,  getEngine());
        em.get(enemySpawn).currentTime = 3f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100,900, "UFO", 6f,  getEngine());
        em.get(enemySpawn2).currentTime = 4f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "UFO", 12f,  getEngine());
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1000, 0, "UFO", 6f,  getEngine());

        GameEntity powerUps = generateItemSpawnPoint(550, 450, "Health", 10f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateRandomPowerUp(300, 450, 15f,  getEngine());
        em.get(powerUps2).currentTime = 7f;
        GameEntity powerUps3 = generateRandomPowerUp(750, 450, 20f,  getEngine());
        em.get(powerUps3).currentTime = 2f;
        GameEntity powerUps4 = generateRandomPowerUp(900, 450, 15f,  getEngine());

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
        getEngine().addEntity(powerUps4);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

    }

    @Override
    public void hide() {
        super.hide();
        this.dispose();
    }
}
