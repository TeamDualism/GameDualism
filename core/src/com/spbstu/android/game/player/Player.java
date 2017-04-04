package com.spbstu.android.game.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.spbstu.android.game.player.Player.Direction.LEFT;
import static com.spbstu.android.game.player.Player.Direction.RIGHT;
import static com.spbstu.android.game.utils.Constants.IMPULSE;
import static com.spbstu.android.game.utils.Constants.MAX_VELOCITY;

public abstract class Player {
    public Body body;
    public int jumpNumber;
    public int bonusCounter = 0;

    public enum State {STANDING, RUNNING, JUMPING};
    public enum Direction {LEFT, RIGHT};

    public Player.Direction direction;

    public void moveRight() {
        direction = RIGHT;

        body.applyLinearImpulse(IMPULSE, 0, body.getPosition().x, body.getPosition().y, false);

        if (Math.abs(body.getLinearVelocity().x) > MAX_VELOCITY) {
            body.setLinearVelocity(MAX_VELOCITY, body.getLinearVelocity().y);
        }
    }
    public void moveLeft() {
        direction = LEFT;

        body.applyLinearImpulse(-IMPULSE, 0, body.getPosition().x, body.getPosition().y, false);

        if (Math.abs(body.getLinearVelocity().x) > MAX_VELOCITY) {
            body.setLinearVelocity(-MAX_VELOCITY, body.getLinearVelocity().y);
        }
    }
    abstract  public void jump(int jumpNumberPlayer);
    public void stop() {
        //body.setLinearVelocity(body.getLinearVelocity().x * STOP, body.getLinearVelocity().y);
        body.setLinearVelocity(0f, body.getLinearVelocity().y);
    }
    public int getTileX() {
        return (int)Math.floor(body.getPosition().x);
    }
    public int getTileY() {
        return (int)Math.floor(body.getPosition().y);
    }

    public void incBonusCounter() {
        bonusCounter++;
    }

    public int getBonusCounter() {
        return bonusCounter;
    }
    abstract public void render(SpriteBatch batch);

    public boolean isGrounded(World world) {
        Fixture sensorFixture = body.getFixtureList().get(1);

        Array<Contact> contactList = world.getContactList();

        for (Contact contact : contactList) {
            if (contact.isTouching() && (contact.getFixtureA() == sensorFixture || contact.getFixtureB() == sensorFixture))
                return true;
        }

        return false;
    }
    abstract public void setState(Player.State newState);
}
