package com.colorshooter.game.scenes.tests;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.EntityConstructors.generatePlayer;
import static com.colorshooter.game.EntityConstructors.generateWall;

/**
 * Created by pnore_000 on 7/4/2016.
 */
public class WallTest extends GameScreen implements Screen {

    private MovementSystem movementSystem;
    private CollisionSystem collisionSystem;
    private BouncingSystem bouncingSystem;
    private PlayerInputSystem playerInputSystem;
    private DrawingSystem drawingSystem;

    public WallTest(int i) {
        super(i);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(500f);

        setBackground(ImageComponent.atlas.findRegion("Space3"));

        setPlayer(generatePlayer(10, 10));

        GameEntity wall = generateWall(200, 200, 100, 100);

        playerInputSystem = new PlayerInputSystem(this, 4);
        movementSystem = new MovementSystem(1);
        collisionSystem = new CollisionSystem(2);
        bouncingSystem = new BouncingSystem(3);
        drawingSystem = new DrawingSystem(4, getBatch(), getShapes());

        getEngine().addSystem(playerInputSystem);
        getEngine().addSystem(movementSystem);
        getEngine().addSystem(collisionSystem);
        getEngine().addSystem(bouncingSystem);
        getEngine().addSystem(drawingSystem);

        getEngine().addEntity(getPlayer());
        getEngine().addEntity(wall);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getEngine().update(delta);
    }

    @Override
    public void hide() {
        getEngine().removeAllEntities();

        for (EntitySystem system : getEngine().getSystems()) {
            getEngine().removeSystem(system);
        }

        movementSystem = null;
        collisionSystem = null;
        playerInputSystem = null;
        drawingSystem = null;
    }
}
