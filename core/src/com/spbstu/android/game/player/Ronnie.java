package com.spbstu.android.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.spbstu.android.game.component.TimeLine;


public class Ronnie extends Player {
    private TextureAtlas atlas;
    public Ronnie(float x, float y, float radius, World world, TimeLine timeLine) {
        super(x, y, radius, world, timeLine);
        atlas = new TextureAtlas(Gdx.files.internal("Textures/player.pack"));
        runningAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("running"), Animation.PlayMode.LOOP);
        standingAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("standing"), Animation.PlayMode.LOOP);
        jumpingAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("jumping"), Animation.PlayMode.LOOP);

    }
}
