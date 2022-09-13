package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class GameScreen implements Screen {
    private Main game;
    private SpriteBatch batch;

    private Enemy enemy;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Texture rock;
    private final int[] bg;
    private final int[] l1;
    public static PhysX physX;
    private Hero hero;
    private final Body bodyHero;
    private final Body bodyBall;
    private final Body bodyEnemy;
    private final Rectangle heroRect;
    private final Rectangle ballRect;
    private final Rectangle enemyRect;
    public static ArrayList<Body> enemiesToDelete;
    private static ArrayList<Enemy> enemies;

    private Array<RectangleMapObject> staticObjects;
    public static  Music musicHero;
    public static  Music musicBall;
    public static  Music musicGameOver;
    public static Music musicPresent;
    public static boolean stopEnemy;
    public static boolean jumpInAir;
    public static boolean rockMove;
    public static boolean bodyBallActive;
    public static boolean bodyEnemyActive;
    float x;
    float y;
    private static int life;
    private BitmapFont font;
    private Rectangle mapSize;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        hero = new Hero();

        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new TmxMapLoader().load("map/map2.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        initMusic();

        enemiesToDelete = new ArrayList<>();
        enemies = new ArrayList<>();
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
        rmp = (RectangleMapObject) map.getLayers().get("dynamic_objects").getObjects().get("enemy");
        enemyRect = rmp.getRectangle();
        bodyEnemy = physX.addObject(rmp, 0);
        enemy = new Enemy("enemy.png", 6, 5, Animation.PlayMode.LOOP, bodyEnemy, enemyRect);
        enemies.add(enemy);
        rmp = (RectangleMapObject) map.getLayers().get("border").getObjects().get("border");
        mapSize = rmp.getRectangle();
        staticObjects = map.getLayers().get("static_objects").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < staticObjects.size; i++) {
           physX.addObject(staticObjects.get(i), 1);

        }
        x = mapSize.x;
        y = mapSize.y;
        stopEnemy = false;
        jumpInAir = false;
        rockMove = false;
        bodyBallActive = true;
        bodyEnemyActive = true;
        life = 100;
        font = new BitmapFont();
        font.setColor(Color.BLACK);
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

        float dt = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) camera.zoom += 0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.1f;

        if (rockMove) {
            bodyBall.setGravityScale(5.0f);
            bodyBall.applyForceToCenter(new Vector2(0, -10000), true);}

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

        camera.position.x = bodyHero.getPosition().x;
        camera.position.y = bodyHero.getPosition().y;
        camera.update();
        ScreenUtils.clear(Color.OLIVE);

        mapRenderer.setView(camera);
        mapRenderer.render(bg);

        Vector2 path = bodyHero.getLinearVelocity(); //направление героя
        System.out.println(path);
        batch.setProjectionMatrix(camera.combined);
        heroRect.x = bodyHero.getPosition().x - heroRect.width/2;
        heroRect.y = bodyHero.getPosition().y - heroRect.height/2;
        ballRect.x = bodyBall.getPosition().x - ballRect.width/2;
        ballRect.y = bodyBall.getPosition().y - ballRect.height/2;
        enemyRect.x = bodyEnemy.getPosition().x - enemyRect.width/2;
        enemyRect.y = bodyEnemy.getPosition().y - enemyRect.height/2;
        update(dt, staticObjects, enemies, physX);
        batch.begin();
        font.draw(batch, "Your life:" + life, camera.position.x - camera.viewportWidth / 4.5f,
                camera.position.y - font.getXHeight() + camera.viewportHeight / 4.5f);
        if (bodyBallActive) {
        batch.draw(rock, ballRect.x, ballRect.y, ballRect.width, ballRect.height);}
        hero.render(batch, bodyHero, heroRect, dt);
        if (enemy.isBodyEnemyActive() && bodyEnemyActive) {
            enemy.render(batch, dt);}
        BulletEmitter.getInstance().render(batch, Hero.isDirectionHero());
        batch.end();

        mapRenderer.render(l1);
        physX.step();
        physX.debugDraw(camera);

        if (enemiesToDelete.size() > 0) {
            for (int i = 0; i < enemiesToDelete.size(); i++) {
                physX.deleteBody(enemiesToDelete.get(i)); // к примеру
            }
        }
        enemiesToDelete.clear();



    }

    public void update(float dt, Array<RectangleMapObject> staticObjects, ArrayList<Enemy> enemies, PhysX physX) {
        hero.update(dt, staticObjects, enemies, physX);
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
        this.game.dispose();
        this.batch.dispose();
        this.enemy.dispose();
        this.hero.dispose();
        this.shapeRenderer.dispose();
        this.map.dispose();
        this.mapRenderer.dispose();
        this.rock.dispose();
        this.font.dispose();
        physX.dispose();
        musicHero.dispose();
        musicGameOver.dispose();
        musicBall.dispose();
        musicPresent.dispose();
    }
}
