package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 8/20/2016.
 */
public class Level2 extends GameScreen {

    public Level2(ColorShooter game) {
        super(2, game);
    }

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space1"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemy = generateEnemy(500, 1000);
        GameEntity enemy2 = generateEnemy(500, -100);
        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "UFO", 15f,  getEngine());
        em.get(enemySpawn).currentTime = 12f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0, 0, "UFO", 16f,  getEngine());

        GameEntity powerUps = generateItemSpawnPoint(550, 450, "Health", 8f,  getEngine());
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
        getEngine().addEntity(enemy);
        getEngine().addEntity(enemy2);

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

    @Override
    public Screen getNextLevel() {
        return new Level3(getGame());
    }
}
