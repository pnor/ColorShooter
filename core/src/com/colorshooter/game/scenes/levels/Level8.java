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
 * Created by pnore_000 on 8/22/2016.
 */
public class Level8 extends GameScreen {

    public Level8(ColorShooter game) {
        super(8, game);
    }

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space4"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemy = generateRedEnemy(1300, 1000);
        GameEntity enemy2 = generateYellowEnemy(1300, 800);
        GameEntity enemy3 = generateRedEnemy(1300, -400);
        GameEntity enemy4 = generateRedEnemy(1300, -100);

        GameEntity enemySpawn = generateEnemySpawnPoint(1000, 1000, "UFO", 19f, getEngine());
        em.get(enemySpawn).currentTime = 13f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0, 1000, "UFO", 19f, getEngine());
        em.get(enemySpawn2).currentTime = 16f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "GreenUFO", 24f, getEngine());
        em.get(enemySpawn3).currentTime = 16f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(950, 0, "YellowUFO", 20f, getEngine());
        em.get(enemySpawn4).currentTime = 13f;

        GameEntity color1 = generateItemSpawnPoint(350, 650, "Purple", 25f, getEngine());
        em.get(color1).currentTime = 25f;
        GameEntity color2 = generateItemSpawnPoint(650, 350, "Pink", 30f, getEngine());

        GameEntity powerUps = generateItemSpawnPoint(280, 280, "MaxHealth", 10f, getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateItemSpawnPoint(780, 780, "MaxHealth", 12f, getEngine());

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(enemy);
        getEngine().addEntity(enemy2);
        getEngine().addEntity(enemy3);
        getEngine().addEntity(enemy4);


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
        return new Level9(getGame());
    }
}
