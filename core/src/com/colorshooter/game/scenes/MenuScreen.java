package com.colorshooter.game.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.colorshooter.game.ColorShooter;

/**
 * Created by pnore_000 on 8/28/2016.
 */
public class MenuScreen implements Screen{

    protected final ColorShooter COLOR_SHOOTER;
    protected Stage stage;
    protected Skin skin;
    protected Table table;

    private TextureAtlas uiatlas;

    public MenuScreen(ColorShooter game) {
        super();
        COLOR_SHOOTER = game;
    }

    @Override
    public void show() {
        stage = new Stage();

        table = new Table();
        table.setSize(stage.getWidth(), stage.getHeight());
        table.setFillParent(true);

        uiatlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.addRegions(uiatlas);

        table.setSkin(skin);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    public String toString() {
        return "" + this.getClass();
    }


}
