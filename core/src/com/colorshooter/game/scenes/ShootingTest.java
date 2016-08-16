package com.colorshooter.game.scenes;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Screen;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.EntityConstructors.generatePlayer;
import static com.colorshooter.game.EntityConstructors.generateWall;

/**
 * Created by pnore_000 on 7/5/2016.
 */
public class ShootingTest extends GameScreen implements Screen{
    private MovementSystem movementSystem;
    private CollisionSystem collisionSystem;
    private PlayerInputSystem playerInputSystem;
    private DrawingSystem drawingSystem;
    private ShootingSystem shootingSystem;
    private HealthSystem healthSystem;
    private DamageSystem damageSystem;

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.atlas.findRegion("GraySpace1"));

        setPlayer(generatePlayer(10, 10));

        GameEntity wall = generateWall(350, 350, 70, 40);


        movementSystem = new MovementSystem(3);
        collisionSystem = new CollisionSystem(1);
        playerInputSystem = new PlayerInputSystem(this, 2);
        shootingSystem = new ShootingSystem(4);
        drawingSystem = new DrawingSystem(5);
        healthSystem = new HealthSystem(ImageComponent.atlas, 7);
        damageSystem = new DamageSystem(6);

        getEngine().addSystem(playerInputSystem);
        getEngine().addSystem(movementSystem);
        getEngine().addSystem(collisionSystem);
        getEngine().addSystem(shootingSystem);
        getEngine().addSystem(drawingSystem);
        getEngine().addSystem(healthSystem);
        getEngine().addSystem(damageSystem);
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
        shootingSystem = null;
        healthSystem = null;
        damageSystem = null;
    }
}
