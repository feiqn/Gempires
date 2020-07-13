package com.feiqn.gempires.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.feiqn.gempires.GempiresGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Gempires";
		config.height = 800;
		config.width = 450;
		new LwjglApplication(new GempiresGame(), config);
	}
}
