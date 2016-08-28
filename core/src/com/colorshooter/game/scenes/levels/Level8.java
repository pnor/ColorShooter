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
import static com.colorshooter.game.EntityConstructors.generateMovingItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generateRandomPowerUp;
import static com.colorshooter.game.Mappers.*;
import static com.colorshooter.game.Mappers.pom;

/**
 * Created by pnore_000 on 8/22/2016.
 */
public class Level8 extends GameScreen implements Screen {

    public Level8() {
        super(8);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(70f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space4"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateEnemySpawnPoint(1000, 1000, "UFO", 7f, getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0, 1000, "UFO", 8f, getEngine());
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "GreenUFO", 12f, getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(950, 0, "YellowUFO", 13f, getEngine());
        em.get(enemySpawn4).currentTime = 13f;

        GameEntity color1 = generateItemSpawnPoint(350, 650, "Purple", 25f, getEngine());
        em.get(color1).currentTime = 25f;
        GameEntity color2 = generateItemSpawnPoint(650, 350, "Pink", 30f, getEngine());

        GameEntity powerUps = generateItemSpawnPoint(280, 280, "MaxHealth", 10f, getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateItemSpawnPoint(780, 780, "MaxHealth", 12f, getEngine());

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
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
