package com.colorshooter.game.scenes.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.MenuScreen;

/**
 * @author pnore_000
 */
public class HowToPlayScreen extends MenuScreen{

    public HowToPlayScreen(ColorShooter game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        setBackgound(ImageComponent.backgroundAtlas.findRegion("GraySpace2"));


        param.size = 35;
        param.borderWidth = 1;
        param.borderColor = Color.LIGHT_GRAY;
        Label title = new Label("Controls", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        Label tipsTitle = new Label("Tips", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));

        param.borderWidth = 0;
        param.size = 20;;
        Label description = new Label("Fend off against the aliens of the Spectrum Solar System!", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));

        param.color = new Color(0.95f, 0.95f, 1, 1);
        Label movementInfo = new Label("Use WASD for Movement", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        Label shootingInfo = new Label("Hold down the SPACE BAR to shoot", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        Label turboInfo = new Label("Hold down SHIFT and a WASD Key to move faster", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));

        Label itemsInfo = new Label("Collect items to regain health or increase your ship's attributes", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        Label colorInfo = new Label("Collecting Color Stars will power up your ship in a unique way", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));

        param.color = new Color(1, 0.2f, 0.2f, 1);
        Label escapeInfo = new Label("Press ESCAPE to return to the Main Menu", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));

        Image healthImage = new Image(ImageComponent.atlas.findRegion("Life1"));
        Image speedImage = new Image(ImageComponent.atlas.findRegion("SpeedUp"));
        Image shootImage = new Image(ImageComponent.atlas.findRegion("ShootUp"));

        Image colorImage = new Image(ImageComponent.atlas.findRegion("RedPower1"));


        table.center().setFillParent(true);
        table.add().width(200f);
        table.add().width(200f);
        table.add().width(200f);
        table.row();

        table.add(description).colspan(3).padBottom(50f);
        table.row();

        table.add(title).colspan(3).padBottom(40f);
        table.row();
        table.add(movementInfo).colspan(3).padBottom(20f).row();
        table.add(shootingInfo).colspan(3).padBottom(20f).row();
        table.add(turboInfo).colspan(3).padBottom(50f);
        table.row();

        table.add(tipsTitle).colspan(3).padBottom(30f);
        table.row();
        table.add(itemsInfo).colspan(3).padBottom(20f).row();
        table.add(healthImage).right();
        table.add(speedImage);
        table.add(shootImage).left().row();
        table.add(colorInfo).padTop(20f).padBottom(20f).colspan(3).row();
        table.add();
        table.add(colorImage);

        table.row();
        table.add();
        table.add(escapeInfo).padTop(60f);

        stage.addActor(table);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            getGame().setScreen(new MainMenu(getGame()));
        }
    }
}
