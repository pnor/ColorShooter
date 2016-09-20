package com.colorshooter.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.colorshooter.game.ColorShooter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Color Wars";
		config.addIcon("ColorShooterMacIcon.png", Files.FileType.Internal);
		config.addIcon("ColorShooter32Icon.png", Files.FileType.Internal);
		config.addIcon("ColorShooter16Icon.png", Files.FileType.Internal);
		config.width = 1400;
		config.height = 900;
		config.fullscreen = false;
		config.resizable = false;

		new LwjglApplication(new ColorShooter(), config);
	}
}
