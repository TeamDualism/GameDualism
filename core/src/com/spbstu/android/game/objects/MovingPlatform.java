package com.spbstu.android.game.objects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.spbstu.android.game.utils.Constants.MAX_VELOCITY;
import static com.spbstu.android.game.utils.Constants.MPLATFORM_BIT;
import static com.spbstu.android.game.utils.Constants.PPM;
import static com.spbstu.android.game.utils.Constants.TILE_BIT;

public class MovingPlatform extends Object{
    private float startPoint;
    private float finishPoint;
    private float vx;
    private float vy;

    public MovingPlatform(float x, float y, Texture texture, World world, int dist, float vx, float vy) {
        super(x,  y, texture, world);

        if (vx != 0.0) {
            startPoint = body.getPosition ().x;
            finishPoint = body.getPosition ().x + dist * 16.0f / (PPM);
        } else {
            startPoint = body.getPosition ().y;
            finishPoint = body.getPosition ().y + dist * 16.0f / (PPM);
        }

        body.setLinearVelocity(vx, vy);
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    protected void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x + this.getWidth() / (2 * PPM), y + this.getHeight() / (2 * PPM));
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2 * 16.0f / (2 * PPM), 16.0f / (2 * PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = MPLATFORM_BIT;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void update() {
        if (body.getLinearVelocity ().x != 0.0f) {
            if (body.getPosition ().x > finishPoint) {
                body.setLinearVelocity (-vx, 0);
            } else if (body.getPosition ().x < startPoint) {
                body.setLinearVelocity(vx, 0);
            }
        } else {
            if (body.getPosition ().y > finishPoint) {
                body.setLinearVelocity (0, -vy);
            } else if (body.getPosition ().y < startPoint) {
                body.setLinearVelocity(0, vy);
            }
        }

        this.setPosition(body.getPosition().x * PPM - this.getWidth() / 2, body.getPosition().y * PPM - this.getHeight() / 2);
    }
}
