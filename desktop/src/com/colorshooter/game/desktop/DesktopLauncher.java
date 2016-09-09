package com.colorshooter.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.colorshooter.game.ColorShooter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "ColorShooter";
		//config.width = 1920;
		//config.height = 1080;
		config.width = 1400;
		config.height = 900;
		config.fullscreen = false;
		config.resizable = false;

		new LwjglApplication(new ColorShooter(), config);
	}
}
