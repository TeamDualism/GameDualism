package com.spbstu.android.game.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Constants {
    //LibGDX
    public static int HEIGHT = Gdx.graphics.getHeight();
    public static int WIDTH = Gdx.graphics.getWidth();

    //Box2D
    public static final Vector2 GRAVITY = new Vector2(0f, -20f);
    public static final float PPM = 16f;

    //Player
    public static final float MAX_VELOCITY = 4.5f;
    public static final float IMPULSE = 3f;
    public static final float STOP = 0.6f;
}
