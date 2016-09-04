package com.colorshooter.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.colorshooter.game.scenes.GameScreen;
import com.colorshooter.game.scenes.levels.*;
import com.colorshooter.game.scenes.menus.HighScoreMenu;
import com.colorshooter.game.scenes.menus.MainMenu;

import static com.colorshooter.game.Mappers.hm;

public class ColorShooter extends Game {

	private final Screen[] SCREENS = {
			new MainMenu(this),
			new HighScoreMenu(this),
			new Level1(this),
			new Level2(this),
			new Level3(this),
			new Level4(this),
			new Level5(this),
			new BonusLevel1(this),
			new Level6(this),
			new Level7(this),
			new Level8(this),
			new Level9(this),
			new Level10(this),
			new Level11(this),
			new Level12(this),
			new Level13(this),
			new Level14(this),
			new Level15(this),
			new BonusLevel2(this),
			new Level16(this),
			new Level17(this),
			new Level18(this),
			new Level19(this),
			new Level20(this),
			new BonusLevel3(this),
			new Level21(this),
			new Level22(this),
			new Level23(this),
			new Level24(this),
			new Level25(this),
			new Level26(this),
			new Level27(this),
			new Level28(this),
			new BonusLevel4(this),
			new Level29(this),
			new Level30(this)
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
		//screen moving with dpad
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
		//---

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (getScreen() instanceof GameScreen && ((GameScreen) getScreen()).getScreenState() == 2) {
			if (((GameScreen) getScreen()).getLives() < 1) {
				setScreen(0);
			} else {
				getScreen().show();
				((GameScreen) getScreen()).reset();
				((GameScreen) getScreen()).incrementPoints((int) (-10000));
				hm.get(((GameScreen) getScreen()).getPlayer()).invincible = true;
			}
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

	public void setScreen(int i) {
		super.setScreen(SCREENS[i]);
		index = i;
	}


	public Screen getScreen() {
		return super.getScreen();
	}

	public void moveScreen() {
		index++;
		index = (SCREENS.length + index) % SCREENS.length;
		super.setScreen(SCREENS[index]);
	}

}

