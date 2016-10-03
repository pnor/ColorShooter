package com.colorshooter.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.HighScore;

/**
 * @author pnore_000
 */
public abstract class DisplayScreen implements Screen, Disposable{

    protected final ColorShooter COLOR_SHOOTER;
    protected static Stage stage;
    protected static Skin skin;
    private static TextureAtlas uiatlas;


    static {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        uiatlas = new TextureAtlas("uiskin.atlas");
        skin.addRegions(uiatlas);
    }

    public DisplayScreen(ColorShooter cs) {
        COLOR_SHOOTER = cs;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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
        stage.clear();
    }

    @Override
    public void dispose() {

    }

    /**
     * Disposes all the disposable static fields.
     */
    public static void release() {
        stage.dispose();
        skin.dispose();
        uiatlas.dispose();
    }

    /**
     * Gets an {@code Array} of {@code HighScore} objects from the file highScores.json
     * @return An {@code Array} of {@code HighScore}
     */
    public Array<HighScore> getHighScores() {
        Array<HighScore> returnArray;

        FileHandle fileHandle = Gdx.files.local("ColorWarsHighScores.json");
        if (!fileHandle.exists())
            return new Array<HighScore>();

        Json json = new Json();
        String jsonScores = fileHandle.readString();

        HighScore currScore;
        returnArray = json.fromJson(Array.class, jsonScores);
        return returnArray;
    }

    public String toString() {
        return "" + this.getClass();
    }

}
