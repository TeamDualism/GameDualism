package com.spbstu.android.game;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

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
                body.createFixture(shape, 1.0f);

                shape.dispose();
            }
        }
    }

    private static Shape createPolyLineObject(PolylineMapObject object) {
        float verticies[] = object.getPolyline().getTransformedVertices();
        for (int i = 0; i < verticies.length; i++) {
            verticies[i] /= 10;
        }
        ChainShape shape = new ChainShape();
        shape.createChain(verticies);

        return shape;
    }
}
