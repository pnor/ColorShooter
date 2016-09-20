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

        param.size = 80;
        param.color = new Color(1f, 0.95f, 0.95f, 1);
        param.borderColor = Color.WHITE; //new Color(1, 1, 0.8f, 1f);
        param.borderWidth = 1f;
        param.shadowColor = Color.BLUE;
        param.shadowOffsetY = -4;
        param.shadowOffsetX = -3;
        BitmapFont title = gen.generateFont(param);
        Label.LabelStyle labelStyle= new Label.LabelStyle(title, Color.WHITE);
        Label titleLabel= new Label("Color Wars", labelStyle);

        table.add(titleLabel).padBottom(75f);

        final TextButton start = new TextButton("Start", skin, "toggle");

        final TextButton highScores = new TextButton("High Scores", skin, "toggle");

        final TextButton howToPlay = new TextButton("How to Play", skin, "toggle");

        final TextButton quit = new TextButton("Quit", skin, "toggle");

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((Button) actor).isChecked()) {
                    if (actor == start)
                        COLOR_SHOOTER.setScreen(new Level1(getGame()));
                    if (actor == highScores)
                        COLOR_SHOOTER.setScreen(new HighScoreMenu(getGame()));
                    if (actor == howToPlay)
                        COLOR_SHOOTER.setScreen(new HowToPlayScreen(getGame()));
                    if (actor == quit)
                        Gdx.app.exit();
                }
            }
        };

        start.addListener(changeListener);
        highScores.addListener(changeListener);
        howToPlay.addListener(changeListener);
        quit.addListener(changeListener);

        table.row();
        table.add(start).size(150f, 30f).padBottom(30f);
        table.row();
        table.add(highScores).size(150f, 30f).padBottom(30f);
        table.row();
        table.add(howToPlay).size(150f, 30f).padBottom(30f);
        table.row();
        table.add(quit).size(150f, 30f).padBottom(30f);

        stage.addActor(table);

        gen.dispose();
    }
}
