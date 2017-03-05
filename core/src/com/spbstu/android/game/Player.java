package com.spbstu.android.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {

    public final float maxVelocity = 7f;

    public Texture texture;
    public Body body;

    public Player(float x, float y, float radius, World world, AssetManager assetManager) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / 2);
        body.createFixture(shape, 1.0f);
        shape.dispose();

        texture = assetManager.get("Textures/character.png", Texture.class);
    }

    public void moveRight() {
        body.applyLinearImpulse(3f, 0, body.getPosition().x, body.getPosition().y, false);

        if (Math.abs(body.getLinearVelocity().x) > maxVelocity) {
            body.setLinearVelocity(Math.signum(body.getLinearVelocity().x) * maxVelocity, body.getLinearVelocity().y);
        }
    }

    public void moveLeft() {
        body.applyLinearImpulse(-3f, 0, body.getPosition().x, body.getPosition().y, false);

        if (Math.abs(body.getLinearVelocity().x) > maxVelocity) {
            body.setLinearVelocity(Math.signum(body.getLinearVelocity().x) * maxVelocity, body.getLinearVelocity().y);
        }
    }

    public void jump() {
        body.applyLinearImpulse(0, body.getMass() * 13.5f, body.getPosition().x, body.getPosition().y, false);
    }

    public void stop() {
        body.setLinearVelocity(body.getLinearVelocity().x * 0.9f, body.getLinearVelocity().y);
    }
}
