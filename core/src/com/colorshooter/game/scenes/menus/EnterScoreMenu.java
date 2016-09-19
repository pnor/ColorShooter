package com.colorshooter.game.scenes.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.colorshooter.game.ColorShooter;
import com.colorshooter.game.HighScore;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.GameScreen;
import com.colorshooter.game.scenes.MenuScreen;
import com.colorshooter.game.scenes.levels.Level1;

import java.util.ArrayList;

/**
 * @author pnore_000
 */
public class EnterScoreMenu extends MenuScreen {

    private String playerName;
    private int playerPoints;
    private int lastLevel;
    private Array<HighScore> highScores;

    private Label playerPointLabel;
    private TextField nameText;
    private TextButton confirm;

    private Label infoMessage;
    private boolean addedInfoMessage;

    public EnterScoreMenu(ColorShooter game) {
        super(game);
    }

    public EnterScoreMenu(int lastLev, ColorShooter game) {
        super(game);
        lastLevel = lastLev;
    }

    @Override
    public void show() {
        super.show();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        setBackgound(ImageComponent.backgroundAtlas.findRegion("HighScores"));

        playerPoints = GameScreen.getPoints();

        highScores = getHighScores();

        param.size = 25;
        Label title = new Label("New High Score!", new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        param.size = 20;
        playerPointLabel = new Label("" + playerPoints, new Label.LabelStyle(generator.generateFont(param), Color.WHITE));
        playerPointLabel.setColor(Color.YELLOW);
        nameText = new TextField("ABC", skin);
        confirm = new TextButton("Confirm", skin, "toggle");

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((Button) actor).isChecked()) {
                    if (actor == confirm) {
                        Json json = new Json();
                        String score = "";

                        highScores.add(new HighScore(nameText.getText(), playerPoints, lastLevel));
                        sortHighScores(highScores, 0, highScores.size - 1);
                        highScores.truncate(10);;

                        score = json.prettyPrint(highScores);

                        FileHandle fileHandle = Gdx.files.local("highScores.json");
                        fileHandle.writeString(score, false);

                        GameScreen.setPoints(0);
                        getGame().setScreen(new HighScoreMenu(getGame()));
                    }
                }
            }
        };

        confirm.addListener(changeListener);


        table.center().setFillParent(true);
        table.add(title).padBottom(30f);
        table.row();
        table.add(playerPointLabel).padBottom(30f);;
        table.row();
        table.add("Enter Name:").padBottom(30f);
        table.row();
        table.add(nameText).size(100f, 30f).padBottom(30f);
        table.row();
        table.add(confirm).size(80, 30).padBottom(10f);
        table.row();

        stage.addActor(table);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!addedInfoMessage && nameText.getText().length() > 10) {
            addedInfoMessage = true;
            infoMessage = new Label("Can't be longer than 10 characters", skin);
            infoMessage.setColor(Color.RED);
            table.add(infoMessage);
        }

        if (nameText.getText().length() > 10) {
            nameText.setText(nameText.getText().substring(0, 10));
        }
    }

    public Array<HighScore> sortHighScores(Array<HighScore> array, int start, int end) {
        if (array == null)
            return null;

        HighScore temp;
        int pivot = (int) (Math.random() * end - start) + start;
        int s = start;
        int e = end;

        while (s < e) {
            while (array.get(s).compareTo(array.get(pivot)) == 1)
                s++;

            while (array.get(e).compareTo(array.get(pivot)) == -1)
                e--;

            if (s <= e) {
                temp = array.get(s);
                array.set(s, array.get(e));
                array.set(e, temp);
                s++;
                e--;
            }
        }

        if (s < end)
            sortHighScores(array, s, end);
        if (e > start)
            sortHighScores(array, start, e);

        return array;
    }
}
