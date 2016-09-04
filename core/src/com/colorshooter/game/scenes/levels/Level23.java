package com.colorshooter.game.scenes.levels;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Screen;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.AIComponent;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.components.PositionComponent;
import com.colorshooter.game.scenes.GameScreen;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.EntityConstructors.generateItemSpawnPoint;
import static com.colorshooter.game.EntityConstructors.generateMovingObject;
import static com.colorshooter.game.Mappers.cm;
import static com.colorshooter.game.Mappers.em;
import static com.colorshooter.game.Mappers.pm;

/**
 * Created by pnore_000 on 8/28/2016.
 */
public class Level23 extends GameScreen {

    private GameEntity object1;
    private GameEntity object2;
    private GameEntity powerUps;
    private GameEntity powerUps2;
    private boolean removed;


    public Level23(ColorShooter game) {
        super(23, game);
    }

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.backgroundAtlas.findRegion("CubeSpace"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemy = generateGhostUFO(1200, 900);
        GameEntity enemy2 = generateGhostUFO(1200, 0);
        GameEntity enemy3 = generateEnemyShipGold(-100,750);
        GameEntity enemy4 = generateEnemyShipGold(-100, 350);

        powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 10f,  getEngine());
        powerUps2 = generateMovingRandomPowerUp(220, 330, 16f,  getEngine());

        GameEntity colors = generateItemSpawnPoint(618, 394, "Green", 35f, getEngine());
        GameEntity colors2 = generateItemSpawnPoint(738, 434, "Blue", 45f, getEngine());

        object1 = generateMovingObject(100, 200, 400, 50, ImageComponent.atlas.findRegion("LongWidthCube"), 150f);
        object2 = generateMovingObject(900, 700, 400, 50, ImageComponent.atlas.findRegion("LongWidthCube"), 150f);


        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemy);
        getEngine().addEntity(enemy2);
        getEngine().addEntity(enemy3);
        getEngine().addEntity(enemy4);

        getEngine().addEntity(colors);
        getEngine().addEntity(colors2);

        getEngine().addEntity(object1);
        getEngine().addEntity(object2);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!removed && getEngine().getEntitiesFor(Family.all(AIComponent.class, PositionComponent.class).get()).size() == 4) {
            getEngine().removeEntity(object1);
            getEngine().removeEntity(object2);
            getEngine().removeEntity(powerUps);
            getEngine().removeEntity(powerUps2);
            removed = true;

        }
    }

    @Override
    public void hide() {
        super.hide();
        this.dispose();
    }
}
