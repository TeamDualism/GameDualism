package com.spbstu.android.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {

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
}
