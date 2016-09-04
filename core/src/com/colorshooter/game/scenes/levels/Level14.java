package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateMovingItemSpawnPoint;
import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 8/25/2016.
 */
public class Level14 extends GameScreen {
    public Level14(ColorShooter game) {
        super(14, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(80f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("GreenSpace1"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "PoisonWisp", 6f,  getEngine());
        em.get(enemySpawn).currentTime = 64f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0,900, "GreenUFO", 10f,  getEngine());
        em.get(enemySpawn2).currentTime = 6f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "PoisonWisp", 8f,  getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1000, -40, "GreenUFO", 12f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(550, 750, "Blue", 30f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(550, 250, "Red", 60f,  getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 4f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(300, 450, "SuperShootUp", 4f,  getEngine());

        GameEntity object = generateFloatingPoison(300, 300, 40, 40, 90f);
        GameEntity object2 = generateFloatingPoison(700, 700, 40, 40, 90f);
        GameEntity object3 = generateFloatingPoison(800, 300, 40, 40, 90f);
        GameEntity object4 = generateFloatingPoison(200, 500, 40, 40, 90f);
        GameEntity object5 = generateFloatingPoison(600, 900, 40, 40, 90f);
        GameEntity object6 = generateFloatingPoison(1100, 800, 40, 40, 90f);
        GameEntity object7 = generateFloatingPoison(1000, 800, 40, 40, 90f);

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(object);
        getEngine().addEntity(object2);
        getEngine().addEntity(object3);
        getEngine().addEntity(object4);
        getEngine().addEntity(object5);
        getEngine().addEntity(object6);
        getEngine().addEntity(object7);

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
