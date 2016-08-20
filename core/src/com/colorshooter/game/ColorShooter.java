package com.colorshooter.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.colorshooter.game.scenes.levels.Level1;
import com.colorshooter.game.scenes.levels.Level2;
import com.colorshooter.game.scenes.tests.*;

public class ColorShooter extends Game {
	/* test screens
	private final Screen[] SCREENS = {
			new PlayerTest(0),
			new WallTest(0),
			new ShootingTest(0),
			new EnemyTest(0),
			new HUDTest(0)
	};
	*/

	private final Screen[] SCREENS = {
			new Level1(),
			new Level2()
	};

	private int index;

	public void create() {
		setScreen(SCREENS[index]);
	}

	public void resize (int width, int height) {
		super.resize(width, height);
	}

	public void render () {
		int lastIndex = index;

		if (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_RIGHT)) {
			index++;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.DPAD_LEFT)) {
			index--;
		}

		if (index != lastIndex) {
			index = (SCREENS.length + index) % SCREENS.length;
			setScreen(SCREENS[index]);
			System.out.println("Current Screen: " + SCREENS[index]);
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (getScreen() instanceof GameScreen && ((GameScreen) getScreen()).getReset()) {
			getScreen().show();
			((GameScreen) getScreen()).reset();

		} else {
			getScreen().render(Gdx.graphics.getDeltaTime());
		}
	}

	public void dispose() {
		super.dispose();
	}

	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}

	public Screen getScreen() {
		return super.getScreen();
	}

}

