package com.spbstu.android.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import static com.spbstu.android.game.utils.Constants.PPM;
import static com.spbstu.android.game.utils.Constants.TILE_BIT;

public class MapParser {
    public static void parseMapObjects(MapObjects objects, World world) {
        Shape shape;

        for (MapObject object: objects) {
            if (object instanceof PolylineMapObject)
            {
                shape = createPolyLineObject((PolylineMapObject)object);

                Body body;
                BodyDef bodyDef = new BodyDef();

                bodyDef.type = BodyDef.BodyType.StaticBody;
                body = world.createBody(bodyDef);

                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.density = 1.0f;
                fixtureDef.shape = shape;
                fixtureDef.filter.categoryBits = TILE_BIT;

                body.createFixture(fixtureDef);

                shape.dispose();
            }
        }
    }

    private static Shape createPolyLineObject(PolylineMapObject object) {
        float verticies[] = object.getPolyline().getTransformedVertices();
        for (int i = 0; i < verticies.length; i++) {
            verticies[i] /= PPM;
        }
        ChainShape shape = new ChainShape();
        shape.createChain(verticies);

        return shape;
    }
}
