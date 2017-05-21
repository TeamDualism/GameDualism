package com.spbstu.android.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.spbstu.android.game.component.TimeLine;
import com.spbstu.android.game.utils.TextureUtil;
import static com.spbstu.android.game.player.Player.Direction.LEFT;
import static com.spbstu.android.game.player.Player.Direction.RIGHT;
import static com.spbstu.android.game.utils.Constants.IMPULSE;
import static com.spbstu.android.game.utils.Constants.MAX_VELOCITY;


public class Reggie extends Player {
    private TextureAtlas atlas;
    public Reggie(float x, float y, float radius, World world, TimeLine timeLine) {
        super(x, y, radius, world, timeLine);
        atlas = new TextureAtlas(Gdx.files.internal("Textures/hero.pack"));
        runningAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("running"), Animation.PlayMode.LOOP);
        standingAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("standing"), Animation.PlayMode.LOOP);
        jumpingAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("jumping"), Animation.PlayMode.LOOP);
    }

    /*public void moveRight() {
        direction = RIGHT;

        body.applyLinearImpulse(IMPULSE, 0, body.getPosition().x, body.getPosition().y, false);

        if (Math.abs(body.getLinearVelocity().x) > MAX_VELOCITY) {
            body.setLinearVelocity(MAX_VELOCITY, body.getLinearVelocity().y);
        }
    }
    public void moveLeft() {
        direction = LEFT;

        body.applyLinearImpulse(-IMPULSE, 0, body.getPosition().x, body.getPosition().y, false);

        if (Math.abs(body.getLinearVelocity().x) > MAX_VELOCITY) {
            body.setLinearVelocity(-MAX_VELOCITY, body.getLinearVelocity().y);
        }
    }*/
}
