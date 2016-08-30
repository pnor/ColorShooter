package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.Mappers.aim;
import static com.colorshooter.game.Mappers.em;

/**
 * Created by pnore_000 on 8/28/2016.
 */
public class Level21 extends GameScreen implements Screen{

    public Level21(ColorShooter game) {
        super(21, game);
    }

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space4"));

        setPlayer(generatePlayer(678, 414));

        GameEntity boss = generateBossShip(1500, 800);
        GameEntity boss2 = generateBossShip(-300, 100);
        aim.get(boss2).projectileType = 'f';
        em.get(boss2).currentTime = 5f;

        GameEntity powerUps = generateItemSpawnPoint(200, 200, "DoubleUp", 9f,  getEngine());
        GameEntity powerUps2 = generateItemSpawnPoint(1100, 700, "DoubleUp", 9f,  getEngine());
        GameEntity powerUps3 = generateItemSpawnPoint(200, 700, "MaxHealth", 6f,  getEngine());
        GameEntity powerUps4 = generateRandomPowerUp(1100, 200, 5f,  getEngine());

        GameEntity colors = generateRandomColorSpawnPoint(678, 450, 10f,  getEngine());

        getEngine().addEntity(boss);
        getEngine().addEntity(boss2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
        getEngine().addEntity(powerUps4);
        getEngine().addEntity(colors);
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
