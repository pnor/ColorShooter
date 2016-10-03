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
public class CongratsMenu extends MenuScreen{

    public CongratsMenu(ColorShooter game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        setBackgound(ImageComponent.backgroundAtlas.findRegion("ColorSpace"));

        int playerPoints = GameScreen.getPoints();

        param.size = 60;
        param.shadowColor = Color.DARK_GRAY;
        param.shadowOffsetX = 4;
        param.shadowOffsetY = 5;
        Label title = new Label("Congratulations!", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        param.size = 45;
        Label info = new Label("You have beaten all 30 levels!", skin);
        Label info2 = new Label("Press C + O + L + R on the title screen to choose any level!", skin);
        Label playerPointLabel = new Label("" + playerPoints, new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        playerPointLabel.setColor(Color.CYAN);

        table.center().setFillParent(true);
        table.add(title).padBottom(30f);
        table.row();
        table.add(playerPointLabel).padBottom(30f);
        table.row();
        table.add(info).padBottom(30f);
        table.row();
        table.add(info2).padBottom(30f);
        table.row();

        stage.addActor(table);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            if (getHighScores().get(9).getScore() >= GameScreen.getPoints()) {
                getGame().setScreen(new MainMenu(getGame()));
                GameScreen.setPoints(0);
            }
            else
                getGame().setScreen(new EnterScoreMenu(30, getGame()));
        }
    }
}
