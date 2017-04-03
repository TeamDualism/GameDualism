package com.spbstu.android.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


import static com.spbstu.android.game.player.Ronnie.State.STANDING;
import static com.spbstu.android.game.player.Ronnie.Direction.LEFT;
import static com.spbstu.android.game.player.Ronnie.Direction.RIGHT;
import static com.spbstu.android.game.utils.Constants.IMPULSE;
import static com.spbstu.android.game.utils.Constants.MAX_VELOCITY;
import static com.spbstu.android.game.utils.Constants.PLAYER_BIT;
import static com.spbstu.android.game.utils.Constants.PPM;
import static com.spbstu.android.game.utils.Constants.SENSOR_BIT;

/**
 * Created by User on 03.04.2017.
 */

public class Ronnie extends Player{
    private TextureAtlas atlas;
    public Body body;
    public int jumpNumber;
    private int bonusCounter;

    float stateTime;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> standingAnimation;
    private Animation<TextureRegion> jumpingAnimation;

    public enum State {STANDING, RUNNING, JUMPING};
    public enum Direction {LEFT, RIGHT};
    private Ronnie.State state;
    private Ronnie.Direction direction;

    public Ronnie(float x, float y, float radius, World world) {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        body.setUserData(this);

        //Main body
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(radius / 2, radius * 0.95f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = PLAYER_BIT;
        body.createFixture(fixtureDef);
        shape.dispose();

        //Sensor body
        PolygonShape shape1 = new PolygonShape();
        shape1.setAsBox(radius / 2.05f, radius / 10, new Vector2(0, -radius * 0.9f), 0f);
        fixtureDef.shape = shape1;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = SENSOR_BIT;
        body.createFixture(fixtureDef);
        shape1.dispose();

        atlas = new TextureAtlas(Gdx.files.internal("Textures/player.pack"));
        runningAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("running"), Animation.PlayMode.LOOP);
        standingAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("standing"), Animation.PlayMode.LOOP);
        jumpingAnimation = new Animation<TextureRegion>(0.2f, atlas.findRegions("jumping"), Animation.PlayMode.LOOP);

        jumpNumber = 1;
        bonusCounter = 0;

        stateTime = 0f;
        state = STANDING;
        direction = RIGHT;
    }

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

    public void jump(int jumpNumberPlayer) {
        jumpNumber = jumpNumberPlayer;
        if (jumpNumber <= 2) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0f);
            body.applyLinearImpulse(0, body.getMass() * 10f, body.getPosition().x, body.getPosition().y, false);

            jumpNumber++;
        }
    }

    public void stop() {
        //body.setLinearVelocity(body.getLinearVelocity().x * STOP, body.getLinearVelocity().y);
        body.setLinearVelocity(0f, body.getLinearVelocity().y);
    }

     public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame;

        switch (state) {
            case RUNNING:
                currentFrame = runningAnimation.getKeyFrame(stateTime, true);
                break;
            case STANDING:
                currentFrame = standingAnimation.getKeyFrame(stateTime, true);
                break;
            case JUMPING:
                currentFrame = jumpingAnimation.getKeyFrame(stateTime, true);
                break;
            default:
                currentFrame = standingAnimation.getKeyFrame(stateTime, true);
                break;
        }
        batch.begin();

        batch.draw(currentFrame,
                body.getPosition().x * PPM - currentFrame.getRegionWidth() / 2 + (direction == LEFT ? currentFrame.getRegionWidth() : 0),
                body.getPosition().y * PPM - currentFrame.getRegionHeight() / 2,
                currentFrame.getRegionWidth() * (direction == LEFT ? -1 : 1),
                currentFrame.getRegionHeight());
        batch.end();
    }

    public boolean isGrounded(World world) {
        Fixture sensorFixture = body.getFixtureList().get(1);

        Array<Contact> contactList = world.getContactList();

        for (Contact contact : contactList) {
            if (contact.isTouching() && (contact.getFixtureA() == sensorFixture || contact.getFixtureB() == sensorFixture))
                return true;
        }

        return false;
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

    public void setState(Ronnie.State newState) {
        state = newState;
    }
}
