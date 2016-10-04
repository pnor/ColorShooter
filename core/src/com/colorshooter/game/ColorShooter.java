package com.colorshooter.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.colorshooter.game.components.ImageComponent;
import com.colorshooter.game.scenes.DisplayScreen;
import com.colorshooter.game.scenes.GameScreen;
import com.colorshooter.game.scenes.MenuScreen;
import com.colorshooter.game.scenes.levels.*;
import com.colorshooter.game.scenes.menus.*;

import static com.colorshooter.game.Mappers.hm;

public class ColorShooter extends Game {

	public void create() {
		setScreen(new MainMenu(this));
	}

	public void resize (int width, int height) {
		super.resize(width, height);
	}

	public void render () {

		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			if (getScreen() instanceof MainMenu)
				Gdx.app.exit();
			else {
				setScreen(new MainMenu(this));
				GameScreen.setPoints(0);
			}
		}

		//screen moving with dpad
		/*
		if (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_LEFT)) {
			setScreen(new CongratsMenu(this));
		}

		if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) {
			if (getScreen() instanceof GameScreen)
				setScreen(((GameScreen) getScreen()).getNextLevel());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
			setScreen(new EnterScoreMenu(this));
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
			setScreen(new HighScoreMenu(this));
		}
		*/

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (getScreen() instanceof GameScreen && ((GameScreen) getScreen()).getScreenState() == 2) {
			if (GameScreen.getLives() < 1) {
				Array<HighScore> highScores = ((DisplayScreen) getScreen()).getHighScores();
				if (highScores.size < 10 || GameScreen.getPoints() >= highScores.get(highScores.size - 1).getScore()) {
					GameScreen.setLives(3);
					setScreen(new EnterScoreMenu(((GameScreen) getScreen()).getLevel(), this));
				}
				else {
					GameScreen.setLives(3);
					setScreen(new DeathMenu(((GameScreen) getScreen()).getLevel(), this));
				}
			} else {
				getScreen().show();
				((GameScreen) getScreen()).reset();
				GameScreen.incrementPoints(-1 * MathUtils.clamp(GameScreen.getPoints() / 4, 0, 50000));
				hm.get(((GameScreen) getScreen()).getPlayer()).invincible = true;
			}
		} else {
			getScreen().render(MathUtils.clamp(Gdx.graphics.getDeltaTime(), 0f, 1f));
		}
	}

	public void dispose() {
		super.dispose();
		ImageComponent.dispose();
		DisplayScreen.release();
	}

	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}
}

