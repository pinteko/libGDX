package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class PhysX {

    private final World world;
    private final Box2DDebugRenderer debugRenderer;
    private final float PPM = 1;

    public PhysX() {
        world = new World(new Vector2(0, - 9.81f), true );
        debugRenderer = new Box2DDebugRenderer();
        world.setContactListener(new MyContList());
    }

    public void deleteBody(Body body) {
        world.destroyBody(body);
    }

    public Body addObject(RectangleMapObject object, float density) {
        Rectangle rectangle = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();
        CircleShape circleShape;
        ChainShape chainShape;

        switch (type) {
            case ("StaticBody"):
                def.type = BodyDef.BodyType.StaticBody;
                break;
            case ("DynamicBody"):
                def.type = BodyDef.BodyType.DynamicBody;
                break;
            case ("KinematicBody"):
                def.type = BodyDef.BodyType.KinematicBody;
                break;
        }

        def.gravityScale = (float) object.getProperties().get("gravityScale");

//        def.type = BodyDef.BodyType.StaticBody;  //тип тела
        def.position.set((rectangle.x + rectangle.width/2) / PPM,
                (rectangle.y + rectangle.height/2) / PPM);  //позиция тела(текстуры)
//        def.gravityScale = 1; //гравитация

        polygonShape.setAsBox(rectangle.width/2/PPM, rectangle.height/2/PPM);

        fDef.shape = polygonShape; //форма
        fDef.friction = 0; //лед
        fDef.density = density; //плотность
        fDef.restitution = (float) object.getProperties().get("restitution"); //упругость

        Body body = world.createBody(def);
        String name = object.getName();
        body.createFixture(fDef).setUserData(name);
        if (name != null && name.equals("hero")) {
            polygonShape.setAsBox(rectangle.width/12, rectangle.height/12, new Vector2(0, - rectangle.width/2), 0);
            body.createFixture(fDef).setUserData("sensor");
            body.getFixtureList().get(body.getFixtureList().size - 1).setSensor(true);
        }
        if (name != null && name.equals("enemy")) {
            polygonShape.setAsBox(rectangle.width/12, rectangle.height/12, new Vector2(0, - rectangle.width/2), 0);
            body.createFixture(fDef).setUserData("footEnemy");
            body.getFixtureList().get(body.getFixtureList().size - 1).setSensor(true);
        }


        polygonShape.dispose();
        return body;
    }

    public void setGravity(Vector2 gravity) {
        world.setGravity(gravity);
    }

    public void step() {
        world.step(1/60.0f, 3, 3);
    }

    public void debugDraw(OrthographicCamera camera) {
        debugRenderer.render(world, camera.combined);
    }

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }
}
