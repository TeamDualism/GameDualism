package com.spbstu.android.game.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.spbstu.android.game.GameDualism;
import java.io.*;
public class DesktopLauncher {
	public static void main (String[] arg)   {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameDualism.WIDTH;
		config.height = GameDualism.HEIGHT;
		config.title = GameDualism.TITLE;
		new LwjglApplication(new GameDualism(), config);
	}
}
