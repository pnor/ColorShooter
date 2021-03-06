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
 * Created by pnore_000 on 8/24/2016.
 */
public class Level11 extends GameScreen {

    public Level11(ColorShooter game) {
        super(11, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer(65f));

        setBackground(ImageComponent.backgroundAtlas.findRegion("GraySpace1"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateEnemySpawnPoint(1450,900, "Wisp", 2f,  getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0,900, "Wisp", 2f,  getEngine());
        em.get(enemySpawn2).currentTime = 1f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "Wisp", 8f,  getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1450, -40, "Wisp", 3f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(750, 550, "Green", 20f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(250, 350, "Yellow", 30f,  getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 7f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(300, 450, "SpeedUp", 6f,  getEngine());

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

    @Override
    public Screen getNextLevel() {
        return new Level12(getGame());
    }
}
