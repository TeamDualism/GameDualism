package com.spbstu.android.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import static com.spbstu.android.game.utils.Constants.BONUS_BIT;
import static com.spbstu.android.game.utils.Constants.EXIT_BIT;
import static com.spbstu.android.game.utils.Constants.PPM;

/**
 * Created by Администратор on 16.04.2017.
 */

public class Exit extends Object {
    public Exit(float x, float y, Texture texture, World world) {
        super(x, y, texture, world);
    }

    protected void createBody(float x, float y) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + this.getWidth() / (2 * PPM), y + this.getHeight() / (2 * PPM));
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(this.getHeight() / (2 * PPM));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = EXIT_BIT;
        body.createFixture(fixtureDef);
        shape.dispose();
    }
}
