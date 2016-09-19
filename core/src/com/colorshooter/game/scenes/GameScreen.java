package com.colorshooter.game.scenes;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.components.AIComponent;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.components.ItemReceivableComponent;
import com.colorshooter.game.components.PositionComponent;
import com.colorshooter.game.systems.*;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/4/2016.
 */
public class GameScreen extends DisplayScreen {

    private Engine engine;

    private static MovementSystem movementSystem;
    private static CollisionSystem collisionSystem;
    private static PlayerInputSystem playerInputSystem;
    private static DrawingSystem drawingSystem;
    private static ShootingSystem shootingSystem;
    private static HealthSystem healthSystem;
    private static DamageSystem damageSystem;
    private static AISystem aiSystem;
    private static LifetimeSystem lifetimeSystem;
    private static AnimationSystem animationSystem;
    private static EventSystem eventSystem;
    private static ItemSystem itemSystem;
    private static BouncingSystem bounceSystem;
    private static PoisonSystem poisSystem;
    private static FrozenSystem frozenSystem;
    private static ItemReceivableSystem itemReceivableSystem;

    private TextureRegion background;

    private Table table;
    private TextureAtlas uiatlas;
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
    private Label objectiveLabel;

    private static GameEntity player;

    /**
     * Tells what level this screen represents. <p>
     * If the level is less than 1, then it is a bonus level
     */
    private int level;
    private static int lastLevel;
    private static int lives = 3;
    private float currentRespawnTime;
    private float endRespawnTime;
    private GameTimer timer;
    private static int points;
    private int oldScore;
    private int newScore;

    private static float pointMultiplier = 1;
    private float bestMultiplier;
    private float currentMultiTime;
    private float endMultiTime;

    private float currentVictoryTime;
    private float victoryEndTime = 3f;

    /**
     * 0 : normal
     * 1 : victory
     * 2 : reset
     * 3 : paused
     */
    private int screenState;

    private int lastHealth;
    private int lastMax;

    /**
     * Creates a GameScreen with an adjustable level
     * @param i what the level will be
     * @param game {@code Game} object, for moving screens, etc.
     */
    public GameScreen(int i, ColorShooter game) {
        super(game);
        level = i;
    }

    /**
     * Creates a GameScreen with the level set to Bonus
     * @param game {@code Game} object, for moving screens, etc.
     */
    public GameScreen(ColorShooter game) {
        super(game);
        level = -1;
    }

    @Override
    public void show() {
        screenState = 0;
        lastLevel = level;
        pointMultiplier = 1.0f;
        oldScore = points;

        engine = new Engine();
        stage.clear();

        stage.getBatch().setColor(Color.WHITE);
        stage.getViewport().apply();

        if (player != null && !player.getDisposed()) {
            lastHealth = hm.get(player).health;
            lastMax = hm.get(player).maxHealth;
        } else {
            lastHealth = 0;
        }

        endRespawnTime = 4f;
        endMultiTime = 3f;
        resetRespawnTimer();
        resetMultiTimer();
        setUpHUD();

        //set up systems
        if (movementSystem == null) {
            movementSystem = new MovementSystem(1);
            playerInputSystem = new PlayerInputSystem(this, 2);
            healthSystem = new HealthSystem(3);
            shootingSystem = new ShootingSystem(4);
            drawingSystem = new DrawingSystem(5, getPlayer(),stage.getBatch());
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
            itemReceivableSystem = new ItemReceivableSystem(16);
        }


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
        engine.addSystem(itemReceivableSystem);

    }

    @Override
    public void render(float delta) {

        if (background != null) {
           stage.getBatch().begin();
           if (screenState == 1)
               stage.getBatch().setColor(Color.GRAY);
            stage.getBatch().draw(background, 0, 0, stage.getWidth(), stage.getHeight());
            stage.getBatch().end();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB))
            if (screenState == 0)
                pause();
            else if (screenState == 3)
                resume();


        if (screenState == 3)
            return;

