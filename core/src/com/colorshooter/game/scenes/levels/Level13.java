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
public class Level13 extends GameScreen {

    public Level13(ColorShooter game) {
        super(13, game);
    }

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.backgroundAtlas.findRegion("GraySpace2"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemy = generatePoisonWisp(100,100);
        GameEntity enemy2 = generateGreenEnemy(1000,1000);
        GameEntity enemy3 = generateGreenEnemy(500,-100);
        GameEntity enemy4 = generateGreenEnemy(100,100);
        GameEntity enemy5 = generatePoisonWisp(500,100);
        GameEntity enemy6 = generatePoisonWisp(100,500);
        GameEntity enemy7 = generateBigWisp(1500,1000);


        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "PoisonWisp", 25f,  getEngine());
        em.get(enemySpawn).currentTime = 24f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0, 0, "GreenUFO", 50f,  getEngine());
        em.get(enemySpawn2).currentTime = 40f;

        GameEntity color1 = generateItemSpawnPoint(550, 750, "Blue", 15f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(550, 250, "Purple", 35f,  getEngine());

        GameEntity powerUps = generateItemSpawnPoint(450, 450, "Health", 7f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateItemSpawnPoint(750, 750, "SpeedUp", 8f,  getEngine());


        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemy);
        getEngine().addEntity(enemy2);
        getEngine().addEntity(enemy3);
        getEngine().addEntity(enemy4);
        getEngine().addEntity(enemy5);
        getEngine().addEntity(enemy6);
        getEngine().addEntity(enemy7);


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
        return new Level14(getGame());
    }
}
