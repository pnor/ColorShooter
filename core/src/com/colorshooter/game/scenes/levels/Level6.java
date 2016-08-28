package com.colorshooter.game.scenes.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.tests.GameScreen;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateMovingItemSpawnPoint;
import static com.colorshooter.game.Mappers.*;
import static com.colorshooter.game.Mappers.pom;

/**
 * Created by pnore_000 on 8/21/2016.
 */
public class Level6 extends GameScreen implements Screen {

    public Level6() {
        super(6);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(65f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("BlueSpace1"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "Wisp", 5f,  getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100,900, "Wisp", 6f,  getEngine());
        em.get(enemySpawn2).currentTime = 4f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "BlueUFO", 8f,  getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(500, -40, "BigWisp", 20f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(750, 550, "Blue", 20f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(250, 350, "Green", 30f,  getEngine());
        em.get(color2).currentTime = 25f;
        GameEntity color3 = generateItemSpawnPoint(600, 600, "Yellow", 20f,  getEngine());
        em.get(color2).currentTime = 15f;
        GameEntity color4 = generateItemSpawnPoint(900, 100, "Red", 20f,  getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "MaxHealth", 10f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(300, 450, "Health", 6f,  getEngine());
        GameEntity powerUps3 = generateRandomPowerUp(780, 780, 6.5f,  getEngine());

        GameEntity object1 = generateMovingObject(300, 300, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object2 = generateMovingObject(800, 800, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object3 = generateMovingObject(900, 100, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object4 = generateMovingObject(200, 750, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object5 = generateMovingObject(750, 650, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object6 = generateMovingObject(30, 920, ImageComponent.atlas.findRegion("SpaceJunk2").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk2").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk2"), 100f);

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(color4);
        getEngine().addEntity(color3);
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
