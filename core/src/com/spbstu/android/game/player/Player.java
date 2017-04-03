package com.spbstu.android.game.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Player {
    public Body body;
    public int jumpNumber;

    abstract public void moveRight();
    abstract public void moveLeft();
    abstract  public void jump(int jumpNumberPlayer);
    abstract  public void stop();
    abstract public int getTileX();
    abstract  public int getTileY();
    abstract public void incBonusCounter();
    abstract public void render(SpriteBatch batch);
    abstract public boolean isGrounded(World world);
    abstract public void setState(Ronnie.State newState);
    abstract public int getBonusCounter();
}
