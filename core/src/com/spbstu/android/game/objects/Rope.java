package com.spbstu.android.game.objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Array;
import static com.spbstu.android.game.utils.Constants.PPM;

/**
 * Created by Администратор on 24.03.2017.
 */

public class Rope {
    private Array<Joint> joint = new Array<Joint>();
    public enum ropeState{
        isRest, inFlight, isRoped
    }

    private ropeState state = ropeState.isRest;
    private Sprite spriteRope;
    public float yRopedBlock, xRopedBlock;
    private float H, L, alpha, alphaRad;
    public Rope() {
        spriteRope = new Sprite(new Texture("ropePart.png"));

    }

    public void buildJoint(World world, float x, float y, Body playerBody, boolean blocksMap[][]) {
        float possibleX ,possibleY;
        if ((y > playerBody.getPosition().y * PPM + 1.5 * PPM) && (Math.abs(x - playerBody.getPosition().x * PPM) < 7 * PPM)) {
            L = norm(playerBody.getPosition().x * PPM, x, playerBody.getPosition().y * PPM, y);
            H = y - playerBody.getPosition().y * PPM;
            alpha = (float) (Math.asin(H / L));
            possibleX = playerBody.getPosition().x*PPM;// + PPM *(float) Math.cos(alpha);
            possibleY = playerBody.getPosition().y*PPM ;// + PPM *(float) Math.sin(alpha);;
            if (playerBody.getPosition().x * PPM > x)
                alpha = (float) (Math.PI - alpha);
            for (int i = 0; i < 8*L / PPM + 16; i++) {
                possibleX += PPM/8 * Math.cos(alpha);
                possibleY += PPM/8 * Math.sin(alpha);
                if(possibleY > (blocksMap.length-1) *PPM)
                    possibleY =  (blocksMap.length-1) *PPM;
                if(possibleX > (blocksMap[0].length-1) *PPM)
                    possibleX =  (blocksMap[0].length-1) *PPM;
                if(possibleX < 0)
                    possibleX =  0;
                if (blocksMap[(int) Math.floor((possibleY) / PPM)][(int) (Math.floor((possibleX) / PPM)) ]) {
                    xRopedBlock = possibleX;
                    yRopedBlock = possibleY;
                    if(state == ropeState.isRoped)
                        destroyJoint(world);
                    state = ropeState.isRoped;
                    buildRopeJoints(world,createBox(world,(xRopedBlock /*+ 2*PPM*/) / PPM, yRopedBlock / PPM, 0.2f/PPM, 0.2f/ PPM, false),  playerBody);
                    break;
                }
            }
        }
    }

    private Body createBox(World world, float x, float y, float height, float width, boolean isDynamic){
        Body box ;
        BodyDef bodyDef = new BodyDef();
        if(isDynamic)
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        else
            bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;

        box = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0f;
        fixtureDef.shape = shape;
        box.createFixture(fixtureDef);
        shape.dispose();
        return box;
    }
    private void buildRopeJoints(World world,Body block, Body player){
        getParams(player);
        int length = (int)Math.floor(3*L/PPM);
        Array<Body> bodies = new Array<Body>();
        bodies.add(block);
        for(int i = 1; i <= length; i++){
            float xx = xRopedBlock/PPM -(float)(i*L * Math.cos(alphaRad)/(length))/PPM;
            float yy = yRopedBlock/PPM - (float)(i*L * Math.sin(alphaRad)/(length))/PPM;
            bodies.add(createBox(world, xx , yy, 1.4f/PPM, 1f/PPM, true));

            RopeJointDef rDef = new RopeJointDef();
            rDef.bodyA = bodies.get(i-1);
            rDef.bodyB= bodies.get(i);

            rDef.collideConnected = true;
            rDef.maxLength =  0.9f*L/ (length +2)/ PPM;
            rDef.localAnchorA.set(0, 0);
            rDef.localAnchorB.set(0, 0);
            joint.add(world.createJoint(rDef));
        }
        RopeJointDef rDef = new RopeJointDef();
        rDef.bodyA = bodies.get(length);
        rDef.bodyB = player;
        //rDef.collideConnected = true;
        rDef.maxLength =    2f*L/ (length +2)/ PPM;
        rDef.localAnchorA.set(0, 0);
        rDef.localAnchorB.set(0, 0);
        joint.add(world.createJoint(rDef));

    }
    public void destroyJoint(World world) {

        for(int i = 0; i< joint.size; i++)
            world.destroyJoint(joint.get(i));
        for(int i = 0; i< joint.size; i++)
            world.destroyBody(joint.get(i).getBodyA());
        joint.clear();
    }
    private void getParams(Body body){
        H = yRopedBlock - body.getPosition().y * PPM;
        L = (float) norm(body.getPosition().x * PPM, xRopedBlock, body.getPosition().y * PPM, yRopedBlock);
        alphaRad = (float) (Math.asin(H / L));
        alpha = (float)(alphaRad/Math.PI * 180);


        if (body.getPosition().x * PPM > xRopedBlock) {
            alpha = -1 * alpha + 90;
            alphaRad = (float)(Math.PI -alphaRad);
        }
        else {
            alpha -= 90;
        }
    }

    public void render(SpriteBatch batch, Body body) { //рисую веревку, рисую, где хочу, законом не запрещено
        float alphaLinks, lLinks = 0;
        if (state == ropeState.isRoped) {
            batch.begin();
            getParams(body);
            for(int i = 0; i < joint.size; i++) {
                lLinks = norm(joint.get(i).getBodyA().getPosition().x * PPM, joint.get(i).getBodyB().getPosition().x * PPM,
                        joint.get(i).getBodyB().getPosition().y * PPM, joint.get(i).getBodyA().getPosition().y * PPM);
                alphaLinks = (float) (Math.asin( (joint.get(i).getBodyA().getPosition().y * PPM -  joint.get(i).getBodyB().getPosition().y* PPM) / lLinks)/Math.PI * 180);
                if (joint.get(i).getBodyB().getPosition().x> joint.get(i).getBodyA().getPosition().x)
                    alphaLinks = -1 * alphaLinks + 90;
                else
                    alphaLinks -= 90;
                spriteRope.setOrigin(0, 0);
                spriteRope.setSize(1 , lLinks+1);
                spriteRope.setRotation(alphaLinks);
                spriteRope.setPosition(joint.get(i).getBodyB().getPosition().x * PPM, joint.get(i).getBodyB().getPosition().y * PPM);//
                spriteRope.draw(batch);

            }

            batch.end();
        }
    }
    public void setRopeState(ropeState state){
        this.state = state;
    }
    public ropeState getRopeState(){
        return state;
    }

    public static float norm(double x1, double x2, double y1, double y2) {
        return (float)Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }


}
