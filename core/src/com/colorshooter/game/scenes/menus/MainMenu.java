package com.colorshooter.game.scenes.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.scenes.MenuScreen;

/**
 * Created by pnore_000 on 8/28/2016.
 */
public class MainMenu extends MenuScreen implements Screen{

    public MainMenu(ColorShooter game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        table.center().setFillParent(true);
        table.add("Color Shooter").padBottom(20f);

        final TextButton start = new TextButton("Start", skin, "toggle");

        final TextButton highScores = new TextButton("High Scores", skin, "toggle");

        final TextButton quit = new TextButton("Quit", skin, "toggle");

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((Button) actor).isChecked()) {
                    if (actor == start)
                        COLOR_SHOOTER.setScreen(1);
                    if (actor == highScores)
                        COLOR_SHOOTER.setScreen(10);
                    if (actor == quit)
                        Gdx.app.exit();
                }
            }
        };

        start.addListener(changeListener);
        highScores.addListener(changeListener);
        quit.addListener(changeListener);

        table.row();
        table.add(start).size(120f, 25f).padBottom(20f);
        table.row();
        table.add(highScores).size(120f, 25f).padBottom(20f);
        table.row();
        table.add(quit).size(120f, 25f).padBottom(20f);

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
