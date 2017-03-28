package com.spbstu.android.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.spbstu.android.game.MapParser;

public class Bonus extends Object{
    public Bonus(float x, float y, Texture texture, World world) {
        super(x, y, texture, world);
    }

    protected void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + this.getWidth() / (2 * MapParser.PPM), y + this.getHeight() / (2 * MapParser.PPM));
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(this.getHeight() / (2 * MapParser.PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;

        body.createFixture(fixtureDef);
        shape.dispose();
    }
}
