package com.colorshooter.game.scenes.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.*;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.HighScore;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.MenuScreen;

import java.util.ArrayList;

/**
 * @author pnore_000
 */
public class HighScoreMenu extends MenuScreen{

    private Array<HighScore> scores;

    public HighScoreMenu(ColorShooter game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        setBackgound(ImageComponent.backgroundAtlas.findRegion("Space1"));

        scores = getHighScores();

        Label title = new Label("High Scores", skin);
        title.setFontScale(1.2f);
        title.setColor(Color.CYAN);
        Label[] scoreLabels = new Label[scores.size * 3];


        table.center().setFillParent(true);
        table.add(title).padBottom(20f).colspan(3);
        table.row();
        table.add().width(200f);
        table.add().width(200f);
        table.add().width(200f);
        table.row();
        table.add("Name").height(50f).align(Align.center);
        table.add("Score").height(50f);
        table.add("Last Level").height(50f);
        table.row();

        int j;
        Color c;
        for (int i = 0; i < scores.size; i++) {
            j = (i + 1) * 3 - 1;
            scoreLabels[j - 2] = new Label(scores.get(i).getName(), skin);
            scoreLabels[j - 1] = new Label(String.format("%,8d%n", scores.get(i).getScore()), skin);
            scoreLabels[j] = new Label("" + scores.get(i).getLastLevel(), skin);

            c = new Color(1, 1f - ((10 - i) / 10f), 0, 1);
            scoreLabels[j - 2].setColor(c);
            scoreLabels[j - 1].setColor(c);
            scoreLabels[j].setColor(c);

            table.add(scoreLabels[j - 2]).height(50f);
            table.add(scoreLabels[j - 1]).height(50f);
            table.add(scoreLabels[j]).height(50f);
            table.row();
        }

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.TAB))
            getGame().setScreen(new MainMenu(getGame()));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
