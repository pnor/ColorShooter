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
import static com.colorshooter.game.EntityConstructors.generateMovingItemSpawnPoint;
import static com.colorshooter.game.Mappers.*;
import static com.colorshooter.game.Mappers.pom;

/**
 * Created by pnore_000 on 8/21/2016.
 */
public class Level6 extends GameScreen implements Screen {

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

    public Level6() {
        super(6);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(110f);

        setBackground(ImageComponent.atlas.findRegion("BlueSpace1"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "Wisp", 5f,  getEngine());
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100,900, "Wisp", 6f,  getEngine());
        em.get(enemySpawn2).currentTime = 4f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "BlueUFO", 8f,  getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(500, -40, "BigWisp", 20f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(750, 550, "Blue", 20f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(250, 350, "Green", 30f,  getEngine());
        em.get(color2).currentTime = 25f;
        GameEntity color3 = generateItemSpawnPoint(600, 600, "Yellow", 20f,  getEngine());
        em.get(color2).currentTime = 15f;
        GameEntity color4 = generateItemSpawnPoint(900, 100, "Red", 20f,  getEngine());

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "MaxHealth", 10f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(300, 450, "Health", 6f,  getEngine());
        GameEntity powerUps3 = generateRandomPowerUp(780, 780, 6.5f,  getEngine());

        GameEntity object1 = generateMovingObject(300, 300, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object2 = generateMovingObject(800, 800, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object3 = generateMovingObject(900, 100, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object4 = generateMovingObject(200, 750, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object5 = generateMovingObject(750, 650, ImageComponent.atlas.findRegion("SpaceJunk").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk"), 60f);
        GameEntity object6 = generateMovingObject(30, 920, ImageComponent.atlas.findRegion("SpaceJunk2").getRegionWidth(), ImageComponent.atlas.findRegion("SpaceJunk2").getRegionHeight(), ImageComponent.atlas.findRegion("SpaceJunk2"), 100f);

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

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(powerUps3);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(color4);
        getEngine().addEntity(color3);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(object1);
        getEngine().addEntity(object2);
        getEngine().addEntity(object3);
        getEngine().addEntity(object4);
        getEngine().addEntity(object5);
        getEngine().addEntity(object6);
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
