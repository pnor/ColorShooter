package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;

/**
 * Created by pnore_000 on 8/26/2016.
 */
public class Level16 extends GameScreen{

    public Level16(ColorShooter game) {
        super(16, game);
    }

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.backgroundAtlas.findRegion("GreenSpace2"));

        setPlayer(generatePlayer(678, 414));

        GameEntity boss = generateBossWisp(900, 800);

        GameEntity powerUps = generateItemSpawnPoint(200, 200, "Health", 6f,  getEngine());
        GameEntity powerUps2 = generateItemSpawnPoint(1100, 700, "MaxHealth", 12f,  getEngine());
        GameEntity powerUps3 = generateItemSpawnPoint(200, 700, "ShootUp", 9.2f,  getEngine());
        GameEntity powerUps4 = generateItemSpawnPoint(1100, 200, "ShootUp", 9f,  getEngine());

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

    @Override
    public Screen getNextLevel() {
        return new Level17(getGame());
    }
}
