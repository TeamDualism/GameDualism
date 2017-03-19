package com.spbstu.android.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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

        //Main body
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(radius / 2, radius * 0.95f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0f;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();

        //Sensor body
        PolygonShape shape1 = new PolygonShape();
        shape1.setAsBox(radius / 2.05f, radius / 10, new Vector2(0, -radius * 0.9f), 0f);
        fixtureDef.shape = shape1;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef);
        shape1.dispose();

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

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture,
                body.getPosition().x * MapParser.PPM - texture.getWidth() / 2,
                body.getPosition().y * MapParser.PPM - texture.getHeight() / 2);
        batch.end();
    }
}
