package com.colorshooter.game.scenes.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.scenes.MenuScreen;

/**
 * @author pnore_000
 */
public class HighScoreMenu extends MenuScreen{

    public HighScoreMenu(ColorShooter game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        table.center().setFillParent(true);
        table.add("High Scores!!!!!").padBottom(20f);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
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
        super.dispose();
    }
}
