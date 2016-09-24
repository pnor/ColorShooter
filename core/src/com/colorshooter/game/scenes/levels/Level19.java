package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/27/2016.
 */
public class Level19 extends GameScreen {

    public Level19(ColorShooter game) {
        super(19, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer(70f));
        setBackground(ImageComponent.backgroundAtlas.findRegion("PinkSpace1"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateRandomUFOSpawnPoint(1000,900, 3.5f, false, getEngine());
        GameEntity enemySpawn2 = generateRandomUFOSpawnPoint(0,0, 6f, false,   getEngine());
        em.get(enemySpawn2).currentTime = 1f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 900, "EnemyShipYellow", 5f, getEngine());
        em.get(enemySpawn3).currentTime = 0.6f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(500, -40, "EnemyShipRed", 5f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(550, 650, "Orange", 30f,  getEngine());
        em.get(color1).currentTime = 28f;
        GameEntity color2 = generateItemSpawnPoint(450, 250, "Orange", 20f,  getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "MaxHealth", 6f,  getEngine());

        getEngine().addEntity(powerUps);
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
        return new Level20(getGame());
    }
}
