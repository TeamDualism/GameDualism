package com.spbstu.android.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.spbstu.android.game.ui.MenuScreen;

public class GameDualism extends Game {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final java.lang.String TITLE = "Dualism";

    @Override
    public void create() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        setScreen(new MenuScreen(this));
    }
}
