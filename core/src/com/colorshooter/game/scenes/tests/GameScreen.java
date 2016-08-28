package com.colorshooter.game.scenes.tests;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.AIComponent;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.components.PositionComponent;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/4/2016.
 */
public class GameScreen implements Screen {

    private Stage stage;
    //private SpriteBatch batch;
    private ShapeRenderer shapes;
    private Engine engine;

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

    private TextureRegion background;

    private Table table;
    private TextureAtlas uiatlas;
    private Skin skin;
    private Label healthLabel;
    private Label levelLabel;
    private Label levelNum;
    private Label life;
    private Label lifeCount;
    private Image icon;
    private Image healthBar;
    private Label pointID;
    private Label pointMultiplierLabel;
    private Label pointNum;
    private Label timeLabel;

    private static GameEntity player;

    private int level;
    private static int lives = 5;
    private float currentRespawnTime;
    private float endRespawnTime;
    private static int points;
    private GameTimer timer;

    private static float pointMultiplier = 1;
    private float bestMultiplier;
    private float currentMultiTime;
    private float endMultiTime;

    private float currentVictoryTime;
    private float victoryEndTime = 3f;
    private boolean nextScreen;

    /**
     * 0 : normal
     * 1 : victory
     * 2 : reset
     * 3 : paused
     */
    private int screenState;

    private int lastHealth;
    private int lastMax;

    public GameScreen(int i) {
        super();
        level = i;
    }

    public GameScreen() {
        super();
        level = -1;
    }

