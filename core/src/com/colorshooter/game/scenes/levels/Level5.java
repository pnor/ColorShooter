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
import static com.colorshooter.game.Mappers.*;
import static com.colorshooter.game.Mappers.pom;

/**
 * Created by pnore_000 on 8/20/2016.
 */
public class Level5 extends GameScreen implements Screen {

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

    public Level5() {
        super(5);
    }

    @Override
    public void show() {
        super.show();
        setTimer(new GameTimer());
        getTimer().setTime(90f);

        setBackground(ImageComponent.backgroundAtlas.findRegion("BlueSpace1"));

        setPlayer(generatePlayer(678, 414));
        cm.get(getPlayer()).boundingBox.setOrigin(pm.get(getPlayer()).x + pm.get(getPlayer()).originX, pm.get(getPlayer()).y + pm.get(getPlayer()).originY);

        GameEntity enemySpawn = generateEnemySpawnPoint(1000,900, "BlueUFO", 10f,  getEngine());
        em.get(enemySpawn).currentTime = 6f;
        GameEntity enemySpawn2 = generateEnemySpawnPoint(100,900, "UFO", 16f,  getEngine());
        em.get(enemySpawn2).currentTime = 6f;
        GameEntity enemySpawn3 = generateEnemySpawnPoint(0, 0, "BlueUFO", 10f,  getEngine());
        em.get(enemySpawn3).currentTime = 3f;
        GameEntity enemySpawn4 = generateEnemySpawnPoint(500, -40, "RedUFO", 16f,  getEngine());

        GameEntity color1 = generateItemSpawnPoint(750, 550, "Yellow", 20f,  getEngine());
        em.get(color1).currentTime = 10f;
        GameEntity color2 = generateItemSpawnPoint(250, 350, "Pink", 50f,  getEngine());
        em.get(color2).currentTime = 25f;

        GameEntity powerUps = generateMovingItemSpawnPoint(300, 450, "Health", 6f,  getEngine());
        em.get(powerUps).currentTime = 5f;
        GameEntity powerUps2 = generateMovingItemSpawnPoint(300, 450, "Health", 6f,  getEngine());

        getEngine().addEntity(powerUps);
        getEngine().addEntity(powerUps2);
        getEngine().addEntity(color1);
        getEngine().addEntity(color2);
        getEngine().addEntity(getPlayer());
        getEngine().addEntity(enemySpawn);
        getEngine().addEntity(enemySpawn2);
        getEngine().addEntity(enemySpawn3);
        getEngine().addEntity(enemySpawn4);
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
}
