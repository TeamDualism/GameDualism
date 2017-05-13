package com.spbstu.android.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
import com.spbstu.android.game.GameDualism;
import com.spbstu.android.game.component.TimeLine;

import static com.spbstu.android.game.player.Player.Direction.LEFT;
import static com.spbstu.android.game.player.Player.Direction.RIGHT;
import static com.spbstu.android.game.player.Player.State.STANDING;
import static com.spbstu.android.game.utils.Constants.IMPULSE;
import static com.spbstu.android.game.utils.Constants.MAX_VELOCITY;
import static com.spbstu.android.game.utils.Constants.PLAYER_BIT;
import static com.spbstu.android.game.utils.Constants.PPM;
import static com.spbstu.android.game.utils.Constants.SENSOR_BIT;

public abstract class Player {
    public TextureAtlas atlas;
    public Body body;
    public int jumpNumber;
    public int bonusCounter = 0;

    float stateTime;
    public Animation<TextureRegion> runningAnimation;
    public Animation<TextureRegion> standingAnimation;
    public Animation<TextureRegion> jumpingAnimation;
    public Player.State state;

    public enum State {STANDING, RUNNING, JUMPING}

    public enum Direction {LEFT, RIGHT}

    public Player.Direction direction;

    private final TimeLine timeLine;

    private final Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("Audio/Jump/jump_08.wav"));

    public Player(float x, float y, float radius, World world, TimeLine timeLine) {
        BodyDef bodyDef = new BodyDef();

        this.timeLine = timeLine;
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

        jumpNumber = 1;

        stateTime = 0f;
        state = STANDING;
    }

    public void setAtlas(TextureAtlas atlas, Animation<TextureRegion> running, Animation<TextureRegion> standing, Animation<TextureRegion> jumping) {
        this.atlas = atlas;
        runningAnimation = running;
        jumpingAnimation = jumping;
        standingAnimation = standing;
    }

    public void changeBody(Player curCharacter, Player prevCharacter, Player nextCharacter) {
        nextCharacter.body.setActive(true);
        nextCharacter.body.setTransform(prevCharacter.body.getPosition().x, prevCharacter.body.getPosition().y, prevCharacter.body.getAngle());
        prevCharacter.body.setActive(false);
        curCharacter.body = nextCharacter.body;
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

    public void moveRightOnRope() {
        direction = RIGHT;
        body.applyLinearImpulse(IMPULSE/2, 0, body.getPosition().x, body.getPosition().y, false);

    }
    public void moveLeftOnRope() {
        direction = LEFT;
        body.applyLinearImpulse(-IMPULSE/2, 0, body.getPosition().x, body.getPosition().y, false);

    }

    public void jump(int jumpNumber) {
        if (this.jumpNumber <= jumpNumber) {
            body.setLinearVelocity(body.getLinearVelocity().x, 0f);
            body.applyLinearImpulse(0, body.getMass() * 10f, body.getPosition().x, body.getPosition().y, false);
            GameDualism.playSound(jumpSound);
            this.jumpNumber++;
        }
    }

    public void stop() {
        //body.setLinearVelocity(body.getLinearVelocity().x * STOP, body.getLinearVelocity().y);
        body.setLinearVelocity(0f, body.getLinearVelocity().y);
    }

    public int getTileX() {
        return (int) Math.floor(body.getPosition().x);
    }

    public int getTileY() {
        return (int) Math.floor(body.getPosition().y);
    }

    public void incBonusCounter() {
        bonusCounter++;
    }

    public int getBonusCounter() {
        return bonusCounter;
    }

    public void setState(Player.State newState) {
        state = newState;
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

    public TimeLine getTimeline() {
        return timeLine;
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
}
