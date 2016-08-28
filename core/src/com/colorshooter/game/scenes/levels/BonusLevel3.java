package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.tests.GameScreen;

import static com.colorshooter.game.EntityConstructors.generateEnemySpawnPoint;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generatePlayer;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/27/2016.
 */
public class BonusLevel3 extends GameScreen implements Screen {
    public BonusLevel3() {
        super();
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(20f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("ColorSpace"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "GoldWisp", 3f,  getEngine());
        em.get(enemySpawn).currentTime = 3f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100,900, "GoldWisp", 3f,  getEngine());
        em.get(enemySpawn2).currentTime = 2f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "GoldWisp", 6f,  getEngine());
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1000, 0, "GoldWisp", 5f,  getEngine());

        GameEntity powerUps = generateItemSpawnPoint(650, 600, "Purple", 20f,  getEngine());
        em.get(powerUps).currentTime = 19f;
        GameEntity powerUps2 = generateItemSpawnPoint(450, 400, "Orange", 20f,  getEngine());
        em.get(powerUps2).currentTime = 19f;
        GameEntity powerUps3 = generateItemSpawnPoint(850, 400, "Green", 20f,  getEngine());
        em.get(powerUps3).currentTime = 19f;


        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
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
