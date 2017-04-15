package com.spbstu.android.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.BooleanArray;
import com.spbstu.android.game.screens.MenuScreen;

public class GameDualism extends Game {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final java.lang.String TITLE = "Dualism";

    public AssetManager assetManager = new AssetManager();
    private Boolean isMusicOn = true;
    private Boolean isSoundOn = true;

    @Override
    public void create() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }

    public void setMusicOn(){
        isMusicOn = true;
    }

    public void setMusicOff(){
        isMusicOn = false;
    }

    public Boolean getIsMusicOn(){
        return isMusicOn;
    }

    public void setSoundOn(){
        isSoundOn = true;
    }

    public void setSoundOff(){
        isSoundOn = false;
    }

    public Boolean getIsSoundOn(){
        return isSoundOn;
    }

    public static void playSound(Sound sound, GameDualism game){
        if(game.getIsSoundOn())
            sound.play();
    }

}