        if (screenState == 1) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if (bestMultiplier >= 3f)
                    lives += 1;
                if (!player.getDisposed() && hm.get(player).maxHealth == hm.get(player).health)
                    points += 5000;
                COLOR_SHOOTER.setScreen(getNextLevel());
                return;
            }
            rouletteScore();
            updateVictoryHUD();
            stage.draw();
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
        stage.draw();

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
        table.clear();

        screenState = 3;

        for (EntitySystem system : getEngine().getSystems()) {
            engine.removeSystem(system);
        }
        engine.removeAllEntities();
    }

    @Override
    public void dispose() {

    }

    public void setUpHUD() {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        uiatlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), uiatlas);

        icon = new Image(ImageComponent.atlas.findRegion("PlayerShip"));
        icon.setSize(65, 65);

        table = new Table(skin);
        table.setSize(getStage().getWidth(), getStage().getHeight());
        table.setFillParent(true);

        param.size = 20;
        healthLabel = new Label("Health : 200 / 200", new Label.LabelStyle(gen.generateFont(param), Color.WHITE));
        //healthBar = new Image(new TextureAtlas("CSNinePatch.pack").createPatch("barpatch"));
        //healthBar = new Image(new Texture("GreenBar.png"));
        if (level >= 1) {
            levelLabel = new Label("Level:", skin);
            param.size = 20;
            levelNum = new Label("" + level, new Label.LabelStyle(gen.generateFont(param), Color.WHITE));
        } else {
            levelLabel = new Label("Bonus!", skin);
        }

        if (level <= 0)
            levelLabel.setColor(Color.YELLOW);
        else if (level <= 10 && level >= 1)
            levelNum.setColor(Color.ORANGE);
        else if (level >= 11 && level <= 16)
            levelNum.setColor(Color.CYAN);
        else if (level >= 17 && level <= 21)
            levelNum.setColor(Color.YELLOW);
        else if (level >= 22)
            levelNum.setColor(Color.PURPLE);

        life = new Label("Lives:", skin);
        lifeCount = new Label("-", skin);
        pointID = new Label("Points:", skin);
        pointNum = new Label("0", skin);
        timeLabel = new Label("-:--", skin);
        timeLabel.setColor(Color.ORANGE);

        param.size = 30;
        pointMultiplierLabel = new Label("" + pointMultiplier, new Label.LabelStyle(gen.generateFont(param), Color.WHITE));

        objectiveLabel = new Label("Defeat All Enemies!", skin);

        //  BACKUP!!!
        //table.setFillParent(true);
        table.top().left();
        table.padTop(22).padLeft(10);
        table.add(icon).size(65, 65);
        table.add(healthLabel).size(170f, 30f).padLeft(5);
        //table.add(healthBar).size(170f, 30f).padLeft(5);
        if (level >= 1)
            table.add(levelLabel).padLeft(400);
        else
            table.add(levelLabel).padLeft(400);
        if (levelNum != null)
            table.add(levelNum).size(20f, 20f).padLeft(10);
        table.add().padLeft(530);
        table.add(life).padLeft(5);
        table.add(lifeCount).padLeft(10);
        table.row();
        table.add(pointID).padLeft(5).padBottom(10f).padTop(10f);
        table.add(pointNum).left();
        //table.add(healthBar);
        if (levelNum == null)
            table.add(timeLabel).padLeft(400);
        else
            table.add(timeLabel).colspan(2).padLeft(400);
        table.row();
        table.add("Combo Multiplier").padTop(710);
        table.row();
        table.add(pointMultiplierLabel).padTop(5f);
        table.add();
        table.add(objectiveLabel).colspan(5).right();

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
        stage.addActor(table);
        gen.dispose();
    }

    private void updateHUD() {
        pointMultiplierLabel.setText("" + pointMultiplier);
        pointMultiplierLabel.setColor(1f, 1.25f - (pointMultiplier / 4), 1.25f - (pointMultiplier / 4), 1f);

        if (!player.getDisposed()) {

            if (lastHealth != hm.get(player).health || lastMax != hm.get(player).maxHealth) {
                lastHealth = hm.get(player).health;
                lastMax = hm.get(player).maxHealth;
                //table.getCell(healthBar).width(healthBar.getWidth() * lastHealth / lastMax);
                if (lastHealth > 0)
                    healthLabel.setText("Health : " + lastHealth + " / " + lastMax);
                else
                    healthLabel.setText("Health : " + 0 + " / " + lastMax);
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
        }

        healthLabel.toFront();
        icon.toFront();
    }

    public void showVictoryHUD() {
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 30;
        param.borderColor = new Color(0.2f, 0.2f, 1, 1);
        param.borderWidth = 2;
        Label victoryText = new Label("Level Complete!", new Label.LabelStyle(gen.generateFont(param), Color.WHITE));
        victoryText.setColor(Color.CYAN);
        table.clearChildren();
        table.center();
        table.add(victoryText).padBottom(50f);
        table.row();
        table.add(icon).padBottom(30f);
        table.row();
        table.add("Lives : " + lives).padBottom(10f);
        table.row();
        if (!player.getDisposed())
            table.add("Health : " + hm.get(player).health + " / " + hm.get(player).maxHealth).padBottom(10f);
        else {
            icon = new Image(ImageComponent.atlas.findRegion("GhostPlayerShip"));
            table.add("Health : 0 / " + lastMax).padBottom(10f);
        }
        table.row();
        pointNum.setText("" + oldScore);
        table.add(pointNum).padBottom(10f);
        table.row();
        Label bestMultiLabel = new Label("Best Score Multiplier : " + bestMultiplier, skin);
        bestMultiLabel.setColor(1.25f  - (bestMultiplier / 4), 1f, 1.25f - (bestMultiplier / 4), 1f);

        if (bestMultiplier > 3f || (!player.getDisposed() && hm.get(player).maxHealth == hm.get(player).health)) {
            table.add(bestMultiLabel).padBottom(10f);
            table.row();
            if (bestMultiplier > 3f) {
                Label bonusText = new Label("Multiplier Bonus! : + 1 Life", skin);
                bonusText.setColor(Color.CYAN);
                table.add(bonusText).padBottom(10f);
                table.row();
            }
            if (!player.getDisposed() && hm.get(player).maxHealth == hm.get(player).health) {
                Label healthBonusText = new Label("Full Health Bonus! : + 5,000 Points", skin);
                newScore += 5000;
                healthBonusText.setColor(Color.CYAN);
                table.add(healthBonusText).padBottom(10f);
                table.row();
            }
            table.add().padBottom(40f);
            table.row();
        } else {
            table.add(bestMultiLabel).padBottom(40f);
            table.row();
        }
        Label screenText = new Label("Press ENTER to continue", skin);
        screenText.setColor(Color.YELLOW);
        table.add(screenText);
        gen.dispose();
    }

    public void updateVictoryHUD() {
        pointNum.setText("" + oldScore);
    }

    public void rouletteScore() {
        if (newScore > oldScore) {
            if (newScore - oldScore > 10000)
                oldScore += 1000;
            else if (newScore - oldScore > 1000)
                oldScore += 100;
            else if (newScore - oldScore > 100)
                oldScore += 10;
            else
                oldScore += 1;
        }

        if (newScore < oldScore) {
            if (Math.abs(newScore - oldScore) > 10000)
                oldScore -= 1000;
            else if (Math.abs(newScore - oldScore) > 1000)
                oldScore -= 100;
            else if (Math.abs(newScore - oldScore) > 100)
                oldScore -= 10;
            else
                oldScore -= 1;
        }
    }

    public void colorHUD(Color color) {
        if (!color.equals(healthLabel.getColor()))
            healthLabel.setColor(color);
    }

    public Stage getStage() {
        return stage;
    }

    public Table getTable() {
        return table;
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

    public void setBackground(TextureRegion tex) {
        background = tex;

    }

    public void setPlayer(GameEntity p) {
        player = p;
        drawingSystem.setPlayer(p);
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
            if (timer.checkIfFinished()) {
                screenState = 1;
                newScore = points;
            }
        } else {
            if (engine.getEntitiesFor(Family.all(PositionComponent.class, AIComponent.class).get()).size() <= 0) {
                currentVictoryTime += dt;
                if (currentVictoryTime >= victoryEndTime) {
                    newScore = points;
                    screenState = 1;
                }
            } else {
                currentVictoryTime = 0f;
            }
        }
    }

    public Screen getNextLevel() {
        System.out.println("non overided!");
        return null;
    }

    public void incrementRespawnTimer(float dt) {
        currentRespawnTime += dt;
        if (currentRespawnTime >= endRespawnTime) {
            screenState = 2;
        }
    }

    public void incrementMultiTimer(float dt) {
        currentMultiTime += dt;
        if (currentMultiTime >= endMultiTime) {
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

    public static int getPoints() {
        return points;
    }

    public static void setPoints(int p) {
        points = p;
    }

    public static void incrementPoints(int p) {
        points += p;
    }

    public static int getLastLevel() {
        return lastLevel;
    }

    public static void setLastLevel(int last) {
        lastLevel = last;
    }

    public GameTimer getTimer() {
        return timer;
    }

    public void setTimer(GameTimer t) {
        timer = t;
        timeLabel.setColor(Color.WHITE);
        objectiveLabel.setText("Survive the Clock");

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

    public static int getLives() {
        return lives;
    }

    public static void setLives(int i) {
        lives = i;
    }

    public int getLevel() {
        return level;
    }

    public ColorShooter getGame() {
        return COLOR_SHOOTER;
    }
}
