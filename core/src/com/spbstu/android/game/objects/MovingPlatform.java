package com.spbstu.android.game.objects;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.spbstu.android.game.utils.Constants.MAX_VELOCITY;
import static com.spbstu.android.game.utils.Constants.PPM;
import static com.spbstu.android.game.utils.Constants.TILE_BIT;

public class MovingPlatform extends Object{
    private float startPoint;
    private float finishPoint;

    public MovingPlatform(float x, float y, Texture texture, World world, int dist) {
        super(x,  y, texture, world);

        startPoint = body.getPosition().x;
        finishPoint = body.getPosition().x + dist * 16.0f / (2 * PPM);
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
        fixtureDef.filter.categoryBits = TILE_BIT;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setLinearVelocity(2f, 0f);
    }

    public void update() {
        if (body.getPosition().x >= finishPoint) {
            body.setLinearVelocity(-2f, 0f);
        } else if (body.getPosition().x <= startPoint) {
            body.setLinearVelocity(2f, 0f);
        }

        this.setPosition(body.getPosition().x * PPM - this.getWidth() / 2, body.getPosition().y * PPM - this.getHeight() / 2);
    }
}
