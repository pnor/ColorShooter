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
import static com.colorshooter.game.EntityConstructors.generateRandomUFOSpawnPoint;
import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 8/20/2016.
 */
public class Level1 extends GameScreen implements Screen {

    public Level1() {
        super(1);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(50f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space1"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "UFO", 12f,  getEngine());
        em.get(enemySpawn).currentTime = 3f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100,900, "UFO", 12f,  getEngine());
        em.get(enemySpawn2).currentTime = 9f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "UFO", 12f,  getEngine());
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1000, 0, "UFO", 12f,  getEngine());

        GameEntity powerUps = generateItemSpawnPoint(650, 600, "Health", 10f,  getEngine());

        getEngine().addEntity(powerUps);
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
