package com.spbstu.android.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Object extends Sprite {
    protected Body body;
    protected World world;

    public Object(float x, float y, Texture texture, World world) {
        super(texture);

        this.setPosition(x, y);
        this.world = world;
        createBody(x, y);
    }

    protected abstract void createBody(float x, float y);

    public Body getBody() {
        return body;
    }

    public void render(Batch batch) {
        this.draw(batch);
    }
}
