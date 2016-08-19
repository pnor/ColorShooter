package com.colorshooter.game.scenes;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.components.AIComponent;
import com.colorshooter.game.components.FrozenComponent;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.EntityConstructors.*;
import static com.colorshooter.game.Mappers.hm;
import static com.colorshooter.game.Mappers.pom;

/**
 * Created by pnore_000 on 7/11/2016.
 */
public class EnemyTest extends GameScreen implements Screen{

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

    @Override
    public void show() {
        super.show();

        setBackground(ImageComponent.atlas.findRegion("Space1"));

        setPlayer(generatePlayer(10, 10));

        GameEntity enemy = generateEnemy(200,200);
        GameEntity enemy2 = generateEnemyShipRed(800,200);
        GameEntity turret = generateTurret(500, 500);

        GameEntity spawn = generateItemSpawnPoint(350,350, "SuperShootUp", 5f,  getEngine());
        GameEntity power = generateRandomPowerUp(700, 350, 3f, getEngine());

        GameEntity poisoner = generateFloatingPoison(500, 500, 40f, 40f);
        GameEntity shocker = generateFloatingShock(900, 900, 80f, 80f, 2);


        GameEntity enemySpawn = generateEnemySpawnPoint(900,700, "EnemyShipBlue", 8f,  getEngine());
        GameEntity enemySpawn2 = generateRandomEnemySpawnPoint(700,700, 6f,  getEngine());
        GameEntity enemySpawn3 = generateRandomShipSpawnPoint(900,700, 9f,  getEngine());
        GameEntity enemySpawn4 = generateRandomWispSpawnPoint(800,700,  9f, true, getEngine());
        GameEntity enemySpawn5 = generateRandomUFOSpawnPoint(100, 900,  8f, true, getEngine());


        movementSystem = new MovementSystem(1);
        collisionSystem = new CollisionSystem(7);
        playerInputSystem = new PlayerInputSystem(this, 2);
        shootingSystem = new ShootingSystem(4);
        drawingSystem = new DrawingSystem(5);
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

        getEngine().addEntity(enemy);
        getEngine().addEntity(enemy2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(turret);

        getEngine().addEntity(spawn);
        getEngine().addEntity(power);

        getEngine().addEntity(poisoner);
        getEngine().addEntity(shocker);

        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
        getEngine().addEntity(enemySpawn5);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        getEngine().update(delta);
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

    }
}
