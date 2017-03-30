package com.spbstu.android.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import static com.spbstu.android.game.utils.Constants.PPM;

public abstract class Object extends Sprite {
    protected Body body;
    protected World world;

    public Object(float x, float y, Texture texture, World world) {
        super(texture);

        this.setPosition(x, y);
        this.world = world;
        createBody(x / PPM, y / PPM);
    }

    protected abstract void createBody(float x, float y);

    public Body getBody() {
        return body;
    }

    public void render(Batch batch) {
        this.draw(batch);
    }
}
