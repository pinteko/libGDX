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
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;

public class GameScreen implements Screen {
    private static final float STEP = 12;
    Main game;
    private SpriteBatch batch;
    private Anim animation;
    boolean direction;
    private Texture button_back;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Rectangle backRect;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    float x;
    float y;

    private Rectangle mapSize;

    float animPositionX;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        animation = new Anim("man.png", 6, 4, Animation.PlayMode.LOOP);
        button_back = new Texture("back.png");
        backRect = new Rectangle(0, Gdx.graphics.getHeight() - button_back.getHeight(),
                button_back.getWidth(), button_back.getHeight());
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new TmxMapLoader().load("map/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        RectangleMapObject rmp = (RectangleMapObject) map.getLayers().get("Objects").getObjects().get("camera");
        camera.position.x = rmp.getRectangle().x;
        camera.position.y = rmp.getRectangle().y;
        rmp = (RectangleMapObject) map.getLayers().get("Objects").getObjects().get("border");
        mapSize = rmp.getRectangle();
        x = mapSize.x;
        y = mapSize.y;
        direction = true;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.A) && mapSize.x < camera.position.x - 1) camera.position.x -= STEP;
        if (Gdx.input.isKeyJustPressed(Input.Keys.D) && mapSize.x + mapSize.width > camera.position.x + 1) camera.position.x += STEP;
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) camera.position.y += STEP;
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) camera.position.y -= STEP;

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) camera.zoom += 0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.1f;

        camera.update();
        ScreenUtils.clear(Color.YELLOW);
        animation.setTime(Gdx.graphics.getDeltaTime());

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) direction = true;
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) direction = false;
        if (!animation.getFrame().isFlipX() && direction) animation.getFrame().flip(true, false);
        if (animation.getFrame().isFlipX() && !direction) animation.getFrame().flip(true, false);

        if (animation.getFrame() == animation.jump()[7]) {y += animation.getFrame().getRegionHeight()/2f;}
        if (animation.getFrame() == animation.jump()[8]) {y += animation.getFrame().getRegionHeight()/2f;}
        if (animation.getFrame() == animation.jump()[9]) {y -= animation.getFrame().getRegionHeight()/2f;}
        if (animation.getFrame() == animation.jump()[10]) {y -= animation.getFrame().getRegionHeight()/2f;}
		/* тут спрайт на картинке прыгает, и я его на двух кадрах подкидываю вверх, на двух опускаю вниз
		в соответствии с картинкой */

        if (x >= Gdx.graphics.getWidth() - animation.getFrame().getRegionWidth()) {
            animation.getFrame().flip(true, false);
            direction = false;
        }

        if (x <= 0) {
            animation.getFrame().flip(true, false);
            direction = true;
        }

        if (direction) {
            x += animation.getFrame().getRegionWidth()/100f;
        } else {
            x -= animation.getFrame().getRegionWidth()/100f;
        }

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(animation.getFrame(), x, y);
        batch.draw(button_back, mapSize.x, mapSize.y - mapSize.height + backRect.y);
        batch.end();



        mapRenderer.setView(camera);
        mapRenderer.render();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(mapSize.x, mapSize.y - mapSize.height + backRect.y, backRect.width, backRect.height);
        shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
        shapeRenderer.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (backRect.contains(x, y)) {
                dispose();
                game.setScreen(new MenuScreen(game)); }
        }
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
