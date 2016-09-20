package com.colorshooter.game.scenes.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.*;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.HighScore;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.MenuScreen;

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
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        setBackgound(ImageComponent.backgroundAtlas.findRegion("Space1"));

        scores = getHighScores();

        Label title;
        param.size = 35;
        param.shadowColor = Color.BLUE;
        param.shadowOffsetX = 2;
        param.shadowOffsetY = 2;
        Label.LabelStyle lblStyle = new Label.LabelStyle(generator.generateFont(param), Color.WHITE);
        title = new Label("High Scores", lblStyle);
        title.setColor(Color.CYAN);

        Label name;
        param.size = 20;
        param.shadowOffsetX = 0;
        param.shadowOffsetY = 0;
        lblStyle = new Label.LabelStyle(generator.generateFont(param), Color.WHITE);
        name = new Label("Name", lblStyle);

        Label score = new Label("Score", lblStyle);

        Label lastLevel = new Label("Last Level", lblStyle);

        Label[] scoreLabels = new Label[scores.size * 3];

        table.center().setFillParent(true);
        table.add(title).padBottom(20f).colspan(3);
        table.row();
        table.add().width(200f);
        table.add().width(200f);
        table.add().width(200f);
        table.row();
        table.add(name).height(50f).align(Align.center);
        table.add(score).height(50f);
        table.add(lastLevel).height(50f);
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
        generator.dispose();
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
