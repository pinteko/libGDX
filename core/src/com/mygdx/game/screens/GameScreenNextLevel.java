package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.mygdx.game.*;

import java.util.ArrayList;

public class GameScreenNextLevel implements Screen {
    private Main game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer mapRenderer;
    public static PhysX physX;
    private Hero hero;
    private final Body bodyHero;
//    private final Body bodyEnemy;
    private final Rectangle heroRect;
//    private final Rectangle enemyRect;
    public static ArrayList<Body> enemiesToDelete;
    public static ArrayList<Body> enemiesToReverse;
    public static ArrayList<Enemy> enemies;
    private Array<RectangleMapObject> staticObjects;
    private Array<RectangleMapObject> sensors;
    private Array<RectangleMapObject> enemiesObjects;
    public static Music musicHero;
    public static  Music musicGameOver;
    public static Music musicPresent;
    public static boolean stopEnemy;
    public static boolean jumpInAir;
    float x;
    float y;
    private final int[] bg;
    private final int[] l1;
    private static int life;
    private BitmapFont font;
    private Rectangle mapSize;


    public GameScreenNextLevel(Main game, String mapName) {
        this.game = game;
        physX = new PhysX();
        this.hero = new Hero(physX);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TiledMap map = new TmxMapLoader().load(mapName); //!!!!!
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        initMusic();

        enemiesToDelete = new ArrayList<>();
        enemiesToReverse = new ArrayList<>();
        enemies = new ArrayList<>();

        bg = new int[1];
        bg[0] = map.getLayers().getIndex("main");
        l1 = new int[2];
        l1[0] = map.getLayers().getIndex("bg1");
        l1[1] = map.getLayers().getIndex("bg1");

        RectangleMapObject rmp = (RectangleMapObject) map.getLayers().get("dynamic_objects").getObjects().get("hero");
        heroRect = rmp.getRectangle();
        bodyHero = physX.addObject(rmp, 0);
        rmp = (RectangleMapObject) map.getLayers().get("border").getObjects().get("border");
        mapSize = rmp.getRectangle();
        enemiesObjects = map.getLayers().get("enemies").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < enemiesObjects.size; i++) {
            Rectangle enemyRect = enemiesObjects.get(i).getRectangle();
            Body bodyEnemy = physX.addObject(enemiesObjects.get(i), 1);
           Enemy enemy = new Enemy("enemy.png", 6, 5, Animation.PlayMode.LOOP, bodyEnemy, enemyRect);
            enemies.add(enemy);
        }
        staticObjects = map.getLayers().get("static_objects").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < staticObjects.size; i++) {
            physX.addObject(staticObjects.get(i), 1);
        }
        sensors = map.getLayers().get("sensors").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < sensors.size; i++) {
            physX.addObject(sensors.get(i), 1);
        }
        x = mapSize.x;
        y = mapSize.y;
        stopEnemy = false;
        jumpInAir = false;
        life = 100;
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        camera.zoom = 0.5f;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) camera.zoom += 0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.1f;

        camera.position.x = bodyHero.getPosition().x;
        camera.position.y = bodyHero.getPosition().y;
        camera.update();
        ScreenUtils.clear(Color.OLIVE);
        mapRenderer.render(l1);
        mapRenderer.setView(camera);
        mapRenderer.render(bg);

        Vector2 path = bodyHero.getLinearVelocity(); //направление героя
//        System.out.println(path);
        batch.setProjectionMatrix(camera.combined);
        heroRect.x = bodyHero.getPosition().x - heroRect.width/2;
        heroRect.y = bodyHero.getPosition().y - heroRect.height/2;
        update(dt, staticObjects, enemies);
        batch.begin();
        font.draw(batch, "Your life:" + life, camera.position.x - camera.viewportWidth / 4.5f,
                camera.position.y - font.getXHeight() + camera.viewportHeight / 4.5f);
        hero.render(batch, bodyHero, heroRect, dt);
        for (int i = 0; i < enemiesObjects.size; i++) {
            if (enemies.get(i).isBodyEnemyActive()) {
                enemies.get(i).render(batch, dt);}
        }
        BulletEmitter.getInstance().render(batch, Hero.isDirectionHero());
        batch.end();


        physX.step();
        physX.debugDraw(camera);

        if (enemiesToDelete.size() > 0) {
            for (int i = 0; i < enemiesToDelete.size(); i++) {
                physX.deleteBody(enemiesToDelete.get(i)); // к примеру
            }
        }
        enemiesToDelete.clear();

        if (enemiesToReverse.size() > 0) {
            for (Enemy enemy : enemies) {
                for (Body body : enemiesToReverse) {
                    if (enemy.getBodyEnemy().equals(body)) {
                        enemy.setContactEnemySensor(true);
                    }
                }
            }
        }

        if (musicGameOver.isPlaying()) {
            life -= 50;
            musicGameOver.stop();
            if (life == 0) {
                dispose();
                game.setScreen(new GameOverScreen(game));}
        }

        if (musicPresent.isPlaying()) {
            dispose();
            game.setScreen(new WinScreen(game));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }


    }

    public void update(float dt, Array<RectangleMapObject> staticObjects, ArrayList<Enemy> enemies) {
        hero.update(dt, staticObjects, enemies);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    private void initMusic() {
        musicHero = Gdx.audio.newMusic(Gdx.files.internal("hero.wav")); //написать имя музыки из папки assets
        musicHero.setLooping(false); //повторяемость
        musicHero.setVolume(0.5f);
        musicGameOver = Gdx.audio.newMusic(Gdx.files.internal("gameover.wav")); //написать имя музыки из папки assets
        musicGameOver.setLooping(false); //повторяемость
        musicGameOver.setVolume(0.5f);
        musicPresent = Gdx.audio.newMusic(Gdx.files.internal("present.wav"));
        musicGameOver.setLooping(false); //повторяемость
        musicGameOver.setVolume(0.5f);
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
        this.game.dispose();
        this.batch.dispose();
        this.hero.dispose();
        this.shapeRenderer.dispose();
        this.mapRenderer.dispose();
        this.font.dispose();
        physX.dispose();
        musicHero.dispose();
        musicGameOver.dispose();
        musicPresent.dispose();
    }
}
