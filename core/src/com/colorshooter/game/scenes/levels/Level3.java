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
import static com.colorshooter.game.EntityConstructors.generateRandomPowerUp;
import static com.colorshooter.game.Mappers.*;
import static com.colorshooter.game.Mappers.pom;

/**
 * Created by pnore_000 on 8/20/2016.
 */
public class Level3 extends GameScreen implements Screen {

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

    public Level3() {
        super(3);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(80f);

        setBackground(ImageComponent.atlas.findRegion("Space2"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity object1 = generateObject(300, 300, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"));
        GameEntity object2 = generateObject(700, 600, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"));
        GameEntity object3 = generateObject(960, 100, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"));


        GameEntity enemySpawn = generateEnemySpawnPoint(100,900, "RedUFO", 7f,  getEngine());
        em.get(enemySpawn).currentTime = 6f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(0, 0, "UFO", 7f,  getEngine());
        em.get(enemySpawn2).currentTime = 3f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(1000, 0, "RedUFO", 18f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(550, 750, "Red", 20f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(750, 150, "Blue", 20f,  getEngine());
        em.get(color2).currentTime = 5f;

        GameEntity powerUps = generateItemSpawnPoint(300, 450, "Health", 7f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateItemSpawnPoint(770, 150, "Health", 7f,  getEngine());
        GameEntity powerUps3 = generateRandomPowerUp(900, 450, 12f,  getEngine());


        movementSystem = new MovementSystem(1);
        collisionSystem = new CollisionSystem(7);
        playerInputSystem = new PlayerInputSystem(this, 2);
        shootingSystem = new ShootingSystem(4);
        drawingSystem = new DrawingSystem(5, getBatch(), getShapes());
        healthSystem = new HealthSystem(ImageComponent.atlas, 3);
        damageSystem = new DamageSystem(6);
        aiSystem = new AISystem(this, new GameEntity[] {getPlayer()}, 8);
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

        getEngine().addEntity(object1);
        getEngine().addEntity(object2);
        getEngine().addEntity(object3);
        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (getVictory()) {
            showVictoryHUD();
            return;
        }

        getEngine().update(delta);
        getBatch().begin();
        drawHUD();
        getBatch().end();

        for (Entity e : getEngine().getEntities()) {
            if (((GameEntity) e).getDisposed()) {
                if (pom.has(e))
                    incrementPoints(pom.get(e).points);
                if (((GameEntity) e).dispose())
                    getEngine().removeEntity(e);
            }
        }
    }

    @Override
    public void hide() {
        super.hide();
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
