package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.generateEnemySpawnPoint;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generatePlayer;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/31/2016.
 */
public class BonusLevel4 extends GameScreen {
    public BonusLevel4(ColorShooter game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(25f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("ColorSpace"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "GoldWisp", 3f,  getEngine());
        em.get(enemySpawn).currentTime = 1f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100,900, "GoldWisp", 3.5f,  getEngine());
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "PlatinumWisp", 5f,  getEngine());
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1000, 0, "PlatinumWisp", 6f,  getEngine());

        GameEntity powerUps = generateItemSpawnPoint(450, 400, "DoubleUp", 5f,  getEngine());
        GameEntity powerUps2 = generateItemSpawnPoint(850, 400, "DoubleUp", 5f,  getEngine());
        em.get(powerUps2).currentTime = 2.5f;


        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
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
        return new Level29(getGame());
    }
}
