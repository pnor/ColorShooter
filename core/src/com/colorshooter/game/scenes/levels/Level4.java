package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generateRandomPowerUp;
import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 8/20/2016.
 */
public class Level4 extends GameScreen {

    public Level4(ColorShooter game) {
        super(4, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(65f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space2"));

        setPlayer(generatePlayer(678, 414));

        GameEntity object1 = generateMovingObject(300, 300, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object2 = generateMovingObject(800, 800, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "Wisp", 7f,  getEngine());
        em.get(enemySpawn).currentTime = 6f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100,900, "RedUFO", 13f,  getEngine());
        em.get(enemySpawn2).currentTime = 6f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "UFO", 5.5f,  getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1000, 0, "Wisp", 9f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(750, 550, "Red", 20f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(250, 350, "Blue", 20f,  getEngine());
        em.get(color2).currentTime = 5f;

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 6f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateRandomPowerUp(900, 450, 10f,  getEngine());

        getEngine().addEntity(object1);
        getEngine().addEntity(object2);
        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
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
