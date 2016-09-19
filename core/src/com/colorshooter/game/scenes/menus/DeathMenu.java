package com.colorshooter.game.scenes.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;
import com.colorshooter.game.scenes.MenuScreen;

/**
 * @author pnore_000
 */
public class DeathMenu extends MenuScreen{
    private int lastLevel;

    public DeathMenu(ColorShooter game) {
        super(game);
    }

    public DeathMenu(int lastLev, ColorShooter game) {
        super(game);
        lastLevel = lastLev;
    }

    @Override
    public void show() {
        super.show();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        setBackgound(ImageComponent.backgroundAtlas.findRegion("HighScores"));

        int playerPoints = GameScreen.getPoints();

        param.size = 35;
        Label title = new Label("Game Over", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        param.size = 30;
        Label playerPointLabel = new Label("" + playerPoints, new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        playerPointLabel.setColor(new Color(1f, 1, 0.9f, 1));
        param.size = 20;
        Label lastLevelLabel = new Label("" + lastLevel, new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        lastLevelLabel.setColor(new Color(0.9f, 0.9f, 1f, 1));

        table.center().setFillParent(true);
        table.add(title).padBottom(30f);
        table.row();
        table.add(playerPointLabel).padBottom(20f);
        table.row();
        table.add(lastLevelLabel);

        stage.addActor(table);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            GameScreen.setPoints(0);
            getGame().setScreen(new MainMenu(getGame()));
        }
    }
}
