package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;

public class GameScreen implements Screen {
    Main game;
    private SpriteBatch batch;
    private Anim animation;
    boolean direction;
    private Texture button_back;
    private ShapeRenderer shapeRenderer;

    private Rectangle backRect;
    float x;
    float y;

    float animPositionX;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        animation = new Anim("man.png", 6, 4, Animation.PlayMode.LOOP);
        button_back = new Texture("back.png");
        backRect = new Rectangle(0, Gdx.graphics.getHeight() - button_back.getHeight(),
                button_back.getWidth(), button_back.getHeight());
        shapeRenderer = new ShapeRenderer();
        x = 0;
        y = 0;
        direction = true;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

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

        batch.begin();
        batch.draw(animation.getFrame(), x, y);
        batch.draw(button_back, 0, Gdx.graphics.getHeight() - button_back.getHeight());
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(backRect.x, backRect.y, backRect.width, backRect.height);
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
