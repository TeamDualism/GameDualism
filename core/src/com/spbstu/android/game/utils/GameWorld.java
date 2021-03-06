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
import com.spbstu.android.game.ScreenProcesser;
import com.spbstu.android.game.objects.Bonus;
import com.spbstu.android.game.objects.DisappearingPlatform;
import com.spbstu.android.game.objects.Exit;
import com.spbstu.android.game.objects.MovingPlatform;
import com.spbstu.android.game.objects.Object;

import java.util.Iterator;

import static com.spbstu.android.game.utils.Constants.GRAVITY;
import static com.spbstu.android.game.utils.Constants.PPM;

public class GameWorld implements Disposable{
    private World world;
    private Array<Object> objectsToDestroy;
    private Array<Bonus> bonuses;
    private Array<DisappearingPlatform> disappearingPlatformss;
    private Array<MovingPlatform> movingPlatforms;
    private GameDualism game;
    private Exit exit;
    private ScreenProcesser screenProcesser;

    public GameWorld(GameDualism game) {
        objectsToDestroy = new Array<Object>();
        bonuses = new Array<Bonus>();
        disappearingPlatformss = new Array<DisappearingPlatform>();
        movingPlatforms = new Array<MovingPlatform>();

        screenProcesser = game.getScreenProcesser();
        world = new World(GRAVITY, false);
        world.setContactListener(new GameContactListener(this));
        this.game = game;
    }

    public void initBonuses(TiledMap map) {
        MapObjects objects = map.getLayers().get("Bonuses").getObjects();

        for (MapObject object: objects) {
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();

            bonuses.add(new Bonus(rectangle.getX(), rectangle.getY(), game.assetManager.get("Textures/coin.png", Texture.class), world));
        }
    }

    public void initPlatforms(TiledMap map) {
        MapObjects objects = map.getLayers().get("Platforms").getObjects();

        for (MapObject object: objects) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            if (object.getProperties().get("type").equals("d")) {
                disappearingPlatformss.add(new DisappearingPlatform(rectangle.getX(), rectangle.getY(), game.assetManager.get("Maps/Tiles/dplatform.png", Texture.class), world));
            } else {
                movingPlatforms.add(new MovingPlatform(rectangle.getX(), rectangle.getY(), game.assetManager.get("Maps/Tiles/mplatform.png", Texture.class), world,
                        object.getProperties().get("dist", Integer.class),
                        object.getProperties().get("vx", Float.class),
                        object.getProperties().get("vy", Float.class)));
            }
        }
    }

    public void initExit(int x, int y,Texture texture) {
        exit = new Exit(x*PPM, y*PPM, texture, world);
    }

    public void onExit()
    {
        destroyObjects();
        screenProcesser.disposeCurrentLevelScreen();
        screenProcesser.setMenuScreen();
    }

    public void renderBonuses(Batch batch) {
        batch.begin();

        for (int i = 0; i < bonuses.size; i++) {
            bonuses.get(i).draw(batch);
        }

        batch.end();
    }

    public void renderPlatforms(Batch batch) {
        batch.begin();

        for (int i = 0; i < disappearingPlatformss.size; i++) {
            disappearingPlatformss.get(i).draw(batch);
        }

        for (int i = 0; i < movingPlatforms.size; i++) {
            movingPlatforms.get(i).update();
            movingPlatforms.get(i).draw(batch);
        }

        batch.end();
    }

    public void renderExit(Batch batch) {
        batch.begin();
        exit.draw(batch);
        batch.end();
    }

    public void destroyObjects() {
        if (objectsToDestroy.size > 0) {
            for (int i = 0; i < objectsToDestroy.size; i++) {
                if (objectsToDestroy.get(i) instanceof Bonus) {
                    bonuses.removeValue((Bonus)(objectsToDestroy.get(i)), true);
                }
                if (objectsToDestroy.get(i) instanceof DisappearingPlatform) {
                    disappearingPlatformss.removeValue((DisappearingPlatform)(objectsToDestroy.get(i)), true);
                }
                world.destroyBody(objectsToDestroy.get(i).getBody());
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
        if (toDestroy != null) {
            objectsToDestroy.add(toDestroy);
        }
    }
}
