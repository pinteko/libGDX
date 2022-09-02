package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;
import com.mygdx.game.PhysX;

import java.util.ArrayList;

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
    private Texture rock;
    private final int[] bg;
    private final int[] l1;
    public static PhysX physX;
    private final Body bodyHero;
    private final Body bodyBall;
    private final Rectangle heroRect;
    private final Rectangle ballRect;
    public static ArrayList<Body> bodies;
    public static  Music musicHero;
    public static  Music musicBall;
    public static  Music musicGameOver;
    public static Music musicPresent;
    public static boolean contactGround;
    private  Sound sound;
    float x;
    float y;

    private Rectangle mapSize;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        animation = new Anim("counter", Animation.PlayMode.LOOP);
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new TmxMapLoader().load("map/map2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        initMusic();
        contactGround = false;
//        sound = Gdx.audio.newSound((Gdx.files.internal("")));  //написать имя аудио из папки assets
        bodies = new ArrayList<>();
        rock = new Texture("rock.png");

        bg = new int[1];
        bg[0] = map.getLayers().getIndex("main");
        l1 = new int[1];
        l1[0] = map.getLayers().getIndex("grass");
        physX = new PhysX();

        RectangleMapObject rmp = (RectangleMapObject) map.getLayers().get("dynamic_objects").getObjects().get("hero");
        heroRect = rmp.getRectangle();
        bodyHero = physX.addObject(rmp, 0);
        rmp = (RectangleMapObject) map.getLayers().get("dynamic_objects").getObjects().get("ball");
        ballRect = rmp.getRectangle();
        bodyBall = physX.addObject(rmp, 1);
        rmp = (RectangleMapObject) map.getLayers().get("border").getObjects().get("border");
        mapSize = rmp.getRectangle();
        Array<RectangleMapObject> objects = map.getLayers().get("static_objects").getObjects().getByType(RectangleMapObject.class);
//        objects.addAll(map.getLayers().get("kinematic_objects").getObjects().getByType(RectangleMapObject.class));
//        objects.addAll(map.getLayers().get("dynamic_objects").getObjects().getByType(RectangleMapObject.class));
        for (int i = 0; i < objects.size; i++) {
           physX.addObject(objects.get(i), 1);

        }
        x = mapSize.x;
        y = mapSize.y;
        direction = true;
        camera.zoom = 0.5f;
    }

    private void initMusic() {
        musicHero = Gdx.audio.newMusic(Gdx.files.internal("hero.wav")); //написать имя музыки из папки assets
        musicHero.setLooping(false); //повторяемость
        musicHero.setVolume(0.5f);
        musicBall = Gdx.audio.newMusic(Gdx.files.internal("rock.wav")); //написать имя музыки из папки assets
        musicBall.setLooping(false); //повторяемость
        musicBall.setVolume(0.5f);
        musicGameOver = Gdx.audio.newMusic(Gdx.files.internal("gameover.wav")); //написать имя музыки из папки assets
        musicGameOver.setLooping(false); //повторяемость
        musicGameOver.setVolume(0.5f);
        musicPresent = Gdx.audio.newMusic(Gdx.files.internal("present.wav"));
        musicGameOver.setLooping(false); //повторяемость
        musicGameOver.setVolume(0.5f);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            bodyHero.applyForceToCenter(new Vector2(-1000, 0), true);
            animation = new Anim("start", Animation.PlayMode.LOOP);
            direction = true;}
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            bodyHero.applyForceToCenter(new Vector2(1000, 0), true);
            animation = new Anim("start", Animation.PlayMode.LOOP);
            direction = false;}
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && contactGround) {
            bodyHero.applyForceToCenter(new Vector2(0, 3000), true);
            animation = new Anim("jump", Animation.PlayMode.LOOP);}
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            bodyHero.applyForceToCenter(new Vector2(0, -3000), true);
            animation = new Anim("jump", Animation.PlayMode.LOOP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            animation = new Anim("punch", Animation.PlayMode.LOOP);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) camera.zoom += 0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.1f;

        if (direction && animation.getFrame().isFlipX()) {
            animation.getFrame().flip(true, false);
        }
        if (!direction && !animation.getFrame().isFlipX()) {
            animation.getFrame().flip(true, false);}

        if (musicGameOver.isPlaying()) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }


        camera.position.x = bodyHero.getPosition().x;
        camera.position.y = bodyHero.getPosition().y;
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

        Vector2 path = bodyHero.getLinearVelocity(); //направление героя
        System.out.println(path);
        batch.setProjectionMatrix(camera.combined);
        heroRect.x = bodyHero.getPosition().x - heroRect.width/2;
        heroRect.y = bodyHero.getPosition().y - heroRect.height/2;
        ballRect.x = bodyBall.getPosition().x - ballRect.width/2;
        ballRect.y = bodyBall.getPosition().y - ballRect.height/2;
        batch.begin();
        batch.draw(rock, ballRect.x, ballRect.y, ballRect.width, ballRect.height);
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

//        for (int i = 0; i < bodies.size(); i++)
//         {
////             bodies.get(i).setType(BodyDef.BodyType.DynamicBody);
////             bodies.get(i).setGravityScale(0.0f);
////            physX.deleteBody(bodies.get(i)); // к примеру
//
//        }
        if (bodies.size() > 0) {
            bodyBall.setGravityScale(5.0f);
            bodyBall.applyForceToCenter(new Vector2(0, -1000000), true);
        }
        bodies.clear();
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
        shapeRenderer.dispose();
        musicHero.dispose();
        musicGameOver.dispose();
        musicBall.dispose();
//        sound.dispose();
    }
}
