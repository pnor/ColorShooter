package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateObject;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/30/2016.
 */
public class Level25 extends GameScreen implements Screen{

    public Level25(ColorShooter game) {
        super(25, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(80f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("CubeSpace"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateRandomWispSpawnPoint(500, -40, 3.5f, true, getEngine());
        em.get(enemySpawn).currentTime = 2f;
        GameEntity enemySpawn2 = generateRandomWispSpawnPoint(0, 900, 4.5f, true, getEngine());
        GameEntity enemySpawn3 = generateEnemySpawnPoint(1300, 900, "GhostUFO", 7.5f, getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 550, "MaxHealth", 9f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(500, 350, "DoubleUp", 7f,  getEngine());
        GameEntity powerUps3 = generateMovingItemSpawnPoint(300, 550, "Health", 8f,  getEngine());
        em.get(powerUps3).currentTime = 3f;


        GameEntity colors = generateMovingItemSpawnPoint(618, 294, "Red", 35f, getEngine());
        GameEntity colors2 = generateMovingItemSpawnPoint(738, 434, "Yellow", 25f, getEngine());
        GameEntity colors3 = generateMovingItemSpawnPoint(618, 694, "Blue", 45f, getEngine());

        GameEntity object1 = generateObject(200, 100, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);
        GameEntity object2 = generateObject(500, 100, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);
        GameEntity object3 = generateObject(800, 100, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);
        GameEntity object4 = generateObject(1100, 100, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);

        GameEntity object5 = generateObject(200, 400, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);
        GameEntity object6 = generateObject(500, 400, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);
        GameEntity object7 = generateObject(800, 400, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);
        GameEntity object8 = generateObject(1100, 400, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);

        GameEntity object9 = generateObject(200, 700, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);
        GameEntity object10 = generateObject(500, 700, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);
        GameEntity object11 = generateObject(800, 700, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);
        GameEntity object12 = generateObject(1100, 700, 100, 100, ImageComponent.atlas.findRegion("Cube"), false);


        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);

        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);


        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);
        getEngine().addEntity(colors3);

        getEngine().addEntity(object1);
        getEngine().addEntity(object2);
        getEngine().addEntity(object3);
        getEngine().addEntity(object4);
        getEngine().addEntity(object5);
        getEngine().addEntity(object6);
        getEngine().addEntity(object7);
        getEngine().addEntity(object8);
        getEngine().addEntity(object9);
        getEngine().addEntity(object10);
        getEngine().addEntity(object11);
        getEngine().addEntity(object12);
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
