package com.colorshooter.game.scenes.tests;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.GameTimer;
import com.colorshooter.game.HUDActor;
import com.colorshooter.game.components.AIComponent;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.components.PositionComponent;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/4/2016.
 */
public class GameScreen implements Screen {

    private Stage stage;
    //private SpriteBatch batch;
    private ShapeRenderer shapes;
    private Engine engine;

    private TextureRegion background;

    private Table table;
    private TextureAtlas uiatlas;
    private Skin skin;
    private Label healthLabel;
    private Label levelLabel;
    private Label levelNum;
    private Label life;
    private Label lifeCount;
    private HUDActor icon;
    private Label pointID;
    private Label pointNum;
    private Label timeLabel;

    private static GameEntity player;

    private int level;
    private static int lives = 5;
    private float currentRespawnTime;
    private float endRespawnTime;
    private static int points;
    private GameTimer timer;
    private boolean victory;
    private float currentVictoryTime;
    private float victoryEndTime = 3f;

    private boolean reset;

    private int lastHealth;
    private int lastMax;

    public GameScreen(int i) {
        super();
        level = i;
    }

    @Override
    public void show() {
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
        resetRespawnTimer();
        setUpHUD();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();

        if (background != null) {
           stage.getBatch().begin();
           if (victory)
               stage.getBatch().setColor(Color.DARK_GRAY);
            stage.getBatch().draw(background, 0, 0, stage.getWidth(), stage.getHeight());
            stage.getBatch().end();
        }
        if (victory)
            return;

        checkVictory(delta);
        checkDeath(delta);
        if (timer != null)
            timer.decreaseTimer(delta);
        updateHUD();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void setUpHUD() {
        uiatlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.addRegions(uiatlas);

        icon = new HUDActor(ImageComponent.atlas.findRegion("PlayerShip"));
        icon.setSize(55, 55);

        table = new Table(skin);
        table.setSize(getStage().getWidth(), getStage().getHeight());
        table.setFillParent(true);

        healthLabel = new Label("Health : 150 / 150", skin);
        healthLabel.setFontScale(1.25f, 1.25f);
        levelLabel = new Label("Level:", skin);
        levelNum = new Label("" + level, skin);
        life = new Label("Lives:", skin);
        lifeCount = new Label("-", skin);
        pointID = new Label("Points:", skin);
        pointID.setFontScale(0.8f);
        pointNum = new Label("0", skin);
        pointNum.setFontScale(0.8f);
        if (timer != null)
            timeLabel = new Label(timer.toString(), skin);
        else
            timeLabel = new Label("-:--", skin);

        table.top().left();
        table.pad(20);
        table.add(icon);
        table.add(healthLabel).padLeft(10);
        table.add(levelLabel).padLeft(400);
        table.add(levelNum).padLeft(10);
        table.add().padLeft(600);
        table.add(life).padLeft(5);
        table.add(lifeCount).padLeft(10);
        table.row();
        table.add(pointID).padLeft(5);
        table.add(pointNum);
        table.add(timeLabel).padLeft(400);

        table.debug();
    }

    private void updateHUD() {
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
                icon.setTex(im.get(player).texRegion);
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
        Label victoryText = new Label("Victory!", skin);
        victoryText.setFontScale(1.25f);
        table.clearChildren();
        table.center();
        table.add(victoryText).padBottom(100f);
        table.row();
        table.add(icon).padBottom(50f);
        table.row();
        table.add("" + points);
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
                victory = true;
        } else if (timer == null) {
            if (engine.getEntitiesFor(Family.all(PositionComponent.class, AIComponent.class).get()).size() <= 0) {
                currentVictoryTime += dt;
                if (currentVictoryTime >= victoryEndTime)
                    victory = true;
            }
        }
    }

    public void incrementRespawnTimer(float dt) {
        currentRespawnTime += dt;
        if (currentRespawnTime >= endRespawnTime) {
            setReset(true);
        }
    }

    public void reset() {
        lives -= 1;
        if (lives < 0)
            Gdx.app.exit();
        reset = false;
    }

    public void resetRespawnTimer() {
        currentRespawnTime = 0f;
    }

    public void setReset(boolean r) {
        reset = r;
    }

    public boolean getReset() {
        return reset;
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

    public boolean getVictory() {
        return victory;
    }

    public void setVictory(boolean b) {
        victory = b;
    }
}
