package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 8/20/2016.
 */
public class Level5 extends GameScreen {

    private boolean removed;
    private GameTimer endSpawns;
    private GameEntity enemySpawn, enemySpawn2, enemySpawn3, enemySpawn4;

    public Level5(ColorShooter game) {
        super(5, game);
    }

    @Override
    public void show() {
        super.show();
        endSpawns = new GameTimer(60f);
        setBackground(ImageComponent.backgroundAtlas.findRegion("BlueSpace1"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemy = generateBlueEnemy(1000,900);
        GameEntity enemy2 = generateBlueEnemy(500,900);
        GameEntity enemy3 = generateRedEnemy(-100,900);
        GameEntity enemy4 = generateEnemy(-100,-100);

        enemySpawn = generateEnemySpawnPoint(1000,900, "BlueUFO", 17f,  getEngine());
        em.get(enemySpawn).currentTime = 6f;
        enemySpawn2 = generateEnemySpawnPoint(100,900, "UFO", 20f,  getEngine());
        em.get(enemySpawn2).currentTime = 6f;
        enemySpawn3 = generateEnemySpawnPoint(0, 0, "BlueUFO", 18f,  getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        enemySpawn4 = generateEnemySpawnPoint(500, -40, "RedUFO", 18f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(750, 550, "Yellow", 20f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(250, 350, "Pink", 50f,  getEngine());
        em.get(color2).currentTime = 25f;

        GameEntity powerUps = generateItemSpawnPoint(300, 450, "Health", 8f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateItemSpawnPoint(700, 750, "Health", 5.5f,  getEngine());

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(enemy);
        getEngine().addEntity(enemy2);
        getEngine().addEntity(enemy3);
        getEngine().addEntity(enemy4);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        endSpawns.decreaseTimer(delta);

        if (!removed && endSpawns.getTime() <= 0) {
            getEngine().removeEntity(enemySpawn);
            getEngine().removeEntity(enemySpawn2);
            getEngine().removeEntity(enemySpawn3);
            getEngine().removeEntity(enemySpawn4);
            removed = true;
        }
    }

    @Override
    public void hide() {
        super.hide();
        this.dispose();
    }

    @Override
    public Screen getNextLevel() {
        return new BonusLevel1(getGame());
    }
}
