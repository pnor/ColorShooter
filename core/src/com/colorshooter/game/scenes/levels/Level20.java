package com.colorshooter.game.scenes.levels;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.AIComponent;
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

    private boolean endSpawns;
    private GameTimer spawnTimer;
    private GameEntity enemySpawn, enemySpawn2, enemySpawn3, enemySpawn4;

    private GameEntity powerUps, powerUps2, object1, object2, object3, object4;
    private boolean endLevel;

    public Level20(ColorShooter game) {
        super(20, game);
    }

    @Override
    public void show() {
        super.show();
        spawnTimer = new GameTimer(45f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("GraySpace2"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemy = generateEnemyShipRed(700, 1500);
        GameEntity enemy2 = generateEnemyShipBlue(-400, 450);
        GameEntity enemy3 = generateEnemyShipBlue(1900, 450);
        GameEntity enemy4 = generateEnemyShipBlue(700, -300);

        enemySpawn = generateEnemySpawnPoint(500,900, "EnemyShipRed", 14f, getEngine());
        enemySpawn2 = generateEnemySpawnPoint(500, 0, "EnemyShipBlue", 15f, getEngine());
        em.get(enemySpawn2).currentTime = 4f;
        enemySpawn3 = generateEnemySpawnPoint(1450, 500, "EnemyShipGold", 21f, getEngine());
        em.get(enemySpawn3).currentTime = 17f;
        enemySpawn4 = generateEnemySpawnPoint(0, 500, "EnemyShipYellow", 14f, getEngine());

        powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 11f,  getEngine());
        powerUps2 = generateMovingRandomPowerUp(220, 330, 12f,  getEngine());

        GameEntity colors = generateItemSpawnPoint(608, 414, "Green", 25f, getEngine());
        GameEntity colors2 = generateItemSpawnPoint(708, 414, "Orange", 35f, getEngine());

        GameEntity doubleUp = generateItemSpawnPoint(650, 200, "DoubleUp", 20f,  getEngine());

        object1 = generateMovingObject(300, 300, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        object2 = generateMovingObject(800, 800, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        object3 = generateMovingObject(900, 100, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        object4 = generateMovingObject(200, 750, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);

        getEngine().addEntity(enemy);
        getEngine().addEntity(enemy2);
        getEngine().addEntity(enemy3);
        getEngine().addEntity(enemy4);

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
        spawnTimer.decreaseTimer(delta);

        if (!endSpawns && spawnTimer.getTime() <= 0) {
            getEngine().removeEntity(enemySpawn);
            getEngine().removeEntity(enemySpawn2);
            getEngine().removeEntity(enemySpawn3);
            getEngine().removeEntity(enemySpawn4);
            endSpawns = true;
        }
        if (!endLevel && getEngine().getEntitiesFor(Family.all(AIComponent.class).get()).size() <= 6) {
            getEngine().removeEntity(powerUps);
            getEngine().removeEntity(powerUps2);
            getEngine().removeEntity(object1);
            getEngine().removeEntity(object2);
            getEngine().removeEntity(object3);
            getEngine().removeEntity(object4);
            endLevel = true;
        }
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
