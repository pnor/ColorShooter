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
public class Level9 extends GameScreen{

    public Level9(ColorShooter game) {
        super(9, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(75f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space1"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "RedUFO", 3f,  getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0,900, "GreenUFO", 3f,  getEngine());
        em.get(enemySpawn2).currentTime = 1f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "BlueUFO", 8f,  getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(500, -40, "YellowUFO", 3f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(750, 550, "Red", 20f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(250, 350, "Pink", 30f,  getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "MaxHealth", 7f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(300, 450, "Health", 6f,  getEngine());
        GameEntity powerUps3 = generateRandomPowerUp(220, 330, 6.5f,  getEngine());

        GameEntity object1 = generateFloatingShock(300, 300, 100, 100, 150f, 2);
        GameEntity object2 = generateFloatingShock(100, 100, 100, 100, 150f, 2);
        GameEntity object3 = generateFloatingShock(800, 800, 100, 100, 150f, 2);
        GameEntity object4 = generateFloatingShock(900, 100, 100, 100, 150f, 2);
        GameEntity object5 = generateFloatingShock(900, 1000, 100, 100, 150f, 2);
        GameEntity object6 = generateFloatingShock(890, 300, 100, 100, 150f, 2);
        GameEntity object7 = generateFloatingShock(130, 670, 100, 100, 150f, 2);

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(object1);
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
