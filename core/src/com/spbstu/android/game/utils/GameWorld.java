package com.spbstu.android.game.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.spbstu.android.game.GameDualism;
import com.spbstu.android.game.objects.Bonus;
import com.spbstu.android.game.objects.Object;

import static com.spbstu.android.game.utils.Constants.GRAVITY;

public class GameWorld implements Disposable{
    private World world;
    private Array<Object> objectsToDestroy;
    private Array<Bonus> bonuses;
    private GameDualism game;

    public GameWorld(GameDualism game) {
        objectsToDestroy = new Array<Object>();
        bonuses = new Array<Bonus>();

        world = new World(GRAVITY, false);
        this.game = game;
    }

    public void initBonuses(TiledMap map) {
        MapObjects objects = map.getLayers().get("Bonuses").getObjects();

        for (MapObject object: objects) {
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();

            bonuses.add(new Bonus(rectangle.getX(), rectangle.getY(), game.assetManager.get("Textures/coin.png", Texture.class), world));
        }
    }

    public void renderBonuses(Batch batch) {
        batch.begin();

        for (int i = 0; i < bonuses.size; i++) {
            bonuses.get(i).draw(batch);
        }

        batch.end();
    }

    public void destroyObjects() {
        if (objectsToDestroy.size > 0) {
            for (int i = 0; i < objectsToDestroy.size; i++) {
                world.destroyBody(objectsToDestroy.get(i).getBody());
                if (objectsToDestroy.get(i) instanceof Bonus) {
                    bonuses.removeValue((Bonus)(objectsToDestroy.get(i)), true);
                }
                objectsToDestroy.removeIndex(i);
            }
        }
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    public void addToDestroy(Object toDestroy) {
        objectsToDestroy.add(toDestroy);
    }
}
