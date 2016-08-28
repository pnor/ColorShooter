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
import static com.colorshooter.game.EntityConstructors.generateMovingObject;
import static com.colorshooter.game.Mappers.*;
import static com.colorshooter.game.Mappers.pom;

/**
 * Created by pnore_000 on 8/22/2016.
 */
public class Level7 extends GameScreen implements Screen {

    public Level7() {
        super(7);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(70f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space4"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateEnemySpawnPoint(1000, 900, "RedUFO", 12f, getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100, 900, "UFO", 6f, getEngine());
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "BlueUFO", 20f, getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(500, 0, "GreenUFO", 15f, getEngine());
        GameEntity enemySpawn5 = generateEnemySpawnPoint(250, 0, "GreenUFO", 10f, getEngine());
        GameEntity enemySpawn6 = generateEnemySpawnPoint(750, 0, "UFO", 15f, getEngine());

        GameEntity color1 = generateItemSpawnPoint(450, 550, "Blue", 40f, getEngine());
        em.get(color1).currentTime = 15f;
        GameEntity color2 = generateItemSpawnPoint(150, 350, "Pink", 30f, getEngine());
        em.get(color2).currentTime = 30f;
        GameEntity color3 = generateItemSpawnPoint(600, 600, "Yellow", 40f, getEngine());
        em.get(color2).currentTime = 20f;
        GameEntity color4 = generateItemSpawnPoint(700, 200, "Red", 40f, getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "MaxHealth", 10f, getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(500, 200, "Health", 9f, getEngine());
        GameEntity powerUps3 = generateMovingItemSpawnPoint(900, 900, "SpeedUp", 9f, getEngine());
        GameEntity powerUps4 = generateMovingItemSpawnPoint(300, 600, "SuperShootUp", 8f, getEngine());
        em.get(powerUps4).currentTime = 5.9f;
        GameEntity powerUps5 = generateRandomPowerUp(780, 780, 6.5f, getEngine());

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
        getEngine().addEntity(powerUps4);
        getEngine().addEntity(powerUps5);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(color4);
        getEngine().addEntity(color3);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(enemySpawn5);
        getEngine().addEntity(enemySpawn6);
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
