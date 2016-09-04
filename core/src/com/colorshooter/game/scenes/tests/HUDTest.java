package com.colorshooter.game.scenes.tests;

import com.badlogic.ashley.core.EntitySystem;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.EntityConstructors.*;

/**
 * Created by pnore_000 on 8/8/2016.
 */
public class HUDTest extends GameScreen {

    private MovementSystem movementSystem;
    private CollisionSystem collisionSystem;
    private PlayerInputSystem playerInputSystem;
    private DrawingSystem drawingSystem;
    private ShootingSystem shootingSystem;
    private HealthSystem healthSystem;
    private DamageSystem damageSystem;
    private AISystem aiSystem;
    private LifetimeSystem lifetimeSystem;
    private AnimationSystem animationSystem;
    private EventSystem eventSystem;
    private ItemSystem itemSystem;
    private BouncingSystem bounceSystem;
    private PoisonSystem poisSystem;
    private FrozenSystem frozenSystem;

    public HUDTest(int i, ColorShooter game) {
        super(i, game);
    }

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.atlas.findRegion("Space4"));

        setPlayer(generatePlayer(10, 10));

        GameEntity enemy = generateBossShip(900,600);
        GameEntity spawn = generateRandomItemSpawnPoint(350,350,5f,  getEngine());
        GameEntity power = generateRandomPowerUp(500, 500, 3f, getEngine());
        GameEntity power2 = generateRandomPowerUp(700, 350, 3f, getEngine());
        GameEntity power3 = generateRandomPowerUp(1000, 350, 3f, getEngine());

        setUpSystems();

        getEngine().addEntity(enemy);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(spawn);
        getEngine().addEntity(power);
        getEngine().addEntity(power2);
        getEngine().addEntity(power3);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    private void setUpSystems() {
        movementSystem = new MovementSystem(1);
        collisionSystem = new CollisionSystem(7);
        playerInputSystem = new PlayerInputSystem(this, 2);
        shootingSystem = new ShootingSystem(4);
        drawingSystem = new DrawingSystem(5, getBatch());
        healthSystem = new HealthSystem(ImageComponent.atlas, 3);
        damageSystem = new DamageSystem(6);
        aiSystem = new AISystem(this, 8);
        lifetimeSystem = new LifetimeSystem(9);
        animationSystem = new AnimationSystem(10);
        eventSystem = new EventSystem(11);
        itemSystem = new ItemSystem(12);
        bounceSystem = new BouncingSystem(13);
        poisSystem = new PoisonSystem(14);
        frozenSystem = new FrozenSystem(15);

        getEngine().addSystem(playerInputSystem);
        getEngine().addSystem(movementSystem);
        getEngine().addSystem(collisionSystem);
        getEngine().addSystem(shootingSystem);
        getEngine().addSystem(drawingSystem);
        getEngine().addSystem(healthSystem);
        getEngine().addSystem(damageSystem);
        getEngine().addSystem(aiSystem);
        getEngine().addSystem(lifetimeSystem);
        getEngine().addSystem(animationSystem);
        getEngine().addSystem(eventSystem);
        getEngine().addSystem(itemSystem);
        getEngine().addSystem(bounceSystem);
        getEngine().addSystem(poisSystem);
        getEngine().addSystem(frozenSystem);
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
        aiSystem = null;
        lifetimeSystem = null;
        animationSystem = null;
        eventSystem = null;
        itemSystem = null;
        bounceSystem = null;
        poisSystem = null;
        frozenSystem = null;

        this.dispose();
    }


}
