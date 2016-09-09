package com.colorshooter.game.scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.HighScore;

/**
 * Created by pnore_000 on 8/28/2016.
 */
public class MenuScreen extends DisplayScreen{

    protected Table table;

    public MenuScreen(ColorShooter game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage();

        table = new Table();
        table.setSize(stage.getWidth(), stage.getHeight());
        table.setFillParent(true);

        table.setSkin(skin);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        super.hide();
        table.clear();
    }

    @Override
    public void dispose() {

    }

    public ColorShooter getGame() {
        return COLOR_SHOOTER;
    }
}
