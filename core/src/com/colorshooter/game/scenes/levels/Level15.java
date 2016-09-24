package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateFloatingShock;
import static com.colorshooter.game.Mappers.em;

/**
 * Created by pnore_000 on 8/26/2016.
 */
public class Level15 extends GameScreen{
    public Level15(ColorShooter game) {
        super(15, game);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer(80f));

        setBackground(ImageComponent.backgroundAtlas.findRegion("YellowSpace"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemySpawn = generateRandomUFOSpawnPoint(1000,900, 2f, false, getEngine());
        GameEntity enemySpawn2 = generateRandomUFOSpawnPoint(0,1000, 3f, false,   getEngine());
        em.get(enemySpawn2).currentTime = 1f;
        GameEntity enemySpawn3 = generateRandomUFOSpawnPoint(-100, 0, 4f, false, getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(1500, -40, "FastEnemy", 6f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(750, 550, "Red", 20f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(250, 350, "Pink", 30f,  getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "MaxHealth", 12f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(300, 450, "Health", 10f,  getEngine());
        GameEntity powerUps3 = generateRandomPowerUp(220, 330, 9f,  getEngine());

        GameEntity object1 = generateFloatingShock(300, 300, 100, 100, 450f, 2);
        GameEntity object2 = generateFloatingShock(100, 100, 100, 100, 450f, 2);

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
        return new BonusLevel2(getGame());
    }
}
