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
public class Level20 extends GameScreen{

    public Level20(ColorShooter game) {
        super(20, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(85f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("RedSpace1"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(500,900, "EnemyShipRed", 8f, getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(500, 0, "EnemyShipBlue", 9f, getEngine());
        em.get(enemySpawn2).currentTime = 8f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(1210, 500, "EnemyShipGold", 17f, getEngine());
        GameEntity enemySpawn4 = generateEnemySpawnPoint(0, 500, "EnemyShipYellow", 14f, getEngine());
        em.get(enemySpawn4).currentTime = 10f;

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 8f,  getEngine());
        GameEntity powerUps2 = generateMovingRandomPowerUp(220, 330, 12f,  getEngine());

        GameEntity colors = generateItemSpawnPoint(608, 414, "Green", 25f, getEngine());
        GameEntity colors2 = generateItemSpawnPoint(708, 414, "Orange", 35f, getEngine());

        GameEntity doubleUp = generateItemSpawnPoint(650, 200, "DoubleUp", 20f,  getEngine());

        GameEntity object1 = generateObject(400, 200, 120, 120, ImageComponent.atlas.findRegion("Asteroid"), true);
        GameEntity object2 = generateObject(300, 600, 120, 120, ImageComponent.atlas.findRegion("Asteroid2"), true);
        GameEntity object3 = generateObject(900, 200, 120, 120, ImageComponent.atlas.findRegion("Asteroid"), true);
        GameEntity object4 = generateObject(1000, 600, 120, 120, ImageComponent.atlas.findRegion("Asteroid2"), true);

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);

        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);
        getEngine().addEntity(doubleUp);

        getEngine().addEntity(object1);
        getEngine().addEntity(object2);
        getEngine().addEntity(object3);
        getEngine().addEntity(object4);
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
        return new BonusLevel3(getGame());
    }
}
