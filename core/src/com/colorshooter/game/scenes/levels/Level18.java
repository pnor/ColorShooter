package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.tests.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/27/2016.
 */
public class Level18 extends GameScreen implements Screen{

    public Level18() {
        super(18);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(85f);
        setBackground(ImageComponent.backgroundAtlas.findRegion("RedSpace2"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(0, 0, "EnemyShipGold", 24f, getEngine());
        em.get(enemySpawn).currentTime = 26f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0, 900, "EnemyShipGold", 25f, getEngine());
        em.get(enemySpawn2).currentTime = 10f;

        GameEntity color1 = generateItemSpawnPoint(550, 750, "Blue", 30f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(550, 250, "Red", 30f,  getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 6f,  getEngine());
        GameEntity powerUps2 = generateMovingRandomPowerUp(220, 330, 6f,  getEngine());

        GameEntity doubleUp = generateItemSpawnPoint(720, 400, "DoubleUp", 15f,  getEngine());
        em.get(doubleUp).currentTime = 9f;

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
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
}