    @Override
    public void show() {
        screenState = 0;
        pointMultiplier = 1.0f;

        stage = new Stage();
        shapes = new ShapeRenderer();
        engine = new Engine();

        stage.getViewport().apply();

        if (player != null && !player.getDisposed()) {
            lastHealth = hm.get(player).health;
            lastMax = hm.get(player).maxHealth;
        } else {
            lastHealth = 0;
        }

        endRespawnTime = 3f;
        endMultiTime = 10f;
        resetRespawnTimer();
        resetMultiTimer();
        setUpHUD();

        //set up systemsw
        movementSystem = new MovementSystem(1);
        playerInputSystem = new PlayerInputSystem(this, 2);
        healthSystem = new HealthSystem(ImageComponent.atlas, 3);
        shootingSystem = new ShootingSystem(4);
        drawingSystem = new DrawingSystem(5, getBatch(), getShapes());
        damageSystem = new DamageSystem(6);
        collisionSystem = new CollisionSystem(7);
        aiSystem = new AISystem(this, 8);
        lifetimeSystem = new LifetimeSystem(9);
        animationSystem = new AnimationSystem(10);
        eventSystem = new EventSystem(11);
        itemSystem = new ItemSystem(12);
        bounceSystem = new BouncingSystem(13);
        poisSystem = new PoisonSystem(14);
        frozenSystem = new FrozenSystem(15);

        engine.addSystem(movementSystem);
        engine.addSystem(playerInputSystem);
        engine.addSystem(healthSystem);
        engine.addSystem(shootingSystem);
        engine.addSystem(drawingSystem);
        engine.addSystem(damageSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(aiSystem);
        engine.addSystem(lifetimeSystem);
        engine.addSystem(animationSystem);
        engine.addSystem(eventSystem);
        engine.addSystem(itemSystem);
        engine.addSystem(bounceSystem);
        engine.addSystem(poisSystem);
        engine.addSystem(frozenSystem);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();


        if (background != null) {
           stage.getBatch().begin();
           if (screenState == 1)
               stage.getBatch().setColor(Color.GRAY);
            stage.getBatch().draw(background, 0, 0, stage.getWidth(), stage.getHeight());
            stage.getBatch().end();
        }

        if (screenState == 3)
            return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
            nextScreen = true;

        if (screenState == 1) {
            showVictoryHUD();
            return;
        }

        checkVictory(delta);
        checkDeath(delta);
        incrementMultiTimer(delta);
        if (timer != null)
            timer.decreaseTimer(delta);
        updateHUD();

        if (getScreenState() == 1) {
            showVictoryHUD();
            return;
        }

        getEngine().update(delta);
        getBatch().begin();
        drawHUD();
        getBatch().end();

        for (Entity e : engine.getEntities()) {
            if (((GameEntity) e).getDisposed()) {
                if (pom.has(e)) {
                    incrementPoints((int) (pom.get(e).points * getPointMultiplier()));
                    incrementPointMultiplier(0.1f);
                    resetMultiTimer();
                }
                if (((GameEntity) e).dispose())
                    engine.removeEntity(e);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        if (screenState != 3)
            screenState = 3;
    }

    @Override
    public void resume() {
        if (player.getDisposed()) {
            screenState = 2;
            return;
        }

        if (timer != null) {
            if (timer.checkIfFinished()) {
                screenState = 1;
                return;
            }
        } else if (timer == null) {
            if (engine.getEntitiesFor(Family.all(PositionComponent.class, AIComponent.class).get()).size() <= 0) {
                screenState = 1;
                return;
            }
        }

        screenState = 0;

    }

    @Override
    public void hide() {
        nextScreen = false;
        screenState = 1;

        for (EntitySystem system : getEngine().getSystems()) {
            engine.removeSystem(system);
        }
        engine.removeAllEntities();

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

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void setUpHUD() {
        uiatlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.addRegions(uiatlas);

        icon = new Image(ImageComponent.atlas.findRegion("PlayerShip"));
        icon.setSize(65, 65);

        table = new Table(skin);
        table.setSize(getStage().getWidth(), getStage().getHeight());
        table.setFillParent(true);

        healthLabel = new Label("Health : 200 / 200", skin);
        healthLabel.setFontScale(1.25f, 1.25f);
        healthBar = new Image(new TextureAtlas("CSNinePatch.pack").createPatch("barpatch"));
        if (level >= 1) {
            levelLabel = new Label("Level:", skin);
            levelNum = new Label("" + level, skin);
            levelNum.setFontScale(1.1f);
        } else {
            levelLabel = new Label("Bonus!", skin);
        }

        if (level <= 0)
            levelLabel.setColor(Color.YELLOW);
        else if (level <= 10 && level >= 1)
            levelNum.setColor(Color.ORANGE);
        else if (level >= 11 && level <= 16)
            levelNum.setColor(Color.CYAN);
        else if (level >= 17 && level <= 22)
            levelNum.setColor(Color.YELLOW);

        life = new Label("Lives:", skin);
        lifeCount = new Label("-", skin);
        pointID = new Label("Points:", skin);
        pointNum = new Label("0", skin);
        if (timer != null)
            timeLabel = new Label(timer.toString(), skin);
        else
            timeLabel = new Label("-:--", skin);

        pointMultiplierLabel = new Label("" + pointMultiplier, skin);
        pointMultiplierLabel.setFontScale(2f);

        //  BACKUP!!!
        //table.center().setFillParent(true);
        table.top().left();
        table.pad(22);
        table.add(icon);
        table.add(healthLabel).padLeft(10);
        if (level >= 1)
            table.add(levelLabel).padLeft(400);
        else
            table.add(levelLabel).padLeft(410);
        if (levelNum != null)
            table.add(levelNum).padLeft(10);
        table.add().padLeft(600);
        table.add(life).padLeft(5);
        table.add(lifeCount).padLeft(10);
        table.row();
        table.add(pointID).padLeft(5);
        table.add(pointNum).padLeft(30);
        //table.add(healthBar);
        table.add(timeLabel).padLeft(400);
        table.row();
        table.add(pointMultiplierLabel).padTop(730);
        /*
        table.add().fillX().expandY();
        table.add(healthBar);


        /* EXPERIMENTAL
        table.center().setFillParent(true);
        table.top().left();
        table.pad(12);
        table.add(icon);
        table.add(healthLabel).padLeft(10);
        table.add(levelLabel).padLeft(400);
        table.add(levelNum);
        table.add().padLeft(520);
        table.add(life).padLeft(5);
        table.add(lifeCount).padLeft(10);
        table.row();
        table.add().fillX().expandY();
        table.add(healthBar);
        table.add(timeLabel).padLeft(450).padBottom(700f);
        table.row();
        table.add(pointID).padLeft(5);
        table.add(pointNum).padLeft(30);
        /*
        table.add().fillX().expandY();
        table.add(healthBar);
        */

        table.debug();
    }

    private void updateHUD() {
        pointMultiplierLabel.setText("" + pointMultiplier);
        pointMultiplierLabel.setColor(1f, 1.25f - (pointMultiplier / 4), 1.25f - (pointMultiplier / 4), 1f);

        if (!player.getDisposed()) {

            if (lastHealth != hm.get(player).health) {
                lastHealth = hm.get(player).health;
                healthLabel.setText("Health : " + lastHealth + " / " + lastMax);
            }
            if (lastMax != hm.get(player).maxHealth) {
                lastMax = hm.get(player).maxHealth;
                healthLabel.setText("Health : " + lastHealth + " / " + lastMax);
            }
            if (!icon.equals(im.get(player).texRegion)) {
                float w = icon.getWidth(); float h = icon.getHeight();
                icon.setDrawable(new TextureRegionDrawable(im.get(player).texRegion));
                icon.setSize(w, h);
            }
            if (!lifeCount.textEquals(Integer.toString(lives))) {
                lifeCount.setText(Integer.toString(lives));
            }

            if (poim.get(player).isPoisoned)
                colorHUD(Color.GREEN);
            else if (fm.get(player).isFrozen)
                colorHUD(Color.CYAN);
            else if (bm.get(player).isBouncing)
                colorHUD(Color.PINK);
            else
                colorHUD(Color.WHITE);

            pointNum.setText(Integer.toString(points));
            if (timer != null)
                timeLabel.setText(timer.toString());
            else
                timeLabel.setText("-:--");
        }


        healthLabel.toFront();
        icon.toFront();
    }

    public void drawHUD() {
        table.draw(stage.getBatch(), 1);
    }

    public void showVictoryHUD() {
        Label victoryText = new Label("Level Complete!", skin);
        victoryText.setColor(Color.CYAN);
        victoryText.setFontScale(1.3f);
        table.clearChildren();
        table.center();
        table.add(victoryText).padBottom(50f);
        table.row();
        table.add(icon).padBottom(10f);
        table.row();
        table.add("Lives : " + lives);
        table.row();
        table.add("Health : " + hm.get(player).health + " / " + hm.get(player).maxHealth);
        table.row();
        table.add("" + points);
        table.row();
        Label bestMultiLabel = new Label("Best Score Multiplier : " + bestMultiplier, skin);
        bestMultiLabel.setColor(1.25f  - (bestMultiplier / 4), 1.25f - (bestMultiplier / 4), 1f, 1f);
        table.add(bestMultiLabel).padBottom(40f);
        table.row();
        Label screenText = new Label("Press ENTER to continue", skin);
        screenText.setColor(Color.YELLOW);
        table.add(screenText);
        stage.getBatch().begin();
        stage.getBatch().setColor(Color.WHITE);
        table.draw(stage.getBatch(), 1);
        stage.getBatch().end();
    }

    public void colorHUD(Color color) {
        if (!color.equals(healthLabel.getColor()))
            healthLabel.setColor(color);
    }

    public String toString() {
        return this.getClass().toString();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage1) {
        stage = stage1;
    }

    public Engine getEngine() {
        return engine;
    }

    public Batch getBatch() {
        return stage.getBatch();
    }

    public ShapeRenderer getShapes() {
        return shapes;
    }

    public void setBackground(TextureRegion tex) {
        background = tex;

    }

    public void setPlayer(GameEntity p) {
        player = p;
    }

    public GameEntity getPlayer() {
        return player;
    }

    public boolean checkDeath(float dt) {
        if (player.getDisposed()) {
            incrementRespawnTimer(dt);
            return true;
        }
        return false;
    }

    public void checkVictory(float dt) {
        if (timer != null) {
            if (timer.checkIfFinished())
                screenState = 1;
        } else if (timer == null) {
            if (engine.getEntitiesFor(Family.all(PositionComponent.class, AIComponent.class).get()).size() <= 0) {
                currentVictoryTime += dt;
                if (currentVictoryTime >= victoryEndTime)
                    screenState = 1;
            }
        }
    }

    public void incrementRespawnTimer(float dt) {
        currentRespawnTime += dt;
        if (currentRespawnTime >= endRespawnTime) {
            screenState = 2;
        }
    }

    public void incrementMultiTimer(float dt) {
        currentMultiTime += dt;
        if (currentMultiTime >= endRespawnTime) {
            pointMultiplier = 1f;
            currentMultiTime = 0f;
        }
    }

    public void resetMultiTimer() {
        currentMultiTime = 0f;
    }

    public void reset() {
        lives -= 1;
        pointMultiplier = 1f;
        if (lives < 0)
            Gdx.app.exit();
        screenState = 0;
    }

    public void resetRespawnTimer() {
        currentRespawnTime = 0f;
    }

    public void setScreenState(int s) {
       screenState = s;
    }

    public int getScreenState() {
        return screenState;
    }

    public int getPoints() {
        return points;
    }

    public void incrementPoints(int p) {
        points += p;
    }

    public GameTimer getTimer() {
        return timer;
    }

    public void setTimer(GameTimer t) {
        timer = t;
    }

    public boolean getNextScreen() {
        return nextScreen;
    }

    public static float getPointMultiplier() {
        return pointMultiplier;
    }

    public static void setPointMultiplier(float n) {
        pointMultiplier = n;
    }

    public void incrementPointMultiplier(float n) {
        pointMultiplier += n;
        pointMultiplier = Math.round(pointMultiplier * 100);
        pointMultiplier /= 100;
        if (pointMultiplier > bestMultiplier)
            bestMultiplier = pointMultiplier;
    }
}
