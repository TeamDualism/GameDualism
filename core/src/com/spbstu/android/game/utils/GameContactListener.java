package com.spbstu.android.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.spbstu.android.game.objects.DisappearingPlatform;
import com.spbstu.android.game.objects.Object;
import com.spbstu.android.game.player.Player;
import com.spbstu.android.game.objects.Bonus;
import com.spbstu.android.game.screens.MenuScreen;

import static com.spbstu.android.game.utils.Constants.BONUS_BIT;
import static com.spbstu.android.game.utils.Constants.DPLATFORM_BIT;
import static com.spbstu.android.game.utils.Constants.EXIT_BIT;
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

            case SENSOR_BIT | DPLATFORM_BIT:
                Timer timer = new Timer();

                if (fixtureB.getFilterData().categoryBits == DPLATFORM_BIT) {
                    timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            gameWorld.addToDestroy((DisappearingPlatform) (fixtureB.getBody().getUserData()));
                        }
                    }, 0.5f);
                } else {
                    timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            gameWorld.addToDestroy((DisappearingPlatform) (fixtureA.getBody().getUserData()));
                        }
                    }, 0.5f);
                }

                if (fixtureA.getFilterData().categoryBits == SENSOR_BIT) {
                    ((Player)(fixtureA.getBody().getUserData())).jumpNumber = 1;
                } else {
                    ((Player)(fixtureB.getBody().getUserData())).jumpNumber = 1;
                }

                break;

            case SENSOR_BIT | TILE_BIT:
                if (fixtureA.getFilterData().categoryBits == SENSOR_BIT) {
                    ((Player)(fixtureA.getBody().getUserData())).jumpNumber = 1;
                } else {
                    ((Player)(fixtureB.getBody().getUserData())).jumpNumber = 1;
                }
                break;

            case PLAYER_BIT | EXIT_BIT:
                gameWorld.onExit();
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
