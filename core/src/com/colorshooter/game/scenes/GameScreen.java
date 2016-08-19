package com.colorshooter.game.scenes;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.colorshooter.game.GameEntity;
import com.colorshooter.game.HUDActor;
import com.colorshooter.game.components.ImageComponent;

import static com.colorshooter.game.Mappers.*;

/**
 * Created by pnore_000 on 7/4/2016.
 */
public class GameScreen implements Screen {

    private Stage stage;
    private SpriteBatch batch;
    private ShapeRenderer shapes;
    private Engine engine;

    private TextureRegion background;

    private Table table;
    private TextureAtlas uiatlas;
    private Skin skin;
    private Label healthLabel;
    private Label level;
    private Label levelNum;
    private Label life;
    private Label lifeCount;
    private HUDActor icon;
    private Label pointID;
    private Label pointNum;

    private static GameEntity player;

    private static int lives = 5;
    private static int points;
    private float currentRespawnTime;
    private float endRespawnTime;

    private boolean reset;

    private int lastHealth;
    private int lastMax;

    @Override
    public void show() {
        stage = new Stage();
        batch = new SpriteBatch();
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
            batch.begin();
            batch.draw(background, 0, 0, stage.getWidth(), stage.getHeight());
            batch.end();
        }

        checkDeath(delta);

        updateHUD();

        getBatch().begin();
        table.draw(getBatch(), 1);
        getBatch().end();
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
        batch.dispose();
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
        level = new Label("Level:", skin);
        levelNum = new Label("1", skin);
        life = new Label("Lives:", skin);
        lifeCount = new Label("-", skin);
        pointID = new Label("Points:", skin);
        pointID.setFontScale(0.8f);
        pointNum = new Label("0", skin);
        pointNum.setFontScale(0.8f);

        table.top().left();
        table.pad(40);
        table.add(icon);
        table.add(healthLabel).pad(10);
        table.add(level).pad(5);
        table.add(levelNum);
        table.add(life).pad(5);
        table.add(lifeCount);
        table.row();
        table.add();
        table.add();
        table.add(pointID).pad(5);
        table.add(pointNum);

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
        }


        healthLabel.toFront();
        icon.toFront();
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

    public SpriteBatch getBatch() {
        return batch;
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


}
