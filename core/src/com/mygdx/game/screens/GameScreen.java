package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;
import com.mygdx.game.PhysX;

public class GameScreen implements Screen {
    private static final float STEP = 12;
    Main game;
    private SpriteBatch batch;
    private Anim animation;
    boolean direction;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private final int[] bg;
    private final int[] l1;
    private PhysX physX;
    private Body body;
    private final Rectangle heroRect;
    float x;
    float y;

    private Rectangle mapSize;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        animation = new Anim("counter", Animation.PlayMode.LOOP);
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        bg = new int[1];
        bg[0] = map.getLayers().getIndex("map");
        l1 = new int[1];
        l1[0] = map.getLayers().getIndex("spirit");
        physX = new PhysX();

        RectangleMapObject rmp = (RectangleMapObject) map.getLayers().get("Objects").getObjects().get("hero");
        heroRect = rmp.getRectangle();
        body = physX.addObject(rmp, 0);

        rmp = (RectangleMapObject) map.getLayers().get("Objects").getObjects().get("border");
        mapSize = rmp.getRectangle();
        Array<RectangleMapObject> objects = map.getLayers().get("static_objects").getObjects().getByType(RectangleMapObject.class);
        objects.addAll(map.getLayers().get("kinematic_objects").getObjects().getByType(RectangleMapObject.class));
        objects.addAll(map.getLayers().get("dynamic_objects").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
            physX.addObject(objects.get(i), 1);
        }
        x = mapSize.x;
        y = mapSize.y;
        direction = true;
        camera.zoom = 0.25f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            body.applyForceToCenter(new Vector2(-10000000, 0), true);
            animation = new Anim("start", Animation.PlayMode.LOOP);}
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            body.applyForceToCenter(new Vector2(10000000, 0), true);
            animation = new Anim("start", Animation.PlayMode.LOOP);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            body.applyForceToCenter(new Vector2(0, 10000000), true);
            animation = new Anim("jump", Animation.PlayMode.LOOP);}
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            body.applyForceToCenter(new Vector2(0, -10000000), true);
            animation = new Anim("jump", Animation.PlayMode.LOOP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            animation = new Anim("punch", Animation.PlayMode.LOOP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) camera.zoom += 0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.1f;

        if (body.getLinearVelocity().x < 0 && animation.getFrame().isFlipX()) {
            animation.getFrame().flip(true, false);}
        if (body.getLinearVelocity().x > 0 && !animation.getFrame().isFlipX()) {
            animation.getFrame().flip(true, false);}


        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        camera.update();
        ScreenUtils.clear(Color.YELLOW);
        animation.setTime(Gdx.graphics.getDeltaTime());


//        if (x >= Gdx.graphics.getWidth() - animation.getFrame().getRegionWidth()) {
//            animation.getFrame().flip(true, false);
//            direction = false;
//        }
//
//        if (x <= 0) {
//            animation.getFrame().flip(true, false);
//            direction = true;
//        }
//
//        if (direction) {
//            x += animation.getFrame().getRegionWidth()/100f;
//        } else {
//            x -= animation.getFrame().getRegionWidth()/100f;
//        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                dispose();
                game.setScreen(new MenuScreen(game));
        }

        mapRenderer.setView(camera);
        mapRenderer.render(bg);

        Vector2 path = body.getLinearVelocity(); //направление героя
        System.out.println(path);
        batch.setProjectionMatrix(camera.combined);
        heroRect.x = body.getPosition().x - heroRect.width/2;
        heroRect.y = body.getPosition().y - heroRect.height/2;
        batch.begin();
        batch.draw(animation.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        batch.end();




//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.BLACK);
//        for (int i = 0; i < objects.size; i++) {
//            Rectangle mapSize = objects.get(i).getRectangle();
//            shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
//        }
//        shapeRenderer.rect(mapSize.x, mapSize.y + mapSize.height, backRect.width, backRect.height);
//        shapeRenderer.end();



        mapRenderer.render(l1);
        physX.step();
        physX.debugDraw(camera);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        animation.dispose();
    }
}
