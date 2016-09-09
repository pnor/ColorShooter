package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/27/2016.
 */
public class Level18 extends GameScreen{

    private boolean added;
    private GameTimer spawnTimer;

    public Level18(ColorShooter game) {
        super(18, game);
    }

    @Override
    public void show() {
        super.show();
        spawnTimer = new GameTimer(14f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("RedSpace2"));

        setPlayer(generatePlayer(678, 414));

        GameEntity enemy = generateEnemyShipGold(1200,1000);

        GameEntity color1 = generateItemSpawnPoint(550, 750, "Blue", 30f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(550, 250, "Red", 30f,  getEngine());

        GameEntity powerUps = generateItemSpawnPoint(200, 700, "Health", 6f,  getEngine());
        GameEntity powerUps2 = generateRandomPowerUp(1100, 200, 7f,  getEngine());

        GameEntity doubleUp = generateItemSpawnPoint(650, 450, "DoubleUp", 15f,  getEngine());
        em.get(doubleUp).currentTime = 4f;

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemy);
        getEngine().addEntity(doubleUp);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        spawnTimer.decreaseTimer(delta);

        if (!added && spawnTimer.getTime() <= 0f) {
            getEngine().addEntity(generateEnemyShipGold(1200,1000));
            getEngine().addEntity(generateEnemyShipGold(-100,-100));
            added = true;
        }
    }

    @Override
    public void hide() {
        super.hide();
        this.dispose();
    }

    @Override
    public Screen getNextLevel() {
        return new Level19(getGame());
    }
}
