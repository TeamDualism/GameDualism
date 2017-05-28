package com.spbstu.android.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

public class GameDualism extends Game {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final java.lang.String TITLE = "Dualism";

    public AssetManager assetManager = new AssetManager();
    private static boolean isMusicOn = true;
    private static boolean isSoundOn = true;

    private ScreenProcesser screenProcesser;

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
        screenProcesser = new ScreenProcesser(this);
        screenProcesser.setThisToScreens();
        screenProcesser.setMenuScreen();
    }

    @Override
    public void dispose() {

        assetManager.dispose();
        screenProcesser.dispose();
    }

    public ScreenProcesser getScreenProcesser(){
        return screenProcesser;
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
