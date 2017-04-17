package com.spbstu.android.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.spbstu.android.game.utils.Constants.DPLATFORM_BIT;
import static com.spbstu.android.game.utils.Constants.PPM;

public class DisappearingPlatform extends Object{
    public DisappearingPlatform(float x, float y, Texture texture, World world) {
        super(x,  y, texture, world);
    }

    @Override
    protected void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + this.getWidth() / (2 * PPM), y + this.getHeight() / (2 * PPM));
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16.0f / (2 * PPM), 16.0f / (2 * PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = DPLATFORM_BIT;

        body.createFixture(fixtureDef);
        shape.dispose();
    }
}
