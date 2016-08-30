package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generatePushableObject;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/28/2016.
 */
public class Level22 extends GameScreen implements Screen{

    public Level22(ColorShooter game) {
        super(22, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(70f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("CubeSpace"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(500,900, "GhostUFO", 21f, getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(500, -40, "GhostUFO", 21f, getEngine());
        em.get(enemySpawn2).currentTime = 18f;

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 10f,  getEngine());
        GameEntity powerUps2 = generateMovingRandomPowerUp(220, 330, 16f,  getEngine());

        GameEntity colors = generateItemSpawnPoint(618, 394, "Red", 25f, getEngine());
        GameEntity colors2 = generateItemSpawnPoint(738, 434, "Orange", 35f, getEngine());

        GameEntity object1 = generateMovingObject(300, 200, 80, 80, ImageComponent.atlas.findRegion("Cube"), 90f);
        GameEntity object2 = generateMovingObject(300, 700, 80, 80, ImageComponent.atlas.findRegion("Cube"), 90f);
        GameEntity object3 = generateMovingObject(500, 200, 80, 80, ImageComponent.atlas.findRegion("Cube"), 90f);
        GameEntity object4 = generateMovingObject(1000, 400, 80, 80, ImageComponent.atlas.findRegion("Cube"), 90f);

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);

        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);

        getEngine().addEntity(object1);
        getEngine().addEntity(object2);
        getEngine().addEntity(object3);
        getEngine().addEntity(object4);
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
