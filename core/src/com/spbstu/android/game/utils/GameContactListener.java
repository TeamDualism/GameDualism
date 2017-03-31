package com.spbstu.android.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.spbstu.android.game.player.Player;
import com.spbstu.android.game.objects.Bonus;

import static com.spbstu.android.game.utils.Constants.BONUS_BIT;
import static com.spbstu.android.game.utils.Constants.PLAYER_BIT;
import static com.spbstu.android.game.utils.Constants.SENSOR_BIT;
import static com.spbstu.android.game.utils.Constants.TILE_BIT;

public class GameContactListener implements ContactListener{
    private GameWorld gameWorld;

    public GameContactListener(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void beginContact(Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();

        int contactType = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (contactType) {
            case PLAYER_BIT | BONUS_BIT:
                if (fixtureB.getFilterData().categoryBits == BONUS_BIT)
                    gameWorld.addToDestroy((Bonus)(fixtureB.getBody().getUserData()));

                if (fixtureA.getFilterData().categoryBits == PLAYER_BIT)
                    ((Player)(fixtureA.getBody().getUserData())).incBonusCounter();

                if (fixtureA.getFilterData().categoryBits == BONUS_BIT)
                    gameWorld.addToDestroy((Bonus)(fixtureA.getBody().getUserData()));

                if (fixtureB.getFilterData().categoryBits == PLAYER_BIT)
                    ((Player)(fixtureB.getBody().getUserData())).incBonusCounter();
                break;

            case SENSOR_BIT | TILE_BIT:
                if (fixtureA.getFilterData().categoryBits == SENSOR_BIT) {
                    Timer timer = new Timer();

                    timer.scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            ((Player)(fixtureA.getBody().getUserData())).jumpNumber = 1;
                        }
                    }, Gdx.graphics.getDeltaTime());
                } else {
                    Timer timer = new Timer();

                    timer.scheduleTask(new Timer.Task() {
                        @Override
                        public void run() {
                            ((Player)(fixtureB.getBody().getUserData())).jumpNumber = 1;
                        }
                    }, Gdx.graphics.getDeltaTime());
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
