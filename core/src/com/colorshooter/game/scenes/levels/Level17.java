package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/27/2016.
 */
public class Level17 extends GameScreen{

    public Level17(ColorShooter game) {
        super(17, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer(75f));

        setBackground(ImageComponent.backgroundAtlas.findRegion("RedSpace1"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "EnemyShipRed", 10f, getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0, 0, "EnemyShipRed", 11f, getEngine());
        em.get(enemySpawn2).currentTime = 3f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(1000, 0, "EnemyShipYellow", 16f, getEngine());
        GameEntity enemySpawn4 = generateEnemySpawnPoint(0, 900, "EnemyShipYellow", 20f, getEngine());
        em.get(enemySpawn4).currentTime = 10f;

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 11f,  getEngine());
        GameEntity powerUps2 = generateMovingRandomPowerUp(220, 330, 6f,  getEngine());

        GameEntity doubleUp = generateItemSpawnPoint(520, 330, "DoubleUp", 15f,  getEngine());
        em.get(doubleUp).currentTime = 14f;

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);

        getEngine().addEntity(doubleUp);
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
        return new Level18(getGame());
    }
}
