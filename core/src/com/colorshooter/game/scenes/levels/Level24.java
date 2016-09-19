package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generateMovingObject;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/29/2016.
 */
public class Level24 extends GameScreen{

    public Level24(ColorShooter game) {
        super(24, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(80f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("CubeSpace"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateRandomUFOSpawnPoint(500, -40, 4f, true, getEngine());
        em.get(enemySpawn).currentTime = 2f;
        GameEntity enemySpawn2 = generateRandomUFOSpawnPoint(500,900, 4f, true, getEngine());

        GameEntity powerUps = generateItemSpawnPoint(300, 550, "Health", 8f,  getEngine());
        GameEntity powerUps2 = generateItemSpawnPoint(500, 350, "SpeedUp", 7f,  getEngine());

        GameEntity colors = generateItemSpawnPoint(558, 294, "White", 35f, getEngine());
        em.get(colors).currentTime = 30f;
        GameEntity colors2 = generateItemSpawnPoint(738, 434, "Yellow", 25f, getEngine());
        GameEntity colors3 = generateItemSpawnPoint(618, 694, "Orange", 55f, getEngine());

        GameEntity object1 = generateObject(0, 400, 400, 50, ImageComponent.atlas.findRegion("LongWidthCube"), false);
        pm.get(object1).rotation = 90;
        cm.get(object1).boundingBox.rotate(90);
        GameEntity object2 = generateObject(1000, 400, 400, 50, ImageComponent.atlas.findRegion("LongWidthCube"), false);
        pm.get(object2).rotation = 90;
        cm.get(object2).boundingBox.rotate(90);



        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);

        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);
        getEngine().addEntity(colors3);

        getEngine().addEntity(object1);
        getEngine().addEntity(object2);
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
        return new Level25(getGame());
    }
}
