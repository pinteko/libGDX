package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Main;

public class GameOverScreen implements Screen {
    private Main game;

    private final SpriteBatch batch;

    private final Texture img;
    private final Texture button_again;
    private final Texture button_exit;
    private final Rectangle againRect;
    private final Rectangle exitRect;

    private final Music music;

    private ShapeRenderer shapeRenderer;

    public GameOverScreen(Main main) {
        this.game = main;
        batch = new SpriteBatch();
        img = new Texture("GameO.jpg");
        button_again = new Texture("tryAgain.jpg");
        button_exit = new Texture("exit.jpg");
        againRect = new Rectangle(0, 0, button_again.getWidth(), button_again.getHeight());
        exitRect = new Rectangle(Gdx.graphics.getWidth() - button_exit.getWidth(),
                0, button_exit.getWidth(), button_exit.getHeight());
        shapeRenderer = new ShapeRenderer();
        music = Gdx.audio.newMusic(Gdx.files.internal("gameover.wav")); //написать имя музыки из папки assets
        music.setLooping(true); //повторяемость
        music.setVolume(0.25f);
        music.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(Color.BLUE);

        batch.begin();
        batch.draw(img, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(button_again, 0, 0, button_again.getWidth(), button_again.getHeight());
        batch.draw(button_exit, Gdx.graphics.getWidth() - button_exit.getWidth(),
                0, button_exit.getWidth(), button_exit.getHeight());
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(againRect.x, againRect.y, againRect.width, againRect.height);
        shapeRenderer.rect(exitRect.x, exitRect.y, exitRect.width, exitRect.height);
        shapeRenderer.end();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            int x = Gdx.input.getX();
            int y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (againRect.contains(x, y)) {
                dispose();
                game.setScreen(new GameScreen(game));
            }
            if (exitRect.contains(x, y)) {
                dispose();
            }
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
            img.dispose();
            music.dispose();
            shapeRenderer.dispose();
        }
}
