package com.colorshooter.game.scenes.levels;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.tests.GameScreen;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.Mappers.*;
import static com.colorshooter.game.Mappers.pom;

/**
 * Created by pnore_000 on 8/23/2016.
 */
public class Level10 extends GameScreen implements Screen {

    public Level10() {
        super(10);
    }

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.backgroundAtlas.findRegion("Space1"));

        setPlayer(generatePlayer(678, 414));

        GameEntity boss = generateBossUFO(100, 900);

        GameEntity powerUps = generateItemSpawnPoint(200, 200, "Health", 5f,  getEngine());
        GameEntity powerUps2 = generateItemSpawnPoint(800, 800, "SpeedUp", 7f,  getEngine());
        GameEntity powerUps3 = generateItemSpawnPoint(200, 800, "MaxHealth", 9f,  getEngine());
        GameEntity powerUps4 = generateItemSpawnPoint(800, 200, "ShootUp", 9f,  getEngine());

        GameEntity colors = generateRandomColorSpawnPoint(678, 450, 15f,  getEngine());

        getEngine().addEntity(boss);
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
