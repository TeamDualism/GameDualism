package com.spbstu.android.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by User on 04.04.2017.
 */

public class Reggie extends Player{
    public TextureAtlas atlas;
    public int jumpNumber;

    public Animation<TextureRegion> runningAnimation;
    public Animation<TextureRegion> standingAnimation;
    public Animation<TextureRegion> jumpingAnimation;

    public Reggie(float x, float y, float radius, World world) {
        super (x, y, radius, world);

        atlas = new TextureAtlas(Gdx.files.internal("Textures/hero.pack"));
        runningAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("running"), Animation.PlayMode.LOOP);
        standingAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("standing"), Animation.PlayMode.LOOP);
        jumpingAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("jumping"), Animation.PlayMode.LOOP);

    }

    public void jump(int jumpNumberPlayer) {
        jumpNumber = jumpNumberPlayer;
        if (jumpNumber <= 1) {
            super.body.setLinearVelocity(super.body.getLinearVelocity().x, 0f);
            super.body.applyLinearImpulse(0, super.body.getMass() * 10f, super.body.getPosition().x, super.body.getPosition().y, false);

            jumpNumber++;
        }
    }
}
