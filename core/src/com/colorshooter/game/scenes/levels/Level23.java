package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generateMovingObject;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/28/2016.
 */
public class Level23 extends GameScreen implements Screen {


    public Level23(ColorShooter game) {
        super(23, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(85f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("CubeSpace"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(500,900, "GhostUFO", 15f, getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(500, -40, "EnemyShipGold", 40f, getEngine());
        em.get(enemySpawn2).currentTime = 37f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(500,900, "GhostUFO", 12f, getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 10f,  getEngine());
        GameEntity powerUps2 = generateMovingRandomPowerUp(220, 330, 16f,  getEngine());

        GameEntity colors = generateItemSpawnPoint(618, 394, "Green", 35f, getEngine());
        GameEntity colors2 = generateItemSpawnPoint(738, 434, "Blue", 45f, getEngine());

        GameEntity object1 = generateMovingObject(100, 200, 400, 50, ImageComponent.atlas.findRegion("LongWidthCube"), 150f);
        GameEntity object2 = generateMovingObject(900, 700, 400, 50, ImageComponent.atlas.findRegion("LongWidthCube"), 150f);


        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);

        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);

        getEngine().addEntity(object1);
        getEngine().addEntity(object2);
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
