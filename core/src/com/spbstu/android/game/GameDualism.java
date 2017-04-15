package com.spbstu.android.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.spbstu.android.game.screens.Level1Screen;

public class GameDualism extends Game {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;
    public static final java.lang.String TITLE = "Dualism";

    public AssetManager assetManager = new AssetManager();
    private static boolean isMusicOn = true;
    private static boolean isSoundOn = true;

    private static class Holder {
        private static GameDualism INSTANCE = new GameDualism();
    }

    private GameDualism() {
    }

    public static GameDualism getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void create() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        setScreen(new Level1Screen(this));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public void setMusicOn() {
        isMusicOn = true;
    }

    public void setMusicOff() {
        isMusicOn = false;
    }

    public boolean getIsMusicOn() {
        return isMusicOn;
    }

    public void setSoundOn() {
        isSoundOn = true;
    }

    public void setSoundOff() {
        isSoundOn = false;
    }

    public boolean getIsSoundOn() {
        return isSoundOn;
    }

    public static void playSound(Sound sound) {
        if (isSoundOn)
            sound.play();
    }

}
