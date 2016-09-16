package com.colorshooter.game.scenes.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.MenuScreen;
import com.colorshooter.game.scenes.levels.Level1;

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

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        setBackgound(ImageComponent.backgroundAtlas.findRegion("ColorSpace"));

        table.center().setFillParent(true);

        param.size = 40;
        param.borderColor = new Color(1, 1, 0.8f, 1f);
        param.borderWidth = 1f;
        BitmapFont title = gen.generateFont(param);
        Label.LabelStyle labelStyle= new Label.LabelStyle(title, Color.WHITE);
        Label titleLabel= new Label("Color Shooter", labelStyle);

        table.add(titleLabel).padBottom(75f);

        final TextButton start = new TextButton("Start", skin, "toggle");

        final TextButton highScores = new TextButton("High Scores", skin, "toggle");

        final TextButton quit = new TextButton("Quit", skin, "toggle");

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((Button) actor).isChecked()) {
                    if (actor == start)
                        COLOR_SHOOTER.setScreen(new Level1(getGame()));
                    if (actor == highScores)
                        COLOR_SHOOTER.goToHighScores();
                    if (actor == quit)
                        Gdx.app.exit();
                }
            }
        };

        start.addListener(changeListener);
        highScores.addListener(changeListener);
        quit.addListener(changeListener);

        table.row();
        table.add(start).size(150f, 30f).padBottom(30f);
        table.row();
        table.add(highScores).size(150f, 30f).padBottom(30f);
        table.row();
        table.add(quit).size(150f, 30f).padBottom(30f);

        stage.addActor(table);

        gen.dispose();
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
