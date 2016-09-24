package com.colorshooter.game.scenes.levels;

import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/31/2016.
 */
public class Level28 extends GameScreen {

    private boolean added;

    public Level28(ColorShooter game) {
        super(28, game);
    }

    @Override
    public void show() {
        super.show();
        added = false;
        setTimer(new GameTimer(80f));

        setBackground(ImageComponent.backgroundAtlas.findRegion("CubeSpace3"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(-100, 300, "SwirlWisp", 12f, getEngine());
        em.get(enemySpawn).currentTime = 10f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(680, 1000, "SwirlWisp", 13f, getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 550, "SpeedUp", 11f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(500, 350, "SpeedUp", 10f,  getEngine());
        GameEntity powerUps3 = generateMovingItemSpawnPoint(300, 550, "MaxHealth", 9f,  getEngine());
        em.get(powerUps3).currentTime = 3f;


        GameEntity colors = generateMovingItemSpawnPoint(618, 294, "Blue", 25f, getEngine());
        GameEntity colors2 = generateMovingItemSpawnPoint(738, 434, "Pink", 32f, getEngine());
        GameEntity colors3 = generateMovingItemSpawnPoint(618, 694, "White", 45f, getEngine());


        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);

        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);

        getEngine().addEntity(getPlayer());

        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);
        getEngine().addEntity(colors3);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!added && getTimer().getTime() <= 50f) {
            getEngine().addEntity(generateEnemySpawnPoint(180, 1000, "SwirlWisp", 7.5f, getEngine()));
            getEngine().addEntity(generateEnemySpawnPoint(-100, -100, "SwirlWisp", 6.5f, getEngine()));
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
        return new BonusLevel4(getGame());
    }
}
