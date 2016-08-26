package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.tests.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/26/2016.
 */
public class BonusLevel2 extends GameScreen implements Screen {
    public BonusLevel2() {
        super();
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(20f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("Sky"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "PoisonWisp", 5f,  getEngine());
        em.get(enemySpawn).currentTime = 3f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100,900, "GoldWisp", 3f,  getEngine());
        em.get(enemySpawn2).currentTime = 2f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "GoldWisp", 6f,  getEngine());
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1000, 0, "Wisp", 5f,  getEngine());

        GameEntity powerUps = generateItemSpawnPoint(650, 600, "Red", 10f,  getEngine());
        em.get(powerUps).currentTime = 9f;


        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(powerUps);
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
