package com.colorshooter.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.colorshooter.game.scenes.*;

public class ColorShooter extends Game {

	private final Screen[] SCREENS = {
			new PlayerTest(),
			new WallTest(),
			new ShootingTest(),
			new EnemyTest(),
			new HUDTest()
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

