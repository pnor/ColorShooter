package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateMovingItemSpawnPoint;
import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 8/25/2016.
 */
public class Level13 extends GameScreen implements Screen {

    public Level13(ColorShooter game) {
        super(13, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(75f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("GraySpace1"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "PoisonWisp", 10f,  getEngine());
        em.get(enemySpawn).currentTime = 5.5f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0,900, "PoisonWisp", 9f,  getEngine());
        em.get(enemySpawn2).currentTime = 6f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "GreenUFO", 11f,  getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1000, -40, "Wisp", 5f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(550, 750, "Blue", 30f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(550, 250, "Purple", 60f,  getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 4f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(300, 450, "SpeedUp", 4f,  getEngine());

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
