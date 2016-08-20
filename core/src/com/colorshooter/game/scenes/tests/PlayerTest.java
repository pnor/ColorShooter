package com.colorshooter.game.scenes.tests;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.systems.DrawingSystem;
import com.colorshooter.game.systems.MovementSystem;
import com.colorshooter.game.systems.PlayerInputSystem;

import static com.colorshooter.game.EntityConstructors.generatePlayer;

/**
 * Created by pnore_000 on 7/3/2016.
 */
public class PlayerTest extends GameScreen implements Screen{

    private MovementSystem movementSystem;
    private PlayerInputSystem playerInputSystem;
    private DrawingSystem drawingSystem;

    public PlayerTest(int i) {
        super(i);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(300f);

        setPlayer(generatePlayer(10, 10));

        movementSystem = new MovementSystem(1);
        playerInputSystem = new PlayerInputSystem(this, 2);
        drawingSystem = new DrawingSystem(3, getBatch(), getShapes());

        getEngine().addSystem(movementSystem);
        getEngine().addSystem(playerInputSystem);
        getEngine().addSystem(drawingSystem);
        getEngine().addEntity(getPlayer());


    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getEngine().update(delta);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void hide() {
        getEngine().removeAllEntities();

        for (EntitySystem system : getEngine().getSystems()) {
            getEngine().removeSystem(system);
        }

        movementSystem = null;
        playerInputSystem = null;
        drawingSystem = null;
    }

}
