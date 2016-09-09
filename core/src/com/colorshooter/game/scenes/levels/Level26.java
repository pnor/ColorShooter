package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateMovingItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generateObject;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/30/2016.
 */
public class Level26 extends GameScreen{


    public Level26(ColorShooter game) {
        super(26, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(80f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("CubeSpace2"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(-100, -100, "FinalUFO", 12f, getEngine());
        em.get(enemySpawn).currentTime = 10f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(1300, 1000, "GhostUFO", 11f, getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 550, "SuperShootUp", 11f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(500, 350, "SpeedUp", 10f,  getEngine());
        GameEntity powerUps3 = generateMovingItemSpawnPoint(300, 550, "Health", 8f,  getEngine());
        em.get(powerUps3).currentTime = 3f;


        GameEntity colors = generateMovingItemSpawnPoint(618, 294, "Red", 25f, getEngine());
        GameEntity colors2 = generateMovingItemSpawnPoint(738, 434, "Blue", 32f, getEngine());
        GameEntity colors3 = generateMovingItemSpawnPoint(618, 694, "Green", 45f, getEngine());


        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);

        getEngine().addEntity(getPlayer());

        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);


        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);
        getEngine().addEntity(colors3);
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
        return new Level27(getGame());
    }
}
