package com.colorshooter.game.scenes.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Json;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.HighScore;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;
import com.colorshooter.game.scenes.MenuScreen;
import com.colorshooter.game.scenes.levels.*;

/**
 * @author pnore_000
 */
public class LevelChooseMenu extends MenuScreen {

    public LevelChooseMenu(ColorShooter game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        setBackgound(ImageComponent.backgroundAtlas.findRegion("CubeSpace2"));

        param.size = 45;
        Label title = new Label("Choose a Level (1 - 30)", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        final TextField lvlText = new TextField("", skin);
        final TextButton confirm = new TextButton("Confirm", skin, "toggle");

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((Button) actor).isPressed()) {
                    if (actor == confirm) {
                        String level = lvlText.getText().trim();

                        if (level.length() == 1) {
                            if (level.equals("1"))
                                getGame().setScreen(new Level1(getGame()));
                            else if (level.equals("2"))
                                getGame().setScreen(new Level2(getGame()));
                            else if (level.equals("3"))
                                getGame().setScreen(new Level3(getGame()));
                            else if (level.equals("4"))
                                getGame().setScreen(new Level4(getGame()));
                            else if (level.equals("5"))
                                getGame().setScreen(new Level5(getGame()));
                            else if (level.equals("6"))
                                getGame().setScreen(new Level6(getGame()));
                            else if (level.equals("7"))
                                getGame().setScreen(new Level7(getGame()));
                            else if (level.equals("8"))
                                getGame().setScreen(new Level8(getGame()));
                            else if (level.equals("9"))
                                getGame().setScreen(new Level9(getGame()));
                            else
                                lvlText.setText("");

                        } else if (level.length() == 2){
                            if (level.equals("10"))
                                getGame().setScreen(new Level10(getGame()));
                            else if (level.equals("11"))
                                getGame().setScreen(new Level11(getGame()));
                            else if (level.equals("12"))
                                getGame().setScreen(new Level12(getGame()));
                            else if (level.equals("13"))
                                getGame().setScreen(new Level13(getGame()));
                            else if (level.equals("14"))
                                getGame().setScreen(new Level14(getGame()));
                            else if (level.equals("15"))
                                getGame().setScreen(new Level15(getGame()));
                            else if (level.equals("16"))
                                getGame().setScreen(new Level16(getGame()));
                            else if (level.equals("16"))
                                getGame().setScreen(new Level16(getGame()));
                            else if (level.equals("17"))
                                getGame().setScreen(new Level17(getGame()));
                            else if (level.equals("18"))
                                getGame().setScreen(new Level18(getGame()));
                            else if (level.equals("19"))
                                getGame().setScreen(new Level19(getGame()));
                            else if (level.equals("20"))
                                getGame().setScreen(new Level20(getGame()));
                            else if (level.equals("21"))
                                getGame().setScreen(new Level21(getGame()));
                            else if (level.equals("22"))
                                getGame().setScreen(new Level22(getGame()));
                            else if (level.equals("23"))
                                getGame().setScreen(new Level23(getGame()));
                            else if (level.equals("24"))
                                getGame().setScreen(new Level24(getGame()));
                            else if (level.equals("25"))
                                getGame().setScreen(new Level25(getGame()));
                            else if (level.equals("26"))
                                getGame().setScreen(new Level26(getGame()));
                            else if (level.equals("27"))
                                getGame().setScreen(new Level27(getGame()));
                            else if (level.equals("28"))
                                getGame().setScreen(new Level28(getGame()));
                            else if (level.equals("29"))
                                getGame().setScreen(new Level29(getGame()));
                            else if (level.equals("30"))
                                getGame().setScreen(new Level30(getGame()));
                            else
                                lvlText.setText("");

                        } else {
                            lvlText.setText("");
                        }
                    }
                }
            }
        };

        confirm.addListener(changeListener);


        table.center().setFillParent(true);
        table.add(title).padBottom(30f);
        table.row();
        table.add(lvlText).size(50f, 30f).padBottom(30f);
        table.row();
        table.add(confirm).size(80, 30).padBottom(30f);
        table.row();
        table.add("Press ESCAPE to leave.").padBottom(30f);
        table.row();

        stage.addActor(table);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

    }
}
