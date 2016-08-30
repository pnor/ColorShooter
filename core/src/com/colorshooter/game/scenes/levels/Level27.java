package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateFloatingPoison;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/30/2016.
 */
public class Level27 extends GameScreen implements Screen {

    public Level27(ColorShooter game) {
        super(27, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(80f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("CubeSpace2"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(-100, -100, "FinalUFO", 18f, getEngine());
        em.get(enemySpawn).currentTime = 16f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(1300, 1000, "FinalUFO", 19f, getEngine());
        GameEntity enemySpawn3 = generateEnemySpawnPoint(1300, 1000, "BigWisp", 13f, getEngine());
        em.get(enemySpawn3).currentTime = 7f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1300, 1000, "BigWisp", 16f, getEngine());



        GameEntity powerUps = generateMovingItemSpawnPoint(300, 550, "MaxHealth", 9f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(500, 350, "ShootUp", 10f,  getEngine());
        GameEntity powerUps3 = generateMovingItemSpawnPoint(300, 550, "Health", 12f,  getEngine());
        em.get(powerUps3).currentTime = 3f;


        GameEntity colors = generateMovingItemSpawnPoint(618, 294, "Pink", 25f, getEngine());
        GameEntity colors2 = generateMovingItemSpawnPoint(738, 434, "Red", 32f, getEngine());
        GameEntity colors3 = generateMovingItemSpawnPoint(618, 694, "Orange", 65f, getEngine());

        GameEntity cube = generateMovingObject(900, -100, 100, 100, ImageComponent.atlas.findRegion("Cube"), 100f);
        GameEntity cube2 = generateMovingObject(-100, -100, 100, 100, ImageComponent.atlas.findRegion("Cube"), 100f);

        GameEntity object = generateFloatingPoison(200, 300, 40, 40, 90f);
        GameEntity object2 = generateFloatingPoison(800, 700, 40, 40, 90f);
        GameEntity object3 = generateFloatingPoison(500, 500, 40, 40, 90f);
        GameEntity object4 = generateFloatingPoison(500, 600, 40, 40, 90f);
        GameEntity object5 = generateFloatingPoison(300, 700, 40, 40, 90f);
        GameEntity object6 = generateFloatingPoison(1200, 800, 40, 40, 90f);
        GameEntity object7 = generateFloatingPoison(1000, 100, 40, 40, 90f);


        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);

        getEngine().addEntity(getPlayer());

        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);

        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);
        getEngine().addEntity(colors3);

        getEngine().addEntity(cube);
        getEngine().addEntity(cube2);

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
